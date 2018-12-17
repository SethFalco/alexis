package com.elypia.alexis.logging;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.security.auth.login.LoginException;

/**
 * A logger for Discord which will redirect some of the logs
 * to a Discord channel.
 */
public final class DiscordAppender extends AppenderBase<ILoggingEvent> {

    private JDA jda;

    /**
     * The token used for authenticating to Discord.
     */
    private String token;

    private long id;

    private boolean failedAuthentication;

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (failedAuthentication)
            return;

        if (jda == null) {
            try {
                jda = new JDABuilder(token).build();
            } catch (LoginException e) {
                failedAuthentication = true;
                e.printStackTrace();
            }
        }

        String message = eventObject.getFormattedMessage();
        MessageChannel channel = jda.getPrivateChannelById(id);

        if (channel != null)
            channel.sendMessage(message).queue();
        else
            jda.getTextChannelById(id).sendMessage(message).queue();

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
