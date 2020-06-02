/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.alexis.persistence.entities;

import org.elypia.commandler.event.Action;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Wraps around {@link Action}
 * in order to persist that data into a database.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(name = "action")
public class ActionData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "action_id")
    protected String id;

    @Column(name = "action_content", nullable = false, length = 2000)
    private String content;

    @Column(name = "controller_type", nullable = false)
    private Class<?> controllerType;

    @Column(name = "method_name", nullable = false)
    private String methodName;

    public ActionData() {
        // Do nothing
    }

    public ActionData(String id) {
        this.id = id;
    }

    public ActionData(String id, String content, Class<?> controllerType, String methodName) {
        this(id);
        this.content = content;
        this.controllerType = controllerType;
        this.methodName = methodName;
    }

    public Serializable getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Class<?> getControllerType() {
        return controllerType;
    }

    public void setControllerType(Class<?> controllerType) {
        this.controllerType = controllerType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
