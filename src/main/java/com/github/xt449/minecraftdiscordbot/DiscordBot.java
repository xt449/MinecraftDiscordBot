package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;
import java.util.UUID;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
public abstract class DiscordBot {

	static JDA jda;

	static String inviteLink;
	static String commandPrefix;
	static long guildId;
	static Guild guild;
	static long roleId;
	static Role role;

	static void initialize(DiscordConfiguration configuration) {
		DiscordBot.inviteLink = configuration.inviteLink;
		DiscordBot.commandPrefix = configuration.commandPrefix;
		DiscordBot.guildId = configuration.guildID;
		DiscordBot.roleId = configuration.roleID;

		final String version = Bukkit.getBukkitVersion();

		try {
			jda = JDABuilder.createDefault(configuration.token)
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
	}

	static void start() {
		try {
			jda.awaitReady();
		} catch(InterruptedException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		guild = jda.getGuildById(guildId);
		role = guild.getRoleById(roleId);
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

		@Override
		public void onGuildBan(GuildBanEvent event) {
			final UUID uuid = AccountLinking.getLink(event.getUser().getId());

			if(uuid != null) {
				final Player player = Bukkit.getPlayer(uuid);

				if(player != null) {
					Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Linked Discord account has been banned!", null, "Linked Account Ban");
					player.kickPlayer("Linked Discord account has been banned!");
				}
			}
		}
	};

	public static JDA getJDA() {
		return jda;
	}

	public static Guild getGuild() {
		return guild;
	}
}
