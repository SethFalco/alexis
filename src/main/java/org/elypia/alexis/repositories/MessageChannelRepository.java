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

package org.elypia.alexis.repositories;

import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.alexis.services.DatabaseService;
import org.hibernate.Session;

import javax.inject.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class MessageChannelRepository extends AbstractRepository<MessageChannelData> {

    @Inject
    public MessageChannelRepository(final DatabaseService dbService) {
        super(dbService);
    }

    public MessageChannelData findMessageChannelDataById(long id) {
        try (Session session = dbService.open()) {
            return session.get(MessageChannelData.class, id);
        }
    }
}
