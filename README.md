# Alexis [![](https://gitlab.com/SethFalco/alexis/badges/main/pipeline.svg)](https://gitlab.com/SethFalco/alexis)

## Deprecation Notice

Alexis has not been worked on for years, and is unlikely to ever return. Alexis, and the libraries it depends on were all part of my first real software engineering project. Looking back, it's not very good, but I'm very proud of the result given how early I was in my career when I created this.

At it's peak, Alexis was in over 120~ guilds on Discord concurrently! 🎉

I'm leaving the code up for archival purposes, but would not expect anyone to actually build and run the project. Feel free to review it for any purpose, however. I've done the bare minimum to clean house, make it buildable on Java v17, and add documentation.

## About

Alexis was a general-purpose chatbot with integrations with many popular games and services like Twitch, Steam, and RuneScape. It also performed many functions in guilds to help manage and automate management or encourage activity.

## Getting Started

There is no official or hosted version of Alexis available. To run this, you must build and configure the project yourself.

### Requirements

* [Git](https://git-scm.com/)
* Java 17
* [Gradle 7](https://gradle.org/)

### Building

```sh
# Clone the project
git clone https://gitlab.com/SethFalco/alexis.git
cd alexis

# Build the project
./gradlew assemble

# Move build to current directory
mv discord/build/libs/alexis-discord.jar .
```

### Configuration

Alexis can be configured through environment variables or Java system properties. A Discord bot token (`discord.bot-token`) is the only required configuration.

If a MySQL database is not configured (`application.persistence.*`), we fall back to an embedded H2 database, however this is not recommended for production environments.

|Variable / Property|Explanation|
|---|---|
|`discord.bot-token`|Discord bot token, this is required to run the bot. You can get one from the [Discord Developer Portal](https://discord.com/developers/docs/intro). |
|`application.name`|Name of the application, this is how the chatbot will refer to itself.|
|`application.author.name`|Name of the author of the application.|
|`application.author.url`|URL to attribute the author, like a website or social media profile.|
|`application.author.support-guild-id`|Discord snowflake of the guild/server that is used to provide support for this instance.|
|`application.persistence.dialect`|Dialect for the SQL connection, this should typically be set to `org.hibernate.dialect.MySQL57Dialect` or `org.hibernate.dialect.MySQL8Dialect` depending on the version of MySQL you're running.|
|`application.persistence.driver`|Driver to use for the SQL connection, recommended value is `com.mysql.cj.jdbc.Driver`.|
|`application.persistence.url`|Connection URL to an SQL database server.|
|`application.persistence.username`|Username to connect to the SQL database server.|
|`application.persistence.password`|Password to connect to the SQL database server.|
|`application.cleverbot.api-key`|API key for [Cleverbot](https://www.cleverbot.com/api/), required for Cleverbot integration.<br><br>⚠️ **Heads-up: Cleverbot is a billed API. There is no way to use it without spending money.**|
|`application.osu.api-key`|API key for [osu!](https://osu.ppy.sh/wiki/en/osu%21api), required for osu! integration.|
|`application.steam.api-key`|API key for [Steam](https://steamcommunity.com/dev), required for Steam integration.|
|`application.twitch.client-id`|Twitch client ID, required for Twitch integration. You can get credentials on the [Twitch Developer Portal](https://dev.twitch.tv/).|
|`application.twitch.client-secret`|Twitch client secret, required for Twitch integration. You can get credentials on the [Twitch Developer Portal](https://dev.twitch.tv/).|
|`org.apache.deltaspike.ProjectStage`|Environment the build is deployed to, possible values: `Production`, `Development`|
|`GOOGLE_APPLICATION_CREDENTIALS`|Path to your Google Cloud Platform service account credentials file. Required for YouTube and Google Translate. Environment variable only! <br><br>⚠️ **Heads-up: Google Translate has a limited free-tier, but with enough usage, this will incur a bill.**|
|`GOOGLE_CLOUD_LOGGING`|If `GOOGLE_APPLICATION_CREDENTIALS` is also populated, this can be set to `true` to send logs to Google Cloud Logging. Environment variable only! |

### Running

To run the project on your hosts Java runtime, the easiest way is to define the `JAVA_OPTS`environment variable with system properties.

```sh
# Optional
GOOGLE_APPLICATION_CREDENTIALS=path/to/file
GOOGLE_CLOUD_LOGGING=true

# Required, additional options can be specified here too
JAVA_OPTS='
  -Ddiscord.bot-token={BOT_TOKEN}
'

# Run the chatbot!
java ${JAVA_OPTS} --add-opens java.base/java.lang=ALL-UNNAMED -jar ./alexis-discord.jar
```

## For Developers

### Libraries

Most of the work was put into building out the framework to create chatbots, not just building Alexis itself. [Commandler](https://gitlab.com/SethFalco/commandler) is a command handler which uses 
Java CDI extensions for an annotation-driven approach to creating commands.

Commandler is a generic project which provides the foundation for a chatbot and is not coupled with any particular platform. [Comcord](https://gitlab.com/SethFalco/comcord) is a command handler for Discord which includes everything you need to work in Discord.

Most third-party services supported in this project were wrapped in [Elypiai](https://gitlab.com/SethFalco/elypiai) where official Java libraries were not available.

It's encouraged to review all of these projects independently to build your own bot from scratch. Alexis can be considered a reference implementation for how to use them.
