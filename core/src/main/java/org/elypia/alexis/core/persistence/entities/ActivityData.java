/*
 * Copyright 2019-2020 Seth Falco and Alexis Contributors
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

package org.elypia.alexis.core.persistence.entities;

import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@DynamicInsert
@DynamicUpdate
@Entity
@Table(
    name = "activity",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"activity_type", "activity_text", "activity_url"})
    }
)
public class ActivityData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private int id;

    @ColumnDefault("0")
    @Column(name = "activity_type", nullable = false)
    private int type;

    @Column(name = "activity_text")
    private String text;

    @Column(name = "activity_url")
    private String url;

    @ColumnDefault("1")
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public ActivityData() {
        // Do nothing.
    }

    public ActivityData(int type, String text) {
        this(type, text, null);
    }

    public ActivityData(int type, String text, String url) {
        this(type, text, url, true);
    }

    public ActivityData(int type, String text, String url, boolean enabled) {
        this.type = type;
        this.text = Objects.requireNonNull(text, "Activity text must not be null.");
        this.url = url;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
