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
