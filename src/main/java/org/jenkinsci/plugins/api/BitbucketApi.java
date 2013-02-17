package org.jenkinsci.plugins.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class BitbucketApi extends DefaultApi10a {
    private static final String OAUTH_ENDPOINT = "https://bitbucket.org/api/1.0/oauth/";

    @Override
    public String getAccessTokenEndpoint() {
        return OAUTH_ENDPOINT + "access_token";
    }

    @Override
    public String getAuthorizationUrl(Token oauthToken) {
        return OAUTH_ENDPOINT + "authenticate?oauth_token=" + oauthToken.getToken();
    }

    @Override
    public String getRequestTokenEndpoint() {
        return OAUTH_ENDPOINT + "request_token";
    }
}