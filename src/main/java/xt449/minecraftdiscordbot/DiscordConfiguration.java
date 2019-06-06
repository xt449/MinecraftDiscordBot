package xt449.minecraftdiscordbot;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xt449.bukkitutilitylibrary.AbstractConfiguration;

import java.io.InputStreamReader;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class DiscordConfiguration extends AbstractConfiguration {

	private final String path_commandPrefix = "command prefix";
	private final String path_guildId = "guild id";

	String commandPrefix /*= "!"*/;
	long guildID;

	DiscordConfiguration(Plugin plugin) {
		super(plugin, "discord.yml");
	}

	@Override
	public void setDefaults() {
		config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(filePath))));
	}

	@Override
	public void getValues() {
		commandPrefix = config.getString(path_commandPrefix);
		guildID = config.getLong(path_guildId);
	}
}
