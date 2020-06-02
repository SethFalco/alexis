package org.elypia.alexis.discord.models;

import net.dv8tion.jda.api.entities.Emote;

public class EmoteLeaderboardEntryModel implements Comparable<EmoteLeaderboardEntryModel> {

    private Emote emote;
    private int localeUsage;
    private int globalUsage;

    public EmoteLeaderboardEntryModel() {
        // Do nothing.
    }

    public EmoteLeaderboardEntryModel(Emote emote, int localUsage, int globalUsage) {
        this.emote = emote;
        this.localeUsage = localUsage;
        this.globalUsage = globalUsage;
    }

    public Emote getEmote() {
        return emote;
    }

    public void setEmote(Emote emote) {
        this.emote = emote;
    }

    public int getLocaleUsage() {
        return localeUsage;
    }

    public void setLocaleUsage(int localeUsage) {
        this.localeUsage = localeUsage;
    }

    public int getGlobalUsage() {
        return globalUsage;
    }

    public void setGlobalUsage(int globalUsage) {
        this.globalUsage = globalUsage;
    }

    @Override
    public int compareTo(EmoteLeaderboardEntryModel o) {
        int local = Integer.compare(o.localeUsage, localeUsage);

        if (local != 0)
            return local;

        return Integer.compare(o.globalUsage, globalUsage);
    }
}
