package com.elypia.alexis.discord.handlers.commands;

import com.elypia.alexis.discord.events.CommandEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class ReactionHandler {

	public List<CommandEvent> tracked;

	public ReactionHandler() {
		tracked = new ArrayList<>();
	}

	public List<CommandEvent> getTrackedEvents() {
		return tracked;
	}
}
