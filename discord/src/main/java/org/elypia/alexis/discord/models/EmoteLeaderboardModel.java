package org.elypia.alexis.discord.models;

import java.util.List;

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
