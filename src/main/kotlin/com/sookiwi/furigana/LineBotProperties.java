package com.sookiwi.furigana;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("line.bot")
@Validated
public class LineBotProperties {
    private @NotEmpty String channelToken;

    private @NotEmpty String channelSecret;

    private @NotNull Handler handler;

    private @NotNull String yahoojpAppid;

    private @NotNull String giphyAppKey;

    public String getChannelToken() {
        return channelToken;
    }

    public void setChannelToken(String channelToken) {
        this.channelToken = channelToken;
    }

    public String getChannelSecret() {
        return channelSecret;
    }

    public void setChannelSecret(String channelSecret) {
        this.channelSecret = channelSecret;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getYahoojpAppid() {
        return yahoojpAppid;
    }

    public void setYahoojpAppid(final String yahoojpAppid) {
        this.yahoojpAppid = yahoojpAppid;
    }

    public String getGiphyAppKey() {
        return giphyAppKey;
    }

    public void setGiphyAppKey(final String giphyAppKey) {
        this.giphyAppKey = giphyAppKey;
    }

    public static class Handler {
        private @NotEmpty String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
