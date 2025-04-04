/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.urbandictionary.DefineResult;
import org.elypia.elypiai.urbandictionary.Definition;
import org.elypia.elypiai.urbandictionary.UrbanDictionary;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.discord.utils.DiscordUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.MarkdownUtil;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class UrbanDictionaryController {

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
     * @param message Event that caused this event.
     * @param terms Terms the user wants to define.
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

            for (Object object : objects) {
                list.add((DefineResult) object);
            }

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
                    if (!result.hasDefinitions()) {
                        continue;
                    }

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
     * @param id ID of the definition on UrbanDictionary.
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
