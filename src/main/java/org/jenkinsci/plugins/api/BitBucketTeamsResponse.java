package org.jenkinsci.plugins.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Represents Bitbuckets team api response
 *
 * https://developer.atlassian.com/bitbucket/api/2/reference/resource/teams
 */
public class BitBucketTeamsResponse
{
    @SerializedName("next")
    private String next;

    @SerializedName("values")
    private List<BitbucketTeams> teamsList;

    public List<BitbucketTeams> getTeamsList()
    {
        return teamsList;
    }

    public void setTeamsList(List<BitbucketTeams> teamsList)
    {
        this.teamsList = teamsList;
    }

    public String getNext()
    {
        return next;
    }

    public void setNext(String next)
    {
        this.next = next;
    }
}
