package com.kun.sso.authentication.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collection;

public class User implements UserDetails ,SocialUserDetails {

   private Integer id;
   private String  username;
   private String  password;
   private String  nickName;
   private String  mobile;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getNickName() {
      return nickName;
   }

   public void setNickName(String nickName) {
      this.nickName = nickName;
   }

   public String getMobile() {
      return mobile;
   }

   public void setMobile(String mobile) {
      this.mobile = mobile;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return AuthorityUtils.createAuthorityList("ADMIN");
   }

   @Override
   public String getPassword() {
      return this.password;
   }

   @Override
   public String getUsername() {
      return this.username;
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

   /**
    * SocialUserDetails 重载方法返回一个用户唯一标识
    */
   @Override
   public String getUserId() {
      return id.toString();
   }
}
