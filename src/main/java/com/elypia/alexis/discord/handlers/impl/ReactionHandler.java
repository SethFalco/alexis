package com.elypia.alexis.discord.handlers.impl;

import com.elypia.commandler.jda.events.MessageEvent;

import java.util.*;

public abstract class ReactionHandler {

	public List<MessageEvent> tracked;

	public ReactionHandler() {
		tracked = new ArrayList<>();
	}

	public List<MessageEvent> getTrackedEvents() {
		return tracked;
	}
}
