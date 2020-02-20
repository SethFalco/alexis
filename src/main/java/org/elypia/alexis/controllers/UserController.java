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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.entities.User;
import org.elypia.comcord.Scope;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.commandler.api.Controller;

import javax.inject.Singleton;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class UserController implements Controller {

	public User info(@Scoped(inGuild = Scope.LOCAL, inPrivate = Scope.MUTUAL) User user) {
        return user;
	}
}
