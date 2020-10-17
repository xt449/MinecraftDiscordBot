package com.github.xt449.minecraftdiscordbot;

import com.github.xt449.spigotutilitylibrary.AbstractConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author xt449 / BinaryBanana
 */
class DiscordConfiguration extends AbstractConfiguration {

	private final String path_commandPrefix = "command prefix";
	private final String path_guildId = "guild id";

	String commandPrefix;
	long guildID;

	DiscordConfiguration(Plugin plugin) {
		super(plugin, "discord.yml");
	}

	@Override
	public void readValues() {
		commandPrefix = config.getString(path_commandPrefix);
		guildID = config.getLong(path_guildId);
	}
}
