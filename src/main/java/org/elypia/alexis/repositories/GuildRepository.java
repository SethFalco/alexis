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

import net.dv8tion.jda.api.entities.Guild;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.services.DatabaseService;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.*;
import java.util.Locale;

/**
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class GuildRepository extends AbstractRepository<GuildData> {

    @Inject
    public GuildRepository(final DatabaseService dbService) {
        super(dbService);
    }

    /**
     * @param id The ID of the {@link Guild} to select.
     * @return The data this application is storing on the guild.
     */
    public GuildData selectGuild(long id) {
        try (Session session = dbService.open()) {
            return session.get(GuildData.class, id);
        }
    }

    public int updateLocale(final long id, final Locale locale) {
        return updateColumn(id, "locale", locale);
    }

    /**
     * @param id The ID of the guild.
     * @param prefix What to set he guild's prefix to.
     * @return The number of rows that changed, this should always be 1.
     */
    public int updatePrefix(final long id, final String prefix) {
        return updateColumn(id, "prefix", prefix);
    }

    public int updateReactTranslation(final long id, final boolean toggle) {
        return updateColumn(id, "reactTranslation", toggle);
    }

    public <T> int updateColumn(final long id, String column, final T value) {
        final String queryString = "UPDATE org.elypia.alexis.entities.GuildData SET " + column + " = :column WHERE id = :id";

        try (Session session = dbService.open()) {
            session.beginTransaction();

            Query query = session.createQuery(queryString)
                .setParameter("id", id)
                .setParameter("column", value);

            int result = query.executeUpdate();
            session.getTransaction().commit();
            return result;
        }
    }
}
