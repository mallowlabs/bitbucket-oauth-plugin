package org.jenkinsci.plugins.api;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;

public class BitbucketUser implements UserDetails {

    public class BitbucketUserResponce {
        public BitbucketUser user;
    }

    public String username = StringUtils.EMPTY;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("is_team")
    public boolean isTeam;
    public String avatar;
    @SerializedName("resource_uri")
    public String resourceUri;

    List<GrantedAuthority> grantedAuthorties = new ArrayList<GrantedAuthority>();

    public BitbucketUser() {
        super();
    }

    @Override
    public GrantedAuthority[] getAuthorities() {

        return grantedAuthorties.toArray(new GrantedAuthority[grantedAuthorties.size()]);
    }

    public void addAuthority(String role)
    {
        grantedAuthorties.add(new GrantedAuthorityImpl(role));
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
