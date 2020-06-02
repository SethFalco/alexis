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

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.persistence.entities.AssignableRole;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;

import java.util.*;
import java.util.stream.Collectors;

@CommandController
@StandardCommand
public class RolesController implements Controller {

//    public String getSelfAssignableRoles(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
//        Guild guild = event.getMessage().getGuild();
//        List<AssignableRole> allowed = getAssignableRoles(guild);
//
//        if (allowed.isEmpty())
//            return "Sorry, there are no self-assignable roles in this guild.";
//
//        String string = asString(guild, allowed);
//        return String.format("** Self-Assignable Roles**\n%s", string);
//    }
//
//    public Object assign(
//        @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES, user = false) ActionEvent<Event, Message> event,
//        @Scoped(inGuild = Scope.LOCAL) Role[] roles
//    ) {
//        Message message = event.getMessage();
//        Guild guild = message.getGuild();
//        Member member = message.getMember();
//
//        Objects.requireNonNull(member, "Guild only event was somehow performed with a null member.");
//
//        Set<Role> membersRoles = new HashSet<>(member.getRoles());
//        List<AssignableRole> allowed = getAssignableRoles(guild);
//
//        if (hasDuplicates(roles))
//            return "It seems you accidently specified some roles twice, maybe you had a typo?";
//
//        List<Role> rolesToAdd = getRoles(allowed, roles, true);
//        List<Role> rolesDenied = getRoles(allowed, roles, false);
//
//        if (rolesToAdd.isEmpty())
//            return "Sorry, none of the roles you specified were actually self-assignable at all.";
//
//        membersRoles.addAll(rolesToAdd);
//        guild.modifyMemberRoles(member, membersRoles).complete();
//
//        String response = "I've succesfully added your roles for you.";
//
//        if (!rolesDenied.isEmpty())
//            response += "\n" + "The following roles weren't added: " + asString(rolesDenied);
//
//        return response;
//    }


//    public Object allow(
//        @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES) @Elevated ActionEvent<Event, Message> command,
//        @Scoped(inGuild = Scope.LOCAL) Role[] roles
//    ) {
//        GuildData data = GuildData.query(command.getSource().getGuild());
//        List<AssignableRole> allowed = getAssignableRoles(data);
//
//        if (hasDuplicates(roles))
//            return "It seems you accidently specified some roles twice, maybe you had a typo?";
//
//        List<Role> rolesAllowed = getRoles(allowed, roles, false);
//        List<Role> alreadyAllowed = getRoles(allowed, roles, true);
//
//        rolesAllowed.forEach((role) -> allowed.add(new AssignableRole(role.getIdLong())));
//
//        if (alreadyAllowed.size() == roles.length)
//            return scripts.get("roles.allow.already_allowed");
//
//        data.commit();
//
//        String response = scripts.get("roles.allow.success");
//
//        if (!alreadyAllowed.isEmpty())
//            response += "\n" + scripts.get("roles.allow.partial") + asString(alreadyAllowed);
//
//        return response;
//    }
//
//    @Command(id = "roles.deny", aliases = "deny", help = "roles.deny.h")
//    @Param(id = "common.roles", help = "roles.deny.roles.h")
//    public Object deny(@Channels(ChannelType.TEXT) @Elevated ActionEvent<Event, Message> command, @Search(Scope.LOCAL) Role[] roles) {
//        GuildData data = GuildData.query(command.getSource().getGuild());
//        List<AssignableRole> allowed = getAssignableRoles(data);
//
//        if (hasDuplicates(roles))
//            return scripts.get("roles.assign.duplicate");
//
//        List<Role> rolesToRemove = getRoles(allowed, roles, true);
//        List<Role> rolesCantRemove = getRoles(allowed, roles, false);
//
//        rolesToRemove.forEach((role) -> allowed.removeIf(o -> role.getIdLong() == o.getRoleId()));
//
//        if (rolesCantRemove.size() == roles.length)
//            return scripts.get("roles.deny.not_assignable");
//
//        data.commit();
//
//        String response = scripts.get("roles.deny.success");
//
//        if (!rolesCantRemove.isEmpty())
//            response += "\n" + scripts.get("roles.deny.partial") + asString(rolesCantRemove);
//
//        return response;
//    }
//
//    private List<AssignableRole> getAssignableRoles(Guild guild) {
//        return GuildData.query(guild).getSettings().getAssignableRoles();
//    }
//
//    /**
//     * Get a list of assignable roles in a guild from the GuildData.
//     *
//     * @param data The GuildData entity which represents a guild as far
//     * as the bot is concerned int the database.
//     * @return A list of any self-assignable roles this guild may have configured.
//     */
//    private List<AssignableRole> getAssignableRoles(GuildData data) {
//        return data.getSettings().getAssignableRoles();
//    }

    /**
     * Check if any of the roles specified in the command
     * are a duplicate. This is likely a mistake and so should
     * be deemed invalid.
     *
     * @param roles The roles specified by the user.
     * @return If this array of roles contains are duplicates.
     */
    private boolean hasDuplicates(Role[] roles) {
        return Arrays.stream(roles).distinct().count() != roles.length;
    }

    /**
     * Return a list of roles from the list of requested roles
     * based split into either only allowed, or only denied.
     *
     * @param assignableRoles All assignable roles in the guild.
     * @param requestedRoles All request roles in this request.
     * @param allowed If we want allowed roles, or denies roles.
     * @return A list of Discord Role entities.
     */
    private List<Role> getRoles(List<AssignableRole> assignableRoles, Role[] requestedRoles, boolean allowed) {
        List<Role> roles = new ArrayList<>();

        for (Role role : requestedRoles) {
            boolean allow = assignableRoles.stream().anyMatch((assignableRole) -> {
                return assignableRole.getRoleId() == role.getIdLong();
            });

            if (allow == allowed)
                roles.add(role);
        }

        return roles;
    }

    private String asString(Guild guild, Collection<AssignableRole> roles) {
        return asString(roles.stream().map((role) -> guild.getRoleById(role.getRoleId()))
            .collect(Collectors.toList()));
    }

    private String asString(List<Role> roles) {
        Collections.sort(roles);
        StringJoiner joiner = new StringJoiner("\n");

        for (Role role : roles)
            joiner.add("`" + role.getName() + "`");

        return joiner.toString();
    }
}
