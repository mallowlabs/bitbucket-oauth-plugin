package org.jenkinsci.plugins.api;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class BitbucketApi extends DefaultApi20 {
    private static final String TOKEN_URL = "https://bitbucket.org/site/oauth2/access_token";
    private static final String AUTHORIZE_URL = "https://bitbucket.org/site/oauth2/authorize";

    protected BitbucketApi() {
    }

    private static class InstanceHolder {
        private static final BitbucketApi INSTANCE = new BitbucketApi();
    }

    public static BitbucketApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
      return TOKEN_URL;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
