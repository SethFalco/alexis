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

package org.elypia.alexis.adapters;

import org.elypia.commandler.adapters.EnumAdapter;
import org.elypia.commandler.annotation.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.elypia.elypiai.osu.data.OsuMode;

import javax.inject.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
@ParamAdapter(OsuMode.class)
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
