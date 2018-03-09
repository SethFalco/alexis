package com.elypia.alexis.discord.commands;

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
