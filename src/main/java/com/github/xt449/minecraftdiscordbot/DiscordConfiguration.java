package com.github.xt449.minecraftdiscordbot;

import com.github.xt449.spigotutilitylibrary.AbstractConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
class DiscordConfiguration extends AbstractConfiguration {

	private final String path_token = "token";
	private final String path_inviteLink = "invite link";
	private final String path_commandPrefix = "command prefix";
	private final String path_guildId = "guild id";
	private final String path_roleId = "role id";

	String token;
	String inviteLink;
	String commandPrefix;
	long guildID;
	long roleID;

	DiscordConfiguration(Plugin plugin) {
		super(plugin, "discord.yml");
	}

	@Override
	public void readValues() {
		token = config.getString(path_token);
		inviteLink = config.getString(path_inviteLink);
		commandPrefix = config.getString(path_commandPrefix);
		guildID = config.getLong(path_guildId);
		roleID = config.getLong(path_roleId);
	}
}
