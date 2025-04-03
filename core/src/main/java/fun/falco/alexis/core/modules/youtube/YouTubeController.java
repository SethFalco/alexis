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

package fun.falco.alexis.core.modules.youtube;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.SearchResult;

import fun.falco.alexis.core.i18n.AlexisMessages;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class YouTubeController {

    private static final Logger logger = LoggerFactory.getLogger(YouTubeController.class);

    private AlexisMessages messages;
    private YouTubeService youtube;

    @Inject
    public YouTubeController(AlexisMessages messages, YouTubeService youtube) {
        this.messages = Objects.requireNonNull(messages);
        this.youtube = Objects.requireNonNull(youtube);
    }

    @StandardCommand
    public Object findVideo(@Param String query) throws IOException {
        try {
            Optional<SearchResult> searchResult = youtube.getSearchResult(query, ResourceType.VIDEO);
            return (searchResult.isPresent()) ? searchResult.get() : messages.noSearchResultsFound();
        } catch (GoogleJsonResponseException ex) {
            logger.error("The YouTube API hasn't been configured properly: {}", ex.getDetails().getMessage());
            return messages.youtubeApiError();
        }
    }
}
