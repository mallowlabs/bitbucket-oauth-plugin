package org.jenkinsci.plugins.api;

public class BitbucketMissingPermissionException extends RuntimeException {

    public BitbucketMissingPermissionException(String message) {
        super(message);
    }
}
