package com.elypia.alexis.discord.commands.modules;

import com.elypia.alexis.AlexisUtils;
import com.elypia.alexis.discord.commands.CommandEvent;
import com.elypia.alexis.discord.commands.annotation.*;
import com.elypia.alexis.discord.commands.impl.CommandHandler;
import com.sethsutopia.utopiai.nanowrimo.NaNoWriMo;

@Module(
	aliases = {"NaNoWriMo", "nano", "nnwm"},
	description = ""
)
public class NaNoWriMoHandler extends CommandHandler {
	
	private NaNoWriMo nanowrimo = new NaNoWriMo();
	
	@Override
	public boolean test() {

		return false;
	}

	@Command(
		aliases = {"authenticate", "auth"},
		help = "Authenticate to your NaNoWriMo account to prove who you are.",
		params = {
			@Parameter (
				param = "name",
				help = "Your NaNoWriMo username.",
				type = String.class
			),
			@Parameter (
				param = "secret",
				help = "Your NaNoWriMo secret, obtainable at: https://nanowrimo.org/api/wordcount",
				type = String.class,
				hidden = true
			),
			@Parameter (
				param = "wordcount",
				help = "Your total word count to submit.",
				type = Integer.class
			)
		}
	)
	public void authenticate(CommandEvent event) {
		event.tryDeleteMessage();
		
		String[] params = event.getParams();
		
		nanowrimo.updateWordCount(params[1], params[0], Integer.parseInt(params[2]), result -> {
			switch (result) {
				case ERROR_NO_ACTIVE_EVENT: {
					event.reply("Well done, you've succesfully authenticated to your account.");
					break;
				}
				default: {
					event.reply("Sorry you failed to authenticate to your account, please check your details and try again.");
				}
			}
		}, failure -> {
			AlexisUtils.unirestFailure(failure, event);
		});
	}
}
