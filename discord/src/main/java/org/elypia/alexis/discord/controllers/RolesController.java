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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Roles controller for setting up self-assignable roles
 * so users can choose to pick one roles of their choice
 * if configured by the guild.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class RolesController {

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public RolesController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    /**
     * Return a list of roles that are assignable in the current guild,
     * and any requirements needed to self-assign them.
     *
     * @param message The mesage that trigged this event.
     * @return The message to send in chat.
     */
    @StandardCommand
    public String getSelfAssignableRoles(@Channels(ChannelType.TEXT) Message message) {
        Guild guild = message.getGuild();
        GuildData guildData = guildRepo.findBy(guild.getIdLong());

        if (guildData == null)
            return messages.assignableRolesNoData();

        List<RoleData> selfAssignableRoles = guildData.getRoles().stream()
            .filter(RoleData::isSelfAssignable)
            .filter((role) -> guild.getRoleById(role.getId()) != null)
            .collect(Collectors.toList());

        if (selfAssignableRoles.isEmpty())
            return messages.assignableRolesNoRolesSet();

        StringJoiner joiner = new StringJoiner("\n");

        for (RoleData role : selfAssignableRoles)
            joiner.add("`" + guild.getRoleById(role.getId()).getName() + "`");

        return messages.assignableRolesList(joiner.toString());
    }

    @StandardCommand
    public String assignSelfAssignableRole(
        @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES, userNeedsPermission = false) Message message,
        @Param Role[] roles
    ) {
        Guild guild = message.getGuild();
        GuildData guildData = guildRepo.findBy(guild.getIdLong());

        if (guildData == null)
            return messages.assignableRolesNoData();

        Member member = message.getMember();

        Objects.requireNonNull(member, "Guild only event was somehow performed with a null member.");

        List<RoleData> selfAssignableRoles = guildData.getRoles().stream()
            .filter(RoleData::isSelfAssignable)
            .filter((role) -> guild.getRoleById(role.getId()) != null)
            .collect(Collectors.toList());

        List<Role> rolesList = new ArrayList<>(List.of(roles));
        List<Role> distinct = popDistinct(rolesList);

        List<Role> rolesToAdd = new ArrayList<>();
        List<Role> rolesDenied = new ArrayList<>();

        for (Role role : distinct) {
            Optional<RoleData> optRoleData = selfAssignableRoles.stream()
                .filter((rd) -> rd.getId() == role.getIdLong())
                .findAny();

            if (optRoleData.isEmpty())
                rolesDenied.add(role);
            else {
                RoleData roleData = optRoleData.get();

                if (roleData.isSelfAssignable())
                    rolesToAdd.add(role);
                else
                    rolesDenied.add(role);
            }
        }

        List<Role> alreadyOwned = new ArrayList<>();

        for (int i = rolesToAdd.size() - 1; i >= 0; i--) {
            if (member.getRoles().contains(rolesToAdd.get(i)))
                alreadyOwned.add(rolesToAdd.remove(i));
        }

        guild.modifyMemberRoles(member, rolesToAdd, null).queue();

        StringJoiner joiner = new StringJoiner("\n\n");

        if (!rolesToAdd.isEmpty())
            joiner.add(messages.assignableRolesUserAssignedRole(toRoleString(rolesToAdd)));

        if (!rolesDenied.isEmpty())
            joiner.add(messages.assignableRolesUserDeniedRole(toRoleString(rolesDenied)));

        if (!alreadyOwned.isEmpty())
            joiner.add(messages.assignableRolesUserAlreadyHadRole(toRoleString(alreadyOwned)));

        if (!rolesList.isEmpty())
            joiner.add(messages.assignableRolesIgnoredDuplicates(toRoleString(rolesList)));

        return joiner.toString();
    }

    /**
     * Add roles that can be marked as assignable.
     *
     * @param message The mesage that trigged this event.
     * @param roles The roles that should be marked as allowed for self-assignment.
     * @return The message to send in chat.
     */
    @StandardCommand
    public String addAssignableRoles(
        @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES) @Elevated Message message,
        @Param Role[] roles
    ) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findOptionalBy(guildId).orElse(new GuildData(guildId));
        List<RoleData> roleDatas = guildData.getRoles();
        List<Role> rolesList = new ArrayList<>(List.of(roles));
        List<Role> distinct = popDistinct(rolesList);

        List<Role> rolesToAllow = new ArrayList<>();
        List<Role> alreadyAllowed = new ArrayList<>();

        for (Role role : distinct) {
            Optional<RoleData> optRoleData = roleDatas.stream()
                .filter((rd) -> rd.getId() == role.getIdLong())
                .findAny();

            if (optRoleData.isPresent()) {
                RoleData roleData = optRoleData.get();

                if (roleData.isSelfAssignable())
                    alreadyAllowed.add(role);
                else {
                    roleData.setSelfAssignable(true);
                    rolesToAllow.add(role);
                }
            } else {
                roleDatas.add(new RoleData(role.getIdLong(), guildData, true));
                rolesToAllow.add(role);
            }
        }

        StringJoiner joiner = new StringJoiner("\n\n");

        if (!rolesToAllow.isEmpty())
            joiner.add(messages.assignableRolesNowAllowed(toRoleString(rolesToAllow)));

        if (!alreadyAllowed.isEmpty())
            joiner.add(messages.assignableRolesAlreadyAllowed(toRoleString(alreadyAllowed)));

        if (!rolesList.isEmpty())
            joiner.add(messages.assignableRolesIgnoredDuplicates(toRoleString(rolesList)));

        guildRepo.save(guildData);
        return joiner.toString();
    }

    /**
     * Disable any roles that have been set to be auto-assigned.
     *
     * @param message The mesage that trigged this event.
     * @param roles The roles that should no longer be marked as self-assignable.
     * @return The message to send in chat.
     */
    @StandardCommand
    public String denyAssignableRoles(@Channels(ChannelType.TEXT) @Elevated Message message, @Param Role[] roles) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);

        if (guildData == null)
            return messages.assignableRolesNoData();

        List<RoleData> roleDatas = guildData.getRoles();
        List<Role> rolesList = new ArrayList<>(List.of(roles));
        List<Role> distinct = popDistinct(rolesList);

        List<Role> rolesToRemove = new ArrayList<>();
        List<Role> redundantRoles = new ArrayList<>();

        for (Role role : distinct) {
            Optional<RoleData> optRoleData = roleDatas.stream()
                .filter((rd) -> rd.getId() == role.getIdLong())
                .findAny();

            if (optRoleData.isEmpty())
                redundantRoles.add(role);
            else {
                RoleData roleData = optRoleData.get();

                if (roleData.isSelfAssignable()) {
                    roleData.setSelfAssignable(false);
                    rolesToRemove.add(role);
                } else {
                    redundantRoles.add(role);
                }
            }
        }

        StringJoiner joiner = new StringJoiner("\n\n");

        if (!rolesToRemove.isEmpty())
            joiner.add(messages.assignableRolesRemoved(toRoleString(rolesToRemove)));

        if (!redundantRoles.isEmpty())
            joiner.add(messages.assignableRolesDidntExist(toRoleString(redundantRoles)));

        if (!rolesList.isEmpty())
            joiner.add(messages.assignableRolesIgnoredDuplicates(toRoleString(rolesList)));

        guildRepo.save(guildData);
        return joiner.toString();
    }

    /**
     * Remove all distinct values from the provided list
     * leaving the list with only duplicate values while returning
     * the distinct values.
     *
     * @param items A list of items to pull distinct values from.
     * @return A list of all distinct values in the collection,
     * leaving the collection with only duplicates.
     */
    private <T> List<T> popDistinct(final List<T> items) {
        List<T> distinct = new ArrayList<>();

        for (int i = items.size() - 1; i >= 0; i--) {
            if (!distinct.contains(items.get(i)))
                distinct.add(items.remove(i));
        }

        return distinct;
    }

    /**
     * Convert a list of Roles into an appropriate {@link String} that
     * can be represented as a comma seperated list.
     *
     * @param roles The roles to join together.
     * @return A single string representing a comma delimetered list of role names.
     */
    private String toRoleString(final Collection<Role> roles) {
        return roles.stream()
            .map((role) -> MarkdownUtil.monospace(role.getName()))
            .collect(Collectors.joining(", "));
    }
}
