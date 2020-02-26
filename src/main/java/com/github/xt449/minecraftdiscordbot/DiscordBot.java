package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class DiscordBot {

	private final MinecraftDiscordBot minecraftDiscordBot;

	final JDA jda;
	final Guild guild;

	DiscordBot(MinecraftDiscordBot plugin) {
		minecraftDiscordBot = plugin;

		JDA jdaTemp = null;
		try {
			jdaTemp = new JDABuilder("").build();
		} catch(LoginException exc) {
			exc.printStackTrace();
			System.out.println("Unable to connect to Discord API. Bot Token is invalid or the servers are offline!");
			System.exit(1);
		}

		jda = jdaTemp;

		try {
			jda.awaitReady();
		} catch(InterruptedException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		guild = jda.getGuildById(minecraftDiscordBot.configDiscord.guild_id);
	}
}
