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

	private final String bot_token_path = "bot.token";
	private final String bot_status_path = "bot.status";
	private final String command_prefix_path = "command.prefix";
	private final String guild_id_path = "guild.id";

	String bot_token;
	String bot_status;
	String command_prefix;
	long guild_id;

	DiscordConfiguration(Plugin plugin) {
		super(plugin, "discord.yml");
	}

	@Override
	public void setDefaults() {
		config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(filePath))));
	}

	@Override
	public void getValues() {
		bot_token = config.getString(bot_token_path);
		bot_status = config.getString(bot_status_path);
		command_prefix = config.getString(command_prefix_path);
		guild_id = config.getLong(guild_id_path);
	}
}
