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

package fun.falco.alexis.discord.modules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import fun.falco.alexis.core.i18n.AlexisMessages;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class VoiceController {

    private final AlexisMessages messages;

    @Inject
    public VoiceController(AlexisMessages messages) {
        this.messages = messages;
    }

    @StandardCommand
    public String mentionMembers(@Channels(ChannelType.TEXT) User author, @Param VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream()
            .map(Member::getUser)
            .filter(Predicate.not(User::isBot))
            .collect(Collectors.toSet());

        if (users.remove(author) && users.isEmpty())
            return messages.voiceNoOneElseInChannel();

        if (users.isEmpty())
            return messages.voiceNoOneInChannel();

        return users.stream()
            .map(User::getAsMention)
            .collect(Collectors.joining());
    }
}
