# Alexis [![Matrix]][matrix-community] [![Discord]][discord-guild] [![Docker]][docker-image] [![i18n-badge]][i18n] [![Build]][gitlab] [![Coverage]][gitlab] [![Donate]][elypia-donate]
## About Me
I'm a general purpose Discord bot with various functionality and integrations with many 
popular games and services like Twitch, Steam and RuneScape. I also perform many 
functions in guilds to help manage and automate management or encourage activity.

You can get even more information about me on my character profile on [Notebook.ai]!

## For Developers
### Libaries
We've worked really hard to not just make this bot, but to make a framework for
building them with ease. [Commandler] is a command handler which uses 
Java CDI extensions for an annotation driven approach to creating commands.
It handles most of the work for you, and is a breeze to get started with.

Commandler is a generic project which provides the foundation for a bot
but not the integration with a particular platform right away. [Comcord] is 
a command handler for Discord which includes everything you need to work
in Discord.

All services wrapped in this bot are available in [Elypiai], a repository where
we keep our wraps for services that don't provide official ones for Java.

### Self-Hosting
I recommend you use me through my invite link in this repository so you get,
the me, managed and served by Elypia, however if you're a developer or have the technical 
knowledge, you're always welcome to build and self-host me however you want, or clone me 
so you can modify my behaviour.

**If you self-host your own instance, please ensure it is clear that we do not endorse or acknowledge
your instance, and that it does _not_ use any of Elypia's graphics including Alexis' avatar.**

### Requirements
* Java 11
* [MySQL 5.7]

## Open-Source
This project is open-source under the [Apache 2.0] license!  
While not legal advice, you can find a [TL;DR] that sums up what
you're allowed and not allowed to do along with any requirements if you want to 
use or derive work from this source code!  

**The repository includes our trade name and registered trademarks, 
which are not granted under the license. Please do not use these 
except as required to describe the origin of work.** 

[matrix-community]: https://matrix.to/#/+elypia:matrix.org "Matrix Invite"
[discord-guild]: https://discord.com/invite/hprGMaM "Discord Invite"
[docker-image]: https://hub.docker.com/r/elypia/alexis-discord "Project on Docker"
[i18n]: https://i18n.elypia.org/engage/alexis/?utm "Weblate Translations"
[gitlab]: https://gitlab.com/Elypia/alexis/commits/master "Repository on GitLab"
[elypia-donate]: https://elypia.org/donate "Donate to Elypia"
[Notebook.ai]: https://www.notebook.ai/plan/characters/830595 "Alexis Character Design"
[Commandler]: https://gitlab.com/Elypia/commandler "Commandler on GitLab"
[Comcord]: https://gitlab.com/Elypia/comcord "Comcord on GitLab"
[Elypiai]: https://gitlab.com/Elypia/elypiai "Elypiai on GitLab"
[MySQL 5.7]: https://www.mysql.com "MySQL Database Server"
[Apache 2.0]: https://www.apache.org/licenses/LICENSE-2.0 "Apache 2.0 License"
[TL;DR]: https://tldrlegal.com/license/apache-license-2.0-(apache-2.0) "TL;DR of Apache 2.0"

[Matrix]: https://img.shields.io/matrix/elypia:matrix.org?logo=matrix "Matrix Shield"
[Discord]: https://discord.com/api/guilds/184657525990359041/widget.png "Discord Shield"
[Docker]: https://img.shields.io/docker/pulls/elypia/alexis-discord?logo=docker "Docker Shield"
[i18n-badge]: https://i18n.elypia.org/widgets/alexis/-/svg-badge.svg "Weblate Translation Badge"
[Build]: https://gitlab.com/Elypia/alexis/badges/master/pipeline.svg "GitLab Build Shield"
[Coverage]: https://gitlab.com/Elypia/alexis/badges/master/coverage.svg "GitLab Coverage Shield"
[Donate]: https://img.shields.io/badge/donate-elypia-blueviolet "Donate Shield"
