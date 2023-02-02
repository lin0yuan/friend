package com.ayit.friend.service.impl;

import com.alibaba.cloud.context.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ayit.friend.common.CustomException;
import com.ayit.friend.enumeration.RoleType;
import com.ayit.friend.mapper.UserMapper;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.pojo.OauthUser;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.OAuth2UserDTO;
import com.ayit.friend.pojo.dto.UserDTO;
import com.ayit.friend.pojo.vo.UserVO;
import com.ayit.friend.service.FollowService;
import com.ayit.friend.service.OauthUserService;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.RedisConstants;
import com.ayit.friend.utils.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
* @author 86138
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-11-30 15:30:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService, UserDetailsService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO loadUserByUsername(String s){
        if(!UserUtil.isEmail(s)){
            return new UserDTO();
        }
        User user = this.query().eq("email_address",s).one();
        if(user==null){
            throw new CustomException("The account not exist");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OauthUserService oauthUserService;
    @Override
    public User bindUser(OAuth2UserDTO oAuth2UserDTO, String code) {
        User user = this.queryEmail(oAuth2UserDTO.getEmail());
        String codeCache = redisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + oAuth2UserDTO.getEmail());
        if(code==null&&!code.equals(codeCache)){
            throw new CustomException("验证码错误");
        }
        if(user==null){
            user = new User();
            user.setEmailAddress(oAuth2UserDTO.getEmail());
            user.setSurname(oAuth2UserDTO.getName());
            user.setAvatar(oAuth2UserDTO.getAvatar());
            String password = UserUtil.generatePassword(10);
            String encode = passwordEncoder.encode(password);
            user.setPassword(encode);
            this.save(user);
            rabbitTemplate.convertAndSend("ayit.user","insert",user.getId());
            user.setPassword(password);
            oauthUserService.addUserId(new OauthUser(oAuth2UserDTO.getSource(),oAuth2UserDTO.getId(),user.getId()));
        }else{
            oauthUserService.addUserId(new OauthUser(oAuth2UserDTO.getSource(),oAuth2UserDTO.getId(),user.getId()));
            user.setPassword(null);
        }
        this.updateState(user.getId(),0);
        user.setUserStatus(0);
        return user;
    }

    @Override
    public void updatePassword(Long id, String password) {
        this.update().eq("id",id).set("password",passwordEncoder.encode(password)).update();
    }


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void saveUser(User user,String code) {
        String codeCache = redisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY+user.getEmailAddress());
        if(StringUtils.isEmpty(code)||!code.equals(codeCache)){
            throw new CustomException("验证码错误");
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        if(UserUtil.isUsername(user.getSurname())&&this.queryEmail(user.getEmailAddress())==null){
            userMapper.insert(user);
        }
        rabbitTemplate.convertAndSend("ayit.user","insert",user.getId());
    }


    @Override
    public User queryEmail(String email) {
        if(!UserUtil.isEmail(email)){
            throw new CustomException("邮箱格式不合法");
        }
        User user = this.query().eq("email_address",email).one();
        return user;
    }

    @Override
    public void updateState(Long userId, Integer state) {
        this.update().eq("id",userId).set("user_status",state).update();
    }

    @Autowired
    private RestHighLevelClient client;
    @SneakyThrows
    @Override
    public Page<User> pageUser(UserVO userVO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SearchRequest request = new SearchRequest("friend_user");
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.mustNot(QueryBuilders.termQuery("id",user.getId()));
        if(StringUtils.isEmpty(userVO.getInfo())){
            queryBuilder.must(QueryBuilders.matchAllQuery());
        }else{
            queryBuilder.must(QueryBuilders.matchQuery("all",userVO.getInfo()));
        }
        if(userVO.getIds().size()>=1){
            List<String> list = userVO.getIds();
            String[] strings = list.toArray(new String[list.size()]);
            queryBuilder.must(QueryBuilders.idsQuery().addIds(strings));
        }
        if(userVO.getGender()!=null){
            queryBuilder.filter(QueryBuilders.termQuery("gender",userVO.getGender()));
        }
        Integer page = userVO.getPage();
        Integer size = userVO.getSize();
        request.source().query(queryBuilder).fetchSource(UserUtil.getUserFields(),null).from((page - 1) * size).size(size);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        List<User> userList = handleResponse(response);
        long value = response.getHits().getTotalHits().value;
        Page<User> userPage = new Page<User>().setCurrent(page).setSize(size).setRecords(userList).setTotal(value);
        return userPage;
    }

    @Override
    public boolean checkPassword(String password) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userById = this.getById(user.getId());
        boolean matches = passwordEncoder.matches(password, userById.getPassword());
        return matches;
    }

    @Override
    public void updateGender(Integer gender) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId,user.getId())
                .set(User::getGender,gender);
        this.update(updateWrapper);
    }

    private List<User> handleResponse(SearchResponse response) {
        SearchHit[] hits = response.getHits().getHits();
        List<User> userList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            User user = JSON.parseObject(json, User.class);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User getUserById(Long userId) {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId()!=userId){
            throw new CustomException("不是本人用户");
        }
        return this.getById(userId);
    }

}




