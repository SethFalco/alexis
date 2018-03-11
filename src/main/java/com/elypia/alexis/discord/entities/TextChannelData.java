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

        List<String> tags = (document.get("tags", List.class));

        tags.forEach(tag -> {
            Tag t = Tag.getByName(tag);
            tagsList.add(t);
        });
    }

    public Document asDocument() {
        Document document = new Document();
        document.put("text_channel_id", textChannelId);
        document.put("tags", tagsList);

        return document;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public TagData getTagData(Tag tag) {
        return null;
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
