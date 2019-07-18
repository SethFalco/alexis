package com.elypia.alexis.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.TranslateSettings;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import com.google.cloud.translate.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.Map;

@Module(id = "Translate", group = "Settings", aliases = {"translate", "trans", "tran"}, help = "translate.h")
public class TranslateModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public TranslateModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "translate.toggle", aliases = "toggle", help = "translate.toggle.h")
    @Param(id = "common.toggle", help = "translate.toggle.p.toggle.h")
    public String toggle(@Channels(ChannelType.TEXT) @Elevated JDACEvent event, boolean toggle) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isEnabled() == toggle)
            return scripts.get("translate.toggle.no_change", Map.of("enabled", toggle));

        settings.setEnabled(toggle);
        data.commit();

        return scripts.get("translate.toggle.response", Map.of("enabled", toggle));
    }

    @Command(id = "translate.dm", aliases = "private", help = "translate.dm.h")
    @Param(id = "common.toggle", help = "translate.dm.p.toggle.h")
    public String inPrivate(@Channels(ChannelType.TEXT) @Elevated JDACEvent event, boolean isPrivate) {
        Guild guild = event.getSource().getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        TranslateSettings settings = data.getSettings().getTranslateSettings();

        if (settings.isPrivate() == isPrivate)
            return scripts.get("translate.dm.no_change", Map.of("enabled", isPrivate));

        settings.setPrivate(isPrivate);
        data.commit();

        return scripts.get("translate.dm.response", Map.of("enabled", isPrivate));
    }

    /**
     * If the translate feature is enabled for the Guild we should check
     * reactions for if they're flags and if so, respond to the message
     * by translating the message to an appropriate languages the flag
     * may represent. <br>
     * <br>
     * <strong>Note:</strong> We pass the settings over so we don't have
     * to query them again.
     *
     * @param event The source event which may have request translating.
     * @param settings The guild settings regarding translation.
     */
    @Static
    @React()
    private void translate(MessageReactionAddEvent event, TranslateSettings settings) {
        if (!settings.isEnabled())
            return;

        String code = event.getReactionEmote().getName();
        var languages = translate.getSupportedLangauges();

        for (var entry : languages.entrySet()) {
            Country[] countries = entry.getKey().getCountries();
            Language value = entry.getValue();

            for (Country country : countries) {
                if (country.getUnicodeEmote().equals(code)) {
                    event.getChannel().getMessageById(event.getMessageIdLong()).queue(message -> {
                        String content = message.getContentStripped();
                        Translation translation = translate.translate(content, value);
                        var source = translate.getLanguage(translation.getSourceLanguage()).getKey();

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.addField("Source (" + source.getName() + ")", content + "\n_ _", false);
                        builder.addField("Target (" + value.getName() + ")", translation.getTranslatedText(), false);
                        builder.setImage("https://elypia.com/resources/google.png");
                        builder.setFooter("http://translate.google.com/", null);

                        if (event.getGuild() != null)
                            builder.setColor(event.getGuild().getSelfMember().getColor());

                        if (!settings.isPrivate())
                            event.getChannel().sendMessage(builder.build()).queue();
                        else {
                            event.getMember().getUser().openPrivateChannel().queue(o -> {
                                o.sendMessage(builder.build()).queue();
                            });
                        }
                    });

                    return;
                }
            }
        }
    }
}
