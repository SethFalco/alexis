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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.elypia.commandler.event.Action;

/**
 * Wraps around {@link Action}
 * in order to persist that data into a database.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Entity
@Table(
    name = "custom_command",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guild_id", "input"})
    }
)
public class CustomCommand implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "command_id")
    protected int id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildData guildData;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "output", nullable = false)
    private String output;

    public CustomCommand() {
        // Do nothing
    }

    public CustomCommand(int id) {
        this.id = id;
    }

    public CustomCommand(int id, GuildData guildData, String input, String output) {
        this(id);
        this.guildData = guildData;
        this.input = input;
        this.output = output;
    }

    public int getId() {
        return id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
