/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.adapters;

import org.elypia.commandler.adapters.EnumAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.elypia.elypiai.osu.data.OsuMode;

import javax.inject.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class OsuModeAdapter implements Adapter<OsuMode> {

    private final EnumAdapter enumAdapter;

    @Inject
    public OsuModeAdapter(final EnumAdapter enumAdapter) {
        this.enumAdapter = enumAdapter;
    }

    @Override
    public OsuMode adapt(String input, Class<? extends OsuMode> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Enum e = enumAdapter.adapt(input, type, metaParam, event);

        if (e != null)
            return (OsuMode)e;

        for (OsuMode mode : OsuMode.values()) {
            if (String.valueOf(mode.getId()).equals(input))
                return mode;
        }

        return null;
    }
}
