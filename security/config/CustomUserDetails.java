package com.personal.security.config;


import com.personal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述：自定义UserDetails，使UserDetails具有TUser的实体结构
 * 自定义SpringSecurity的认证器
 * @author
 * @date 2020/06/24 15:43
 **/

public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(User tUser){
        if(null != tUser){
            this.setId(tUser.getId());
            this.setCode(tUser.getCode());
            this.setCreateTime(tUser.getCreateTime());
            this.setUpdateTime(tUser.getUpdateTime());
            this.setUsername(tUser.getUsername());
            this.setPassword(tUser.getPassword());
            this.setRole(tUser.getRole());
            this.setRealname(tUser.getRealname());
            this.setLicense(tUser.getLicense());
            this.setDeadline(tUser.getDeadline());
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> permsSet = new HashSet<GrantedAuthority>();
        if("2".equals(this.getRole())){
            permsSet.add(new SimpleGrantedAuthority("admin"));
        }else if("1".equals(this.getRole())){
            permsSet.add(new SimpleGrantedAuthority("custom"));
        }
        return permsSet;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
