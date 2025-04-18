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

package fun.falco.alexis.core;

import org.elypia.commandler.Commandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Main class for the bot which initialized everything Alexis depends on and
 * connects to Discord. This does not contain any actual command handling code.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class Alexis {

    private static final Logger logger = LoggerFactory.getLogger(Alexis.class);

    /** Time this application started, this is used to determine runtime statistics. */
    public static final long START_TIME = System.currentTimeMillis();

    /**
     * @param args Command line arguments passed when running this application.
     */
    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        logger.info("Bridged JUL Logger to SLF4J.");

        logger.info("Bootstrapping Commandler application.");
        Commandler commandler = Commandler.create();

        try {
            logger.info("Initializing Commandler application.");
            commandler.run();
        } catch (Exception ex) {
            logger.error("Exception occurred during Commandler initialization, backing out and exiting application.", ex);
            System.exit(ExitCode.INITIALIZATION_ERROR.getId());
        }
    }
}
