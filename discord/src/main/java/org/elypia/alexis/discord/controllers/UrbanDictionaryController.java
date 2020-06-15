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

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.urbandictionary.*;
import org.slf4j.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class UrbanDictionaryController {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryController.class);

    private final AlexisMessages messages;
    private final MessageSender sender;
    private final UrbanDictionary ud;

	@Inject
    public UrbanDictionaryController(AlexisMessages messages, MessageSender sender) {
        this.messages = messages;
        this.sender = sender;
        this.ud = new UrbanDictionary();
    }

    /**
     * @param message The event that caused this event.
     * @param terms All terms the user wants to define.
     * @param random If we should fetch random results, or the top definitions.
     */
    @StandardCommand(isDefault = true, isStatic = true)
    public void getDefinitions(Message message, @Param String[] terms, @Param("false") boolean random) {
        List<Observable<DefineResult>> requests = Stream.of(terms)
            .map(String::toLowerCase)
            .distinct()
            .map(ud::getDefinitions)
            .map(Single::toObservable)
            .collect(Collectors.toList());

        Observable<List<DefineResult>> test = Observable.zip(requests, objects -> {
            List<DefineResult> list = new ArrayList<>();

            for (Object object : objects)
                list.add((DefineResult)object);

            return list;
        });

        var contexts = AsyncUtils.copyContext();

        test.subscribe((results) -> {
            var requestContext = AsyncUtils.applyContext(contexts);
            Object response;

            if (results.isEmpty()) {
                response = messages.udNoDefinitions();
            } else if (results.size() > 1) {
                EmbedBuilder builder = DiscordUtils.newEmbed(message);
                builder.setTitle("Urban Dictionary", "https://www.urbandictionary.com/");

                for (DefineResult result : results) {
                    if (!result.hasDefinitions())
                        continue;

                    Definition definition = result.getDefinition(random);
                    String definitionBody = definition.getDefinitionBody();
                    String ifTruncated = "... " + MarkdownUtil.maskedLink(messages.readMore(), definition.getPermaLink());
                    String value = ChatUtils.truncateAndAppend(definitionBody, MessageEmbed.VALUE_MAX_LENGTH, ifTruncated);
                    builder.addField(definition.getWord(), value, false);
                }

                builder.setFooter(messages.udTotalResults(builder.getFields().size()));
                response = builder;
            } else {
                DefineResult result = results.get(0);
                response = (result.hasDefinitions()) ? result.getDefinition(random) : messages.udNoDefinitions();
            }

            sender.send(response);
            requestContext.deactivate();
        });
    }

    /**
     * @param id The ID of the definition on UrbanDictionary.
     */
    @StandardCommand
    public void getDefinitionById(@Param int id) {
        var contextCopy = AsyncUtils.copyContext();

        ud.getDefinitionById(id).subscribe(
            (definition) -> {
                var context = AsyncUtils.applyContext(contextCopy);
                sender.send(definition);
                context.deactivate();
            },
            (err) -> {},
            () -> {
                var context = AsyncUtils.applyContext(contextCopy);
                sender.send(messages.udNoDefinitions());
                context.deactivate();
            });
    }
}
