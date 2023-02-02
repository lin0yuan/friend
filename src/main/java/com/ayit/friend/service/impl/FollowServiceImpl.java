package com.ayit.friend.service.impl;

import com.ayit.friend.pojo.User;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.RedisConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ayit.friend.pojo.Follow;
import com.ayit.friend.service.FollowService;
import com.ayit.friend.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 86138
* @description 针对表【follow(关注与粉丝)】的数据库操作Service实现
* @createDate 2022-12-04 15:45:54
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void addTutor(Long tutorId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Follow follow = new Follow(user.getId(),tutorId);
        this.save(follow);
    }

    @Override
    public Integer getRelation(Long toUserId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Follow> queryWrapper1 = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getSubscriberId,user.getId())
                .eq(Follow::getTutorId,toUserId);
        List<Follow> list1 = this.list(queryWrapper1);
        LambdaQueryWrapper<Follow> queryWrapper2 = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getTutorId,user.getId())
                .eq(Follow::getSubscriberId,toUserId);
        List<Follow> list2 = this.list(queryWrapper2);
        if(list1.size()==0&&list2.size()==0){
            return 0;
        }else if(list1.size()>=1&&list2.size()>=1){
            return 3;
        }else if(list1.size()>=1&&list2.size()==0){
            return 2;
        }
        return 1;
    }


    @Override
    public void removeFollow(Follow follow) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getSubscriberId,follow.getSubscriberId())
                .eq(Follow::getTutorId,follow.getTutorId());
        this.remove(queryWrapper);
    }


    private List<Long> listMutually(Long userId) {
        LambdaQueryWrapper<Follow> queryWrapperTutor = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getSubscriberId,userId)
                .select(Follow::getTutorId);
        Function<Object,Long> function = (follow->Long.valueOf(follow.toString()));
        List<Long> tutorList = this.listObjs(queryWrapperTutor,function);
        if(tutorList.size()==0){
            return tutorList;
        }
        LambdaQueryWrapper<Follow> queryWrapperSubscr = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getTutorId,userId)
                .in(Follow::getSubscriberId,tutorList)
                .select(Follow::getSubscriberId);
        List<Long> friendList = this.listObjs(queryWrapperSubscr,function);
        return friendList;
    }

    @Autowired
    private UserService userService;
    @Override
    public Page<User> pageMutually(Long userId, Integer page, Integer size) {
        List<Long> tutorList = this.listMutually(userId);
        Page<User> userPage = new Page<>();
        userPage.setCurrent(page).setSize(size);
        if(tutorList.size()==0){
            return userPage;
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .in(User::getId,tutorList);
        userPage = userService.page(userPage,queryWrapper);
        return userPage;
    }

    public Page<User> listSubscriber(Long id,Integer page,Integer size){
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getTutorId,id)
                .select(Follow::getSubscriberId);
        return getPage(queryWrapper,page,size);
    }

    @Override
    public Page<User> listTutor(Long id,Integer page,Integer size) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<Follow>()
                .eq(Follow::getSubscriberId,id)
                .select(Follow::getTutorId);
       return getPage(queryWrapper,page,size);
    }

    private Page<User> getPage(LambdaQueryWrapper<Follow> queryWrapper,Integer page,Integer size){
        Function<Object,Long> function = follow->Long.valueOf(follow.toString());
        List<Long> list = this.listObjs(queryWrapper, function);
        Page<User> userPage = new Page<>();
        if(list.size()==0){
            userPage.setTotal(0);
            return userPage;
        }
        LambdaQueryWrapper<User> queryWrapperUser = new LambdaQueryWrapper<User>()
                .in(User::getId,list);
        userPage = userService.page(userPage,queryWrapperUser);
        userPage.setCurrent(page).setSize(size);
        return userPage;
    }


}




