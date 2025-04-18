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

package fun.falco.alexis.core.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Entity
@Table(name = "emote")
public class EmoteData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "emote_id")
    private long id;

    /** The guild that <strong>owns</strong> this emote. */
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildData guildData;

    @OneToMany(targetEntity = EmoteUsage.class, mappedBy = "emoteData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmoteUsage> usages;

    public EmoteData() {
        // Do nothing
    }

    public EmoteData(final long id, final GuildData guildData) {
        this.id = id;
        this.guildData = guildData;
        usages = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildId) {
        this.guildData = guildId;
    }

    public List<EmoteUsage> getUsages() {
        return usages;
    }

    public void setUsages(List<EmoteUsage> usages) {
        this.usages = usages;
    }
}
