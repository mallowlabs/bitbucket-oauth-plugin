package org.jenkinsci.plugins.api;

import hudson.security.GroupDetails;

public class BitbucketGroup extends GroupDetails
{
    private String name;

    public BitbucketGroup(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
