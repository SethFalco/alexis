package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.AssignableRole;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.*;
import java.util.stream.Collectors;

@Module(id = "Roles", group = "Settings", aliases = {"role", "roles"}, help = "roles.h")
public class RolesModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public RolesModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "common.list", aliases = {"list", "show"}, help = "roles.list.h")
    public Object list(@Channels(ChannelType.TEXT) JDACEvent event) {
        Guild guild = event.getSource().getGuild();
        List<AssignableRole> allowed = getAssignableRoles(guild);

        if (allowed.isEmpty())
            return scripts.get("roles.list.no_roles");

        String script = scripts.get("roles.list.title");
        String string = asString(guild, allowed);

        return String.format("** %s**\n%s", script, string);
    }

    @Static
    @Command(id = "roles.assign", aliases = {"iam", "assign"}, help = "roles.assign.h")
    @Param(id = "common.roles", help = "roles.assign.roles.h")
    public Object assign(
            @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES, user = false) JDACEvent command,
            @Search(Scope.LOCAL) Role[] roles
    ) {
        Guild guild = command.getSource().getGuild();
        List<AssignableRole> allowed = getAssignableRoles(guild);

        if (hasDuplicates(roles))
            return scripts.get("roles.assign.duplicates");

        List<Role> rolesToAdd = getRoles(allowed, roles, true);
        List<Role> rolesDenied = getRoles(allowed, roles, false);

        if (rolesToAdd.isEmpty())
            return scripts.get("roles.assign.denied");

        Member member = command.asMessageRecieved().getMember();
        guild.getController().addRolesToMember(member, rolesToAdd).queue(o -> {
            String response = scripts.get("roles.assign.success");

            if (!rolesDenied.isEmpty())
                response += "\n" + scripts.get("roles.assign.partial") + asString(rolesDenied);

            command.send(response);
        });

        return null;
    }


    @Command(id = "roles.allow", aliases = "allow", help = "roles.allow.h")
    @Param(id = "common.roles", help = "roles.allow.roles.h")
    public Object allow(
        @Channels(ChannelType.TEXT) @Permissions(value = Permission.MANAGE_ROLES) @Elevated JDACEvent command,
        @Search(Scope.LOCAL) Role[] roles
    ) {
        GuildData data = GuildData.query(command.getSource().getGuild());
        List<AssignableRole> allowed = getAssignableRoles(data);

        if (hasDuplicates(roles))
            return scripts.get("roles.assign.duplicate");

        List<Role> rolesAllowed = getRoles(allowed, roles, false);
        List<Role> alreadyAllowed = getRoles(allowed, roles, true);

        rolesAllowed.forEach((role) -> allowed.add(new AssignableRole(role.getIdLong())));

        if (alreadyAllowed.size() == roles.length)
            return scripts.get("roles.allow.already_allowed");

        data.commit();

        String response = scripts.get("roles.allow.success");

        if (!alreadyAllowed.isEmpty())
            response += "\n" + scripts.get("roles.allow.partial") + asString(alreadyAllowed);

        return response;
    }

    @Command(id = "roles.deny", aliases = "deny", help = "roles.deny.h")
    @Param(id = "common.roles", help = "roles.deny.roles.h")
    public Object deny(@Channels(ChannelType.TEXT) @Elevated JDACEvent command, @Search(Scope.LOCAL) Role[] roles) {
        GuildData data = GuildData.query(command.getSource().getGuild());
        List<AssignableRole> allowed = getAssignableRoles(data);

        if (hasDuplicates(roles))
            return scripts.get("roles.assign.duplicate");

        List<Role> rolesToRemove = getRoles(allowed, roles, true);
        List<Role> rolesCantRemove = getRoles(allowed, roles, false);

        rolesToRemove.forEach((role) -> allowed.removeIf(o -> role.getIdLong() == o.getRoleId()));

        if (rolesCantRemove.size() == roles.length)
            return scripts.get("roles.deny.not_assignable");

        data.commit();

        String response = scripts.get("roles.deny.success");

        if (!rolesCantRemove.isEmpty())
            response += "\n" + scripts.get("roles.deny.partial") + asString(rolesCantRemove);

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
