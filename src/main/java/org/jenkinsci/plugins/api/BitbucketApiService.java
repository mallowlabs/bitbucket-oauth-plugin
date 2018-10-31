package org.jenkinsci.plugins.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class BitbucketApiService {

    private static final Logger LOGGER = Logger.getLogger(BitbucketApiService.class.getName());
    private static final String API2_ENDPOINT = "https://api.bitbucket.org/2.0/";

    private OAuth20Service service;

    public BitbucketApiService(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, null);
    }

    public BitbucketApiService(String apiKey, String apiSecret, String callback) {
        super();
        ServiceBuilder builder = new ServiceBuilder(apiKey).apiKey(apiKey).apiSecret(apiSecret);
        if (StringUtils.isNotBlank(callback)) {
            builder.callback(callback);
        }
        service = builder.build(BitbucketApi.instance());
    }

    public String createAuthorizationCodeURL() {
        return service.getAuthorizationUrl();
    }

    public OAuth2AccessToken getTokenByAuthorizationCode(String code) {
        OAuth2AccessToken token = null;
        try {
            token = service.getAccessToken(code);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

    public BitbucketUser getUserByToken(OAuth2AccessToken accessToken) {
        BitbucketUser bitbucketUser = getBitbucketUser(accessToken);

        bitbucketUser.addAuthority("authenticated");

        findAndAddUserTeamAccess(accessToken, bitbucketUser, "admin");
        findAndAddUserTeamAccess(accessToken, bitbucketUser, "contributor");
        findAndAddUserTeamAccess(accessToken, bitbucketUser, "member");

        return bitbucketUser;
    }

    private BitbucketUser getBitbucketUser(OAuth2AccessToken accessToken) {
        BitbucketUser bitbucketUser = getBitbucketUserV2(accessToken);
        if (bitbucketUser != null) {
            return bitbucketUser;
        }
        throw new BitbucketMissingPermissionException(
                "Your Bitbucket credentials lack one required privilege scopes: [Account Read]");
    }

    private BitbucketUser getBitbucketUserV2(OAuth2AccessToken accessToken) {
        // require "Account Read" permission
        BitbucketUser bitbucketUser = null;

        try {
            OAuthRequest request = new OAuthRequest(Verb.GET, API2_ENDPOINT + "user");
            service.signRequest(accessToken, request);
            Response response = service.execute(request);
            String json = response.getBody();
            Gson gson = new Gson();
            bitbucketUser = gson.fromJson(json, BitbucketUser.class);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }

        if (bitbucketUser == null || StringUtils.isEmpty(bitbucketUser.username)) {
            bitbucketUser = null;
        }

        return bitbucketUser;
    }

    private void findAndAddUserTeamAccess(OAuth2AccessToken accessToken, BitbucketUser bitbucketUser, String role) {
        // require "Team membership Read" permission
        Gson gson = new Gson();
        String url = API2_ENDPOINT + "teams/?role=" + role;
        try {
            do {
                OAuthRequest request1 = new OAuthRequest(Verb.GET, url);
                service.signRequest(accessToken, request1);
                Response response1 = service.execute(request1);
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
        UserDetails userResponce = null;

        try {
            String userName = URLEncoder.encode(username, "UTF-8");
            URL url = new URL(API2_ENDPOINT + "users/" + userName);
            LOGGER.fine(url.toString());
            reader = new InputStreamReader(url.openStream(), "UTF-8");
            Gson gson = new Gson();
            userResponce = gson.fromJson(reader, BitbucketUser.class);
        } catch (FileNotFoundException e) {
            LOGGER.warning("Can not found this bibucket user : " + username);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return userResponce;
    }
}
