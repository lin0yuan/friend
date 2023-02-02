package com.ayit.friend.pojo.dto;

import com.ayit.friend.pojo.User;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDTO extends User implements UserDetails {
    @ApiModelProperty("身份集合")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(this.getUserRole()==0){
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }

    @ApiModelProperty("用户账户")
    @Override
    public String getUsername() {
        return this.getEmailAddress();
    }

    @ApiModelProperty("是否未过期")
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @ApiModelProperty("是否未锁定")
    @Override
    public boolean isAccountNonLocked() {
        return this.getUserStatus()!=2;
    }

    @ApiModelProperty("证书是否未过期")
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @ApiModelProperty("是否可用")
    @Override
    public boolean isEnabled() {
        return true;
    }

}
