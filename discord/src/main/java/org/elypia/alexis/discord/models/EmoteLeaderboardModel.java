package org.elypia.alexis.discord.models;

import java.util.List;

/**
 * Model object that represents a single entry in the {@link EmoteLeaderboardModel}.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
public class EmoteLeaderboardModel {

    private List<EmoteLeaderboardEntryModel> entries;

    public EmoteLeaderboardModel() {
        // Do nothing.
    }

    public EmoteLeaderboardModel(List<EmoteLeaderboardEntryModel> entries) {
        this.entries = entries;
    }

    public List<EmoteLeaderboardEntryModel> getEntries() {
        return entries;
    }

    public void setEntries(List<EmoteLeaderboardEntryModel> entries) {
        this.entries = entries;
    }
}
