package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Tag;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TextChannelData {

    private GuildData guildData;
    private long textChannelId;
    private List<Tag> tagsList;

    public TextChannelData(GuildData guildData, Document document) {
        this.guildData = guildData;
        textChannelId = document.getLong("text_channel_id");
        tagsList = new ArrayList<>();

        List<Document> tags = (document.get("tags", List.class));
        tags.forEach(tag -> tagsList.add(Tag.getByName(tag.getString("tag"))));
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public TagData getTagData(Tag tag) {
        return guildData.isChannel(this, tag);
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public void setTextChannelId(long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public boolean hasTag(Tag tag) {
        return tagsList.contains(tag);
    }
}
