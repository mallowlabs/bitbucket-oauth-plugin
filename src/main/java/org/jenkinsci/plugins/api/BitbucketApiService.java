package org.jenkinsci.plugins.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.api.BitbucketUser.BitbucketUserResponce;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;

public class BitbucketApiService {

    private static final Logger LOGGER = Logger.getLogger(BitbucketApiService.class.getName());

    private static final String API_ENDPOINT = "https://api.bitbucket.org/1.0/";
    private static final String API2_ENDPOINT = "https://api.bitbucket.org/2.0/";

    private OAuthService service;

    public BitbucketApiService(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, null);
    }

    public BitbucketApiService(String apiKey, String apiSecret, String callback) {
        super();
        ServiceBuilder builder = new ServiceBuilder().provider(BitbucketApi.class).apiKey(apiKey).apiSecret(apiSecret);
        if (StringUtils.isNotBlank(callback)) {
            builder.callback(callback);
        }
        service = builder.build();
    }

    public Token createRquestToken() {
        return service.getRequestToken();
    }

    public String createAuthorizationCodeURL(Token requestToken) {
        return service.getAuthorizationUrl(requestToken);
    }

    public Token getTokenByAuthorizationCode(String code, Token requestToken) {
        Verifier v = new Verifier(code);
        return service.getAccessToken(requestToken, v);
    }

    public BitbucketUser getUserByToken(Token accessToken) {
        OAuthRequest request = new OAuthRequest(Verb.GET, API_ENDPOINT + "user");
        service.signRequest(accessToken, request);
        Response response = request.send();
        String json = response.getBody();
        Gson gson = new Gson();
        BitbucketUserResponce userResponce = gson.fromJson(json, BitbucketUserResponce.class);
        if (userResponce != null && userResponce.user != null) {

            userResponce.user.addAuthority("authenticated");

            findAndAddUserTeamAccess(accessToken, userResponce.user, "admin");
            findAndAddUserTeamAccess(accessToken, userResponce.user, "contributor");
            findAndAddUserTeamAccess(accessToken, userResponce.user, "member");

            return userResponce.user;

        } else {
            return null;
        }
    }

    private void findAndAddUserTeamAccess(Token accessToken, BitbucketUser bitbucketUser, String role) {
        Gson gson = new Gson();
        String url = API2_ENDPOINT + "teams/?role=" + role;
        try {
            do {
                OAuthRequest request1 = new OAuthRequest(Verb.GET, url);
                service.signRequest(accessToken, request1);
                Response response1 = request1.send();
                String json1 = response1.getBody();

                LOGGER.finest("Response from bitbucket api " + url);
                LOGGER.finest(json1);

                BitBucketTeamsResponse bitBucketTeamsResponse = gson.fromJson(json1, BitBucketTeamsResponse.class);

                if (CollectionUtils.isNotEmpty(bitBucketTeamsResponse.getTeamsList())) {
                    for (Teams team : bitBucketTeamsResponse.getTeamsList()) {
                        String authority = team.getUsername() + "::" + role;
                        bitbucketUser.addAuthority(authority);
                    }
                }
                url = bitBucketTeamsResponse.getNext();
            } while (url != null);
        } catch (Exception e) {
            // Some error, So ignore it and move on.
            e.printStackTrace();
        }
    }

    public UserDetails getUserByUsername(String username) {
        InputStreamReader reader = null;
        BitbucketUserResponce userResponce = null;
        try {
            URL url = new URL(API_ENDPOINT + "users/" + username);
            reader = new InputStreamReader(url.openStream(), "UTF-8");
            Gson gson = new Gson();
            userResponce = gson.fromJson(reader, BitbucketUserResponce.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }

        if (userResponce != null) {
            return userResponce.user;
        } else {
            return null;
        }
    }

}