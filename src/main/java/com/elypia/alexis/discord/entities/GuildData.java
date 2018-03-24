package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Tag;
import com.elypia.alexis.discord.entities.impl.DatabaseEntity;
import com.elypia.alexis.discord.events.impl.GenericEvent;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bson.Document;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class GuildData extends DatabaseEntity {

    /**
     * Hold reference to self to pass as parameter in
     * anonymous methods.
     */

    private final GuildData guildData;

    /**
     * The long ID of the guild this instance holds data for.
     */

    private long guildId;

    /**
     * The total amount of XP all members together have earned
     * while in this guild.
     */

    private int xp;

    /**
     * Data on each text channel in the guild, such as tags.
     */

    private Map<Long, TextChannelData> textChannelData;

    private Map<Tag, Boolean> tagData;

    public GuildData(MongoDatabase database, Guild guild) {
        super(database, "guilds");
        guildData = this;
        guildId = guild.getIdLong();
        textChannelData = new HashMap<>();
        tagData = new HashMap<>();

        Document data = getById("guild_id", guildId);

        if (data == null)
            return;

        xp = data.getInteger("xp");

        getArray(data, "text_channels", o -> {
            TextChannelData t = new TextChannelData(guildData, (Document)o);
            textChannelData.put(t.getTextChannelId(), t);
        });

        getArray(data, "tags", o -> {
            Document d = (Document)o;
            tagData.put(Tag.getByName(d.getString("tag")), d.getBoolean("enabled"));
        });
    }

    @Override
    public void commit() {
        Document data = new Document();
        data.put("xp", xp);

        collection.updateOne(eq("guild_id", guildId), data, options);
    }

    public long getGuildId() {
        return guildId;
    }

    /**
     * This does <strong>NOT</strong> check if the channel has the tag.
     * This checks if the channel has the tag functionality. <br>
     * If the guild has this tag set for whitelist, will return true if
     * the channel has this tag; if the tag is set for blacklist it will
     * only return true if the channel doesn't have this tag.
     *
     * @param channel The channel we wish to check.
     * @param tag What functionality we're checking for.
     * @return If the channel has the functionality enabled.
     */

    public boolean channelHasTag(TextChannel channel, Tag tag) {
        return channelHasTag(channel.getIdLong(), tag);
    }

    public boolean channelHasTag(TextChannelData channel, Tag tag) {
        return channelHasTag(channel.getTextChannelId(), tag);
    }

    public boolean channelHasTag(long id, Tag tag) {
        if (textChannelData.containsKey(id)) {
            TextChannelData txt = textChannelData.get(id);
        }

        return false;
    }

    /**
     * @return The total amount of XP earned in this guild.
     */

    public int getXp() {
        return xp;
    }

    /**
     * Increase the total XP this guild has.
     * Less validation checks are required for guild vs global.
     *
     * @param event The generic event this is due rewarding XP.
     * @return The new total XP the guild has.
     */

    public int gainXp(GenericEvent event) {
        String content = event.getContent();
        int gains = content.split("\\s+").length;

        xp += gains;
        return xp;
    }

    /**
     * @param channel A text channel which is under this guild.
     * @return Any data Alexis is storing for that channel such as tags.
     */

    public TextChannelData getTextChannelData(TextChannel channel) {
        long textChannelId = channel.getIdLong();
        return textChannelData.get(textChannelId);
    }

    /**
     * A list of all tags the guild has set, this list may
     * omit tags the guild has never interacted with or enabled.
     *
     * @return Get a list of all tags the guild has set.
     */

    public Collection<Tag> getTags() {
        return tagData.keySet();
    }
}
