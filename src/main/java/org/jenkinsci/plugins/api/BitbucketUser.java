package org.jenkinsci.plugins.api;

import hudson.security.SecurityRealm;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

import com.google.gson.annotations.SerializedName;

public class BitbucketUser implements UserDetails {

    public class BitbucketUserResponce {
        public BitbucketUser user;
    }

    public String username;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("is_team")
    public boolean isTeam;
    public String avatar;
    @SerializedName("resource_uri")
    public String resourceUri;

    public BitbucketUser() {
        super();
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[] { SecurityRealm.AUTHENTICATED_AUTHORITY };
    }

    @Override
    public String getPassword() {
        return null;
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

}
