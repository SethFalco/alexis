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

import org.elypia.commandler.annotation.stereotypes.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.elypia.elypiai.osu.data.OsuMode;

import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ParamAdapter(OsuMode.class)
public class OsuModeAdapter implements Adapter<OsuMode> {

    @Override
    public OsuMode adapt(String input, Class<? extends OsuMode> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);

        switch (input.toLowerCase()) {
            case "osu":
            case "osu!":
            case "0":
            case "zero":
                return OsuMode.OSU;
            case "taiko":
            case "1":
            case "one":
                return OsuMode.TAIKO;
            case "catch the beat":
            case "catch_the_beat":
            case "catch":
            case "ctb":
            case "2":
            case "two":
                return OsuMode.CATCH_THE_BEAT;
            case "osu mania":
            case "mania":
            case "piano":
            case "keys":
            case "3":
            case "three":
                return OsuMode.MANIA;
            default:
                return null;
        }
    }
}
