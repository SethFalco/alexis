package com.elypia.alexis;

import com.elypia.alexis.entities.*;
import com.elypia.commandler.interfaces.IScripts;
import com.elypia.elypiai.runescape.Activity;
import com.elypia.elypiai.runescape.*;
import com.elypia.elyscript.ScriptStore;
import com.elypia.elyscript.sheets.SheetsLoader;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class ElyScripts implements IScripts<GenericMessageEvent> {

    private ScriptStore scripts;

    public ElyScripts(String id, String range) throws GeneralSecurityException, IOException {
        this(null, id, range);
    }

    public ElyScripts(String applicationName, String id, String range) throws GeneralSecurityException, IOException {
        var loader = new SheetsLoader(applicationName, id, range);
        scripts = loader.load();
    }

    @Override
    public <T> String get(GenericMessageEvent source, String script, Map<String, T> params) {
        Objects.requireNonNull(source);

        Map<String, Object> scriptParams = new HashMap<>(params);
        boolean database = Alexis.getDatabaseManager() != null;

        User user = source.getChannelType().isGuild() ? ((MessageReceivedEvent)source).getAuthor() : source.getPrivateChannel().getUser();
        scriptParams.put("user.name", user.getName());
        scriptParams.put("user.mention", user.getAsMention());
        scriptParams.put("user.type", user.isBot() ? "bot" : "user");
        scriptParams.put("user.id", user.getId());
        scriptParams.put("user.avatar", user.getEffectiveAvatarUrl());
        scriptParams.put("user.discriminator", user.getDiscriminator());

        if (source.getChannelType().isGuild()) {
            MessageReceivedEvent mSource = (MessageReceivedEvent)source;
            Message message = mSource.getMessage();
            scriptParams.put("message.content", message.getContentRaw());
            scriptParams.put("message.id", message.getId());

            Member member = mSource.getMember();
            scriptParams.put("member.nickname", member.getEffectiveName());
            scriptParams.put("member.role", member.getRoles());

            var activity = member.getActivities();
            if (!activity.isEmpty())
                scriptParams.put("member.game", activity.get(0).getName());

            Guild guild = mSource.getGuild();
            scriptParams.put("guild.name", guild.getName());
            scriptParams.put("guild.id", guild.getId());
            scriptParams.put("guild.icon", guild.getIconUrl());

            if (database) {
                long userId = member.getUser().getIdLong();
                long guildId = member.getGuild().getIdLong();
                MemberData memberData = MemberData.query(userId, guildId);
                scriptParams.put("member.xp", memberData.getXp());
                scriptParams.put("member.level", RuneScape.parseXpAsLevel(memberData.getXp()));

                UserData userData = UserData.query(user.getIdLong());
                scriptParams.put("user.xp", userData.getXp());
                scriptParams.put("user.level", RuneScape.parseXpAsLevel(userData.getXp()));
                scriptParams.put("user.last_message", userData.getLastMessage().toString());

                GuildData guildData = GuildData.query(guild.getIdLong());
                scriptParams.put("guild.xp", guildData.getXp());
                scriptParams.put("guild.level", RuneScape.parseXpAsLevel(guildData.getXp()));
                scriptParams.put("guild.prefix", guildData.getSettings().getPrefix());
            }
        }

        if (params.containsKey("guild.prefix"))
            scriptParams.put("prefix", params.get("guild.prefix"));

        return scripts.get(new String[] { script, getLanguage(source) }, scriptParams);
    }

    @Override
    public String getLanguage(GenericMessageEvent source) {
        MessageChannelData data = MessageChannelData.query(source.getChannel().getIdLong());
        return data.getLanguage().toLowerCase();
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[0];
    }
}
