/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * Objects created specifically to prep up objects
 * for messengers to display messages in a way
 * friendly for users.
 *
 * This could abstract information away, add custom
 * information, or simply wrap the object so it's more
 * friendly for a messenger to handle it.
 *
 * <strong>
 *     While <em>NOT</em> bad practice, it's encouraged
 *     to have {@link org.elypia.commandler.api.Messenger} implementations
 *     manage how the message is built and sent back.
 * </strong>
 *
 * @author seth@elypia.org (Seth Falco)
 */
package org.elypia.alexis.models;
