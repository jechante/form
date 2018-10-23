package com.schinta.wechat;

import com.riversoft.weixin.common.AccessToken;
import com.riversoft.weixin.common.AccessTokenHolder;

//@Service
public class CustomAccessTokenHolder extends AccessTokenHolder {
    private AccessToken accessToken;

    public CustomAccessTokenHolder(String tokenUrl, String clientId, String clientSecret) {
        super(tokenUrl, clientId, clientSecret);
    }

    public AccessToken getAccessToken() {
        if (this.accessToken == null || this.accessToken.expired()) {
            this.refreshToken();
        }

        return this.accessToken;
    }

    public void refreshToken() {
        if (this.accessToken == null || this.accessToken.expired()) {
            String content = this.fetchAccessToken();
            AccessToken accessToken = AccessToken.fromJson(content);
            this.accessToken = accessToken;
        }

    }

    public void expireToken() {
        this.accessToken.setExpiresIn(-30L);
    }
}

