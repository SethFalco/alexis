package com.elypia.alexis.handlers.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.AssignableRole;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Channel;
import com.elypia.commandler.jda.annotations.validation.command.*;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

import java.util.*;
import java.util.stream.Collectors;

@Channel(ChannelType.TEXT)
@Module(name = "roles", group = "group.settings", aliases = {"role", "roles"}, help = "roles.h")
public class RolesHandler extends JDAHandler {

    @Command(name = "common.list", aliases = {"list", "show"}, help = "roles.list.h")
    public Object list(JDACommand command) {
        Guild guild = command.getSource().getGuild();
        List<AssignableRole> allowed = getAssignableRoles(guild);

        if (allowed.isEmpty())
            return command.getScript("roles.list.no_roles");

        String script = command.getScript("roles.list.title");
        String string = asString(guild, allowed);

        return String.format("** %s**\n%s", script, string);
    }

    @Static
    @Permissions(value = Permission.MANAGE_ROLES, userRequiresPermission = false)
    @Command(name = "roles.assign", aliases = {"iam", "assign"}, help = "roles.assign.h")
    @Param(name = "common.roles", help = "roles.assign.roles.h")
    public Object assign(JDACommand command, @Search(Scope.LOCAL) Role[] roles) {
        Guild guild = command.getSource().getGuild();
        List<AssignableRole> allowed = getAssignableRoles(guild);

        if (hasDuplicates(roles))
            return command.getScript("roles.assign.duplicates");

        List<Role> rolesToAdd = getRoles(allowed, roles, true);
        List<Role> rolesDenied = getRoles(allowed, roles, false);

        if (rolesToAdd.isEmpty())
            return command.getScript("roles.assign.denied");

        Member member = command.getMessage().getMember();
        guild.getController().addRolesToMember(member, rolesToAdd).queue(o -> {
            String response = command.getScript("roles.assign.success");

            if (!rolesDenied.isEmpty())
                response += "\n" + command.getScript("roles.assign.partial") + asString(rolesDenied);

            command.reply(response);
        });

        return null;
    }

    @Elevated
    @Permissions(value = Permission.MANAGE_ROLES)
    @Command(name = "roles.allow", aliases = "allow", help = "roles.allow.h")
    @Param(name = "common.roles", help = "roles.allow.roles.h")
    public Object allow(JDACommand command, @Search(Scope.LOCAL) Role[] roles) {
        GuildData data = GuildData.query(command.getSource().getGuild());
        List<AssignableRole> allowed = getAssignableRoles(data);

        if (hasDuplicates(roles))
            return command.getScript("roles.assign.duplicate");

        List<Role> rolesAllowed = getRoles(allowed, roles, false);
        List<Role> alreadyAllowed = getRoles(allowed, roles, true);

        rolesAllowed.forEach((role) -> allowed.add(new AssignableRole(role.getIdLong())));

        if (alreadyAllowed.size() == roles.length)
            return command.getScript("roles.allow.already_allowed");

        data.commit();

        String response = command.getScript("roles.allow.success");

        if (!alreadyAllowed.isEmpty())
            response += "\n" + command.getScript("roles.allow.partial") + asString(alreadyAllowed);

        return response;
    }

    @Elevated
    @Command(name = "roles.deny", aliases = "deny", help = "roles.deny.h")
    @Param(name = "common.roles", help = "roles.deny.roles.h")
    public Object deny(JDACommand command, @Search(Scope.LOCAL) Role[] roles) {
        GuildData data = GuildData.query(command.getSource().getGuild());
        List<AssignableRole> allowed = getAssignableRoles(data);

        if (hasDuplicates(roles))
            return command.getScript("roles.assign.duplicate");

        List<Role> rolesToRemove = getRoles(allowed, roles, true);
        List<Role> rolesCantRemove = getRoles(allowed, roles, false);

        rolesToRemove.forEach((role) -> allowed.removeIf(o -> role.getIdLong() == o.getRoleId()));

        if (rolesCantRemove.size() == roles.length)
            return command.getScript("roles.deny.not_assignable");

        data.commit();

        String response = command.getScript("roles.deny.success");

        if (!rolesCantRemove.isEmpty())
            response += "\n" + command.getScript("roles.deny.partial") + asString(rolesCantRemove);

        return response;
    }

    private List<AssignableRole> getAssignableRoles(Guild guild) {
        return GuildData.query(guild).getSettings().getAssignableRoles();
    }

    /**
     * Get a list of assignable roles in a guild from the GuildData.
     *
     * @param data The GuildData entity which represents a guild as far
     * as the bot is concerned int the database.
     * @return A list of any self-assignable roles this guild may have configured.
     */
    private List<AssignableRole> getAssignableRoles(GuildData data) {
        return data.getSettings().getAssignableRoles();
    }

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
        return asString(roles.stream().map((role) -> {
            return guild.getRoleById(role.getRoleId());
        }).collect(Collectors.toList()));
    }

    private String asString(List<Role> roles) {
        Collections.sort(roles);
        StringJoiner joiner = new StringJoiner("\n");

        for (Role role : roles)
            joiner.add("`" + role.getName() + "`");

        return joiner.toString();
    }
}
