package com.elypia.alexis.discord.entities;

import com.elypia.alexis.discord.entities.data.Tag;
import com.elypia.alexis.discord.entities.data.TagFilter;
import org.bson.Document;

public class TagData {

    private Tag tag;
    private boolean enabled;
    private TagFilter filter;

    public TagData(Document document) {
        tag = Tag.getByName(document.getString("tag"));
        enabled = document.getBoolean("enabled");
        filter = TagFilter.getByName(document.getString("filter"));
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
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
