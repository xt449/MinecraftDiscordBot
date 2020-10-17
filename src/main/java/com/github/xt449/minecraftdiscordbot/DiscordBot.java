package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

/**
 * @author xt449 / BinaryBanana
 */
abstract class DiscordBot {

	static String commandPrefix;
	static JDA jda;
	static Guild guild;

	static void initialize(String token, long guildId, String commandPrefix) {
		DiscordBot.commandPrefix = commandPrefix;

		final String version = Bukkit.getBukkitVersion();

		try {
			jda = JDABuilder.createDefault(token)
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.addEventListeners(listener)
					.setActivity(Activity.playing(version.substring(0, version.indexOf('-'))))
					.build();
		} catch(LoginException exc) {
			exc.printStackTrace();
			System.out.println("Unable to connect to Discord API. Bot Token is invalid or the servers are offline!");
			System.exit(1);
		}

		AccountLinking.load();

		try {
			jda.awaitReady();
		} catch(InterruptedException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		guild = jda.getGuildById(guildId);
	}

	private static final ListenerAdapter listener = new ListenerAdapter() {
		@Override
		public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
			DiscordLinkCommand.execute(event);
		}

		@Override
		public void onGuildReady(GuildReadyEvent event) {
			event.getGuild().loadMembers().onSuccess(list -> System.out.println("Discord Guild Member list cached!"));
		}
	};
}
