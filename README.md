# Alexis [![](https://gitlab.com/SethFalco/alexis/badges/master/pipeline.svg)](https://gitlab.com/SethFalco/alexis/commits/main)

## Deprecation Notice

Alexis has not been worked on for years, and is unlikely to ever return. Alexis, and the libraries it depends on were all part of my first real software engineering project. Looking back, it's not very good, but I'm very proud of the result given how early I was in my career when I created this.

At it's peak, Alexis was in over 120~ guilds on Discord concurrently! 🎉

I'm leaving the code up for archival purposes, but would not expect anyone to actually build and run the project. Feel free to review it for any purpose, however. I've done the bare minimum to clean house, make it buildable on Java v17, and add documentation.

## About

Alexis was a general-purpose chatbot with integrations with many popular games and services like Twitch, Steam, and RuneScape. It also performed many functions in guilds to help manage and automate management or encourage activity.

## For Developers

### Libraries

Most of the work was put into building out the framework to create chatbots, not just building Alexis itself. [Commandler](https://gitlab.com/SethFalco/commandler) is a command handler which uses 
Java CDI extensions for an annotation-driven approach to creating commands.

Commandler is a generic project which provides the foundation for a chatbot and is not coupled with any particular platform. [Comcord](https://gitlab.com/SethFalco/comcord) is a command handler for Discord which includes everything you need to work in Discord.

Most third-party services supported in this project were wrapped in [Elypiai](https://gitlab.com/SethFalco/elypiai) where official Java libraries were not available.

### Requirements

* Java
* [MySQL 5.7](https://www.mysql.com)
