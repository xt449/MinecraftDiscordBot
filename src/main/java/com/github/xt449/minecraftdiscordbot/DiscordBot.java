package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;

/**
 * @author xt449 / BinaryBanana
 */
abstract class DiscordBot {

	//static final char COMMAND_PREFIX = '/';
	static JDA jda;
	static Guild guild;

	static void initialize() {
		try {
			jda = JDABuilder.createDefault("NTY5MTcwODEwODYyOTYwNjQw.XMI8IA.gElso5XPEYE47FZ_EvZPa-q-0Pg")
					.build();
		} catch(LoginException exc) {
			exc.printStackTrace();
			System.out.println("Unable to connect to Discord API. Bot Token is invalid or the servers are offline!");
			System.exit(1);
		}

		try {
			jda.awaitReady();
		} catch(InterruptedException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		guild = jda.getGuildById(MinecraftDiscordBot.configDiscord.guildID);
	}
}
