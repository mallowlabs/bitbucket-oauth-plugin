package org.jenkinsci.plugins.api;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class BitbucketUserPrivileges {

    @SerializedName("teams")
    Map<String, String> teams;

    public Map<String, String> getTeams()
    {
        return teams;
    }

    public void setTeams(Map<String, String> teams)
    {
        this.teams = teams;
    }
}
