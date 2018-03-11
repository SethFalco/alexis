package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Tag;
import com.elypia.alexis.discord.entities.data.TagFilter;
import org.bson.Document;

/**
 * The global tag settings for the guild, for example
 * the global {@link Tag#SPAM} will affect all channels
 * with this tag. (or without if set to blacklist.)
 */

public class TagData {

    private Tag tag;
    private boolean enabled;
    private TagFilter filter;

    public TagData(Document document) {
        tag = Tag.getByName(document.getString("tag"));
        enabled = document.getBoolean("enabled");
        filter = TagFilter.getByName(document.getString("filter"));
    }

    public Document asDocument() {
        Document document = new Document();
        document.put("tag", tag);
        document.put("enabled", enabled);
        document.put("filter", filter);

        return document;
    }

    public Tag getTag() {
        return tag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TagFilter getFilter() {
        return filter;
    }

    public void setFilter(TagFilter filter) {
        this.filter = filter;
    }
}
