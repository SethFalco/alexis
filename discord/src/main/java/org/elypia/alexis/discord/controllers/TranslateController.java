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

package org.elypia.alexis.discord.controllers;

import com.google.cloud.translate.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.models.TranslationModel;
import org.elypia.alexis.repositories.GuildRepository;
import org.elypia.alexis.services.TranslateService;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TranslateController implements Controller {

    private final GuildRepository guildRepo;
    private final TranslateService translateService;
    private final AlexisMessages messages;

    @Inject
    public TranslateController(GuildRepository guildRepo, TranslateService translateService, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.translateService = translateService;
        this.messages = messages;
    }

    public TranslationModel translate(String body, Language language) {
        Translation translation = translateService.translate(body, language);
        return new TranslationModel(translation, body, language);
    }

    public String toggle(@Channels(ChannelType.TEXT) @Elevated ActionEvent<Event, Message> event, boolean toggle) {
        Guild guild = event.getRequest().getMessage().getGuild();
        guildRepo.updateReactTranslation(toggle, guild.getIdLong());
        return (toggle) ? messages.featureEnabledTryItNow() : messages.reactionFeatureDisabled();
    }

//    public String inPrivate(@Channels(ChannelType.TEXT) @Elevated ActionEvent<Event, Message> event, boolean isPrivate) {
//        Guild guild = event.getSource().getGuild();
//        GuildData data = GuildData.query(guild.getIdLong());
//        TranslateSettings settings = data.getSettings().getTranslateSettings();
//
//        if (settings.isPrivate() == isPrivate)
//            return scripts.get("translate.dm.no_change", Map.of("enabled", isPrivate));
//
//        settings.setPrivate(isPrivate);
//        data.commit();
//
//        return scripts.get("translate.dm.response", Map.of("enabled", isPrivate));
//    }
}
