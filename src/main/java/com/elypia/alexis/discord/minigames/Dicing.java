package com.elypia.alexis.discord.minigames;

import com.elypia.elypiai.utils.ElyUtils;
import net.dv8tion.jda.core.entities.User;

import java.util.*;

public class Dicing {

	private Set<User> players;
	private Map<User, Integer> leaderboard;
	private int minValue;
	private int maxValue;

	public Dicing() {
		players = new HashSet<>();
		leaderboard = new HashMap<>();
		minValue = 1;
		maxValue = 120;
	}

	public boolean addPlayer(User user) {
		return players.add(user);
	}

	public boolean removePlayer(User user) {
		return players.remove(user);
	}

	public Map<User, Integer> result() {
		Random rand = ElyUtils.RANDOM;

		players.forEach(player -> {
			int roll = rand.nextInt(maxValue + minValue) - minValue;
			leaderboard.put(player, roll);
		});

		Collections.sort((List<Integer>)leaderboard.values());

		return leaderboard;
	}
}
