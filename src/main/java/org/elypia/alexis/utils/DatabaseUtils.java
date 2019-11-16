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

package org.elypia.alexis.utils;

import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public final class DatabaseUtils {

    /**
     * @param type The type of entity to return all rows for.
     * @param session The open session to use for this query.
     * @param <T> The type of entity to return all rows for.
     * @return A list of all rows in the table for this entity.
     */
    public static <T> List<T> getAllRows(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> all = query.from(type);
        query = query.select(all);
        TypedQuery<T> allQuery = session.createQuery(query);
        return allQuery.getResultList();
    }
}
