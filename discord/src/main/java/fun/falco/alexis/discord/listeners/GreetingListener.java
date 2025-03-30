/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.listeners;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.persistence.entities.FeatureSettings;
import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.entities.GuildMessage;
import fun.falco.alexis.core.persistence.entities.RoleData;
import fun.falco.alexis.core.persistence.enums.Feature;
import fun.falco.alexis.core.persistence.enums.GuildMessageType;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import fun.falco.alexis.discord.modules.GreetingController;
import org.elypia.comcord.ActivatedListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

/**
 * This controller exclusively handles the greeting logic to welcome or say
 * farewell to users and bots.
 *
 * This uses additional configurations to produce the welcome message.
 *
 * Can be configured from the {@link GreetingController}.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Singleton
public class GreetingListener extends ActivatedListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GreetingListener.class);

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public GreetingListener(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = Objects.requireNonNull(guildRepo);
        this.messages = messages;
    }

    /**
     * Send a welcome message if the guild is configured to send one,
     * and add all join roles the user may be entitled to.
     *
     * @param event Event that represents a new member joining a guild.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();

        if (user == event.getJDA().getSelfUser()) {
            return;
        }

        Guild guild = event.getGuild();
        GuildData data = guildRepo.findBy(guild.getIdLong());

        if (data == null) {
            return;
        }

        Stream<RoleData> roleDatas = data.getRoles().stream()
            .filter(RoleData::isSelfAssignable)
            .filter((roleData) -> guild.getRoleById(roleData.getId()) != null);

        if (user.isBot()) {
            onGuildMemberEvent(event, Feature.BOT_JOIN_MESSAGE, GuildMessageType.BOT_WELCOME);
            roleDatas = roleDatas.filter(RoleData::isOnBotJoin);
        } else {
            onGuildMemberEvent(event, Feature.USER_JOIN_MESSAGE, GuildMessageType.USER_WELCOME);
            roleDatas = roleDatas.filter(RoleData::isOnUserJoin);
        }

        List<Role> roles = roleDatas.map((roleData) -> guild.getRoleById(roleData.getId()))
            .collect(Collectors.toList());

        if (!roles.isEmpty()) {
            if (guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                guild.modifyMemberRoles(event.getMember(), roles, null).queue();
            } else {
                logger.debug("Attempted to add on-join roles to member but lacking the {} permission.", Permission.MANAGE_ROLES);
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        User user = event.getUser();

        if (user == event.getJDA().getSelfUser()) {
            return;
        }

        if (user.isBot()) {
            onGuildMemberEvent(event, Feature.BOT_LEAVE_MESSAGE, GuildMessageType.BOT_LEAVE);
        } else {
            onGuildMemberEvent(event, Feature.USER_LEAVE_MESSAGE, GuildMessageType.USER_LEAVE);
        }
    }

    public void onGuildMemberEvent(GenericGuildEvent event, Feature feature, GuildMessageType type) {
        Guild guild = event.getGuild();
        GuildData data = guildRepo.findBy(guild.getIdLong());
        FeatureSettings guildFeature = data.getFeatures().get(feature);

        if (guildFeature == null || !guildFeature.isEnabled()) {
            return;
        }

        GuildMessage guildMessage = data.getMessages().get(type);

        if (guildMessage == null) {
            logger.warn("Greeting event occured in guild where feature was enabled, but message isn't defined.");
            return;
        }

        Long channelId = guildMessage.getChannelId();
        TextChannel channel = guild.getTextChannelById(channelId);

        if (channel == null) {
            logger.warn("Greeting event occured in guild where message is defined, but the channel to no longer exists.");
            return;
        }

        String content = guildMessage.getMessage();

        if (content == null) {
            switch (type) {
                case USER_WELCOME:
                    content = messages.defaultUserJoinMessage();
                    break;
                case BOT_WELCOME:
                    content = messages.defaultBotJoinMessage();
                    break;
                case USER_LEAVE:
                    content = messages.defaultUserLeaveMessage();
                    break;
                case BOT_LEAVE:
                    content = messages.defaultBotLeaveMessage();
                    break;
                default:
                    throw new IllegalStateException("Message type received as parameter is not a greeting type.");
            }
        }

        channel.sendMessage(content).queue();
    }
}
