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

package org.elypia.alexis;

import org.elypia.commandler.Commandler;
import org.slf4j.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * This is the main class for the bot which initialised everything Alexis
 * depends on and connects to Discord. This does not contain any
 * actual command handling code.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class Alexis {

    private static final Logger logger = LoggerFactory.getLogger(Alexis.class);

    /** The time this application started, this is used to determine runtime statistics. */
    public static final long START_TIME = System.currentTimeMillis();

    /**
     * @param args Command line arguments passed when running this application.
     */
    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        logger.info("Bridged JUL Logger to SLF4J.");

        logger.info("Initializing the Commandler application.");
        Commandler commandler = Commandler.create();

        try {
            commandler.run();
        } catch (Exception ex) {
            logger.error("Exception occured during Commandler initialization, backing out and exiting application.", ex);
            System.exit(ExitCode.INITIALIZATION_ERROR.getId());
        }
    }
}
