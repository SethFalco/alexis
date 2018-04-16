package com.elypia.commandler.parsing.parsers.jda;

import com.elypia.commandler.parsing.impl.Parser;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;

public class RoleParser implements Parser<Role> {

    @Override
    public Role parse(String input) throws IllegalArgumentException {
        Collection<Role> roles = event.getGuild().getRoles();

        for (Role role : roles) {
            if (role.getId().equals(input) || role.getAsMention().equals(input) || role.getName().equalsIgnoreCase(input))
                return role;
        }

        throw new IllegalArgumentException("Parameter `" + input + "` could not be be linked to a role.");
    }
}
