package com.ayit.friend.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OAuth2UserDTO implements OAuth2User {
     private String source;
     private String id;
     private String name;
     private String email;
     private String avatar;
     @JsonIgnore
     @JSONField(serialize = false)
     private List<GrantedAuthority> authorities= AuthorityUtils.createAuthorityList("ROLE_USER");
     @JsonIgnore
     @JSONField(serialize = false)
     private Map<String,Object> attributes;
     @Override
     public Map<String, Object> getAttributes() {
          if (attributes==null){
               attributes=new HashMap<>();
               attributes.put("id",this.getId());
               attributes.put("name",this.getName());
               attributes.put("email",this.getEmail());
          }

          return attributes;
     }

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return this.authorities;
     }
     public OAuth2UserDTO(Map<String,Object> attributes,String source){
          this.attributes = attributes;
          this.source = source;
          this.id = attributes.get("id").toString();
          this.name = attributes.get("name").toString();
          this.email = attributes.get("email")==null?null:attributes.get("email").toString();
          this.avatar = attributes.get("avatar_url")==null?null:attributes.get("avatar_url").toString();
     }
}
