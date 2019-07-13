package com.elypia.alexis.listeners;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.GuildData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GreetingsListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        onGuildMemberEvent(event, true);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        onGuildMemberEvent(event, false);
    }

    public void onGuildMemberEvent(GenericGuildMemberEvent event, boolean join) {
        boolean bot = event.getUser().isBot();

        Guild guild = event.getGuild();
        GuildData data = Alexis.getDatabaseManager().query(GuildData.class, "guild_id", guild.getIdLong());

        GreetingSettings greetings = data.getSettings().getGreetingSettings();
        GreetingSetting greeting = join ? greetings.getJoin() : greetings.getLeave();
        MessageSettings settings = bot ? greeting.getBot() : greeting.getUser();

        if (settings.isEnabled()) {
            JDA jda = event.getJDA();
            TextChannel channel = jda.getTextChannelById(settings.getChannel());
            String message = settings.getMessage();
//			message = BotUtils.get(event, message, event);

            channel.sendMessage(message).queue();
        }
    }
}
