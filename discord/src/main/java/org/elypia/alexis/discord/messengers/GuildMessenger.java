/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.GuildImpl;
import org.elypia.alexis.persistence.entities.GuildData;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.utils.LevelUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = {Guild.class, GuildImpl.class})
public class GuildMessenger implements DiscordMessenger<Guild> {

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public GuildMessenger(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Guild output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Guild toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(toSend);
        builder.setThumbnail(toSend.getIconUrl());
        builder.setTitle(toSend.getName());

        Member owner = toSend.getOwner();
        String ownerValue = (owner != null) ? owner.getUser().getAsTag() : messages.deletedOrBanned();
        builder.addField(messages.guildOwner(), ownerValue, true);

        Collection<Member> members = toSend.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(messages.totalBotsAndUsers(), memberField, true);

        GuildData data = guildRepo.findBy(toSend.getIdLong());
        String description = data.getDescription();

        if (description != null)
            builder.setDescription(description);

        String guildPrefix = data.getPrefix();
        String prefix = (guildPrefix != null) ? guildPrefix : toSend.getSelfMember().getAsMention();
        builder.addField(messages.commandPrefix(), prefix, true);

        int level = LevelUtils.getLevelFromXp(data.getXp());
        builder.addField(messages.userLevel(), String.valueOf(level), true);

        Locale locale = data.getLocale();
        builder.addField(messages.currentLocale(), locale.getDisplayName(locale), false);

        builder.setFooter(messages.uniqueIdentifier(toSend.getId()), toSend.getIconUrl());
        return new MessageBuilder(builder.build()).build();
    }
}
