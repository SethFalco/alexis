package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Tag;
import com.elypia.alexis.discord.entities.data.TagFilter;
import com.elypia.alexis.discord.entities.impl.DatabaseEntity;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bson.Document;

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

    /**
     * Guild level tag settings, such as if a tag is enabled
     * or disabled, of if tha tag is set for {@link TagFilter#ONLY}
     * or {@link TagFilter#EXCEPT}.
     */

    private Map<Tag, TagData> tagData;

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
            TagData t = new TagData((Document)o);
            tagData.put(t.getTag(), t);
        });
    }

    @Override
    public void commit() {
        Document data = new Document();
        data.put("xp", xp);


        collection.updateOne(eq("guild_id", guildId), data, options);
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
        long id = channel.getIdLong();

        if (textChannelData.containsKey(id)) {
            TextChannelData txt = textChannelData.get(id);

            TagData data = tagData.get(tag);
        }

        return false;
    }

    public long getGuildIdLong() {
        return guildId;
    }

    public int getXp() {
        return xp;
    }

    public TextChannelData getTextChannelData(TextChannel channel) {
        long textChannelId = channel.getIdLong();
        return textChannelData.get(textChannelId);
    }
}
