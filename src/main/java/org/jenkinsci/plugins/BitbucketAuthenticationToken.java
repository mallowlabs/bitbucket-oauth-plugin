package org.jenkinsci.plugins;

import com.github.scribejava.core.model.OAuth2AccessToken;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.AbstractAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.api.BitbucketApiService;
import org.jenkinsci.plugins.api.BitbucketUser;

public class BitbucketAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -7826610577724673531L;

    private OAuth2AccessToken accessToken;
    private BitbucketUser bitbucketUser;

    public BitbucketAuthenticationToken(OAuth2AccessToken accessToken, String apiKey, String apiSecret) {
        super(null);

        this.accessToken = accessToken;
        this.bitbucketUser = new BitbucketApiService(apiKey, apiSecret).getUserByToken(accessToken);

        boolean authenticated = false;

        if (bitbucketUser != null) {
            authenticated = true;
        }

        setAuthenticated(authenticated);
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return this.bitbucketUser != null ? this.bitbucketUser.getAuthorities() : new GrantedAuthority[0];
    }

    /**
     * @return the accessToken
     */
    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public Object getCredentials() {
        return StringUtils.EMPTY;
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public String getName() {
        return (bitbucketUser != null ? bitbucketUser.getUsername() : null);
    }

}
