package com.github.xt449.minecraftdiscordbot;

import com.github.xt449.spigotutilitylibrary.AbstractConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author xt449 / BinaryBanana
 */
class DiscordConfiguration extends AbstractConfiguration {

	private final String path_token = "token";
	private final String path_commandPrefix = "command prefix";
	private final String path_guildId = "guild id";

	String token;
	String commandPrefix;
	long guildID;

	DiscordConfiguration(Plugin plugin) {
		super(plugin, "discord.yml");
	}

	@Override
	public void readValues() {
		token = config.getString(path_token);
		commandPrefix = config.getString(path_commandPrefix);
		guildID = config.getLong(path_guildId);
	}
}
