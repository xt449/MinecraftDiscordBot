package com.github.xt449.minecraftdiscordbot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author xt449 / BinaryBanana
 */
public class MinecraftDiscordBot extends JavaPlugin {

	static DiscordConfiguration configDiscord;

	@Override
	public final void onEnable() {
		configDiscord = new DiscordConfiguration(this);
		configDiscord.initialize();

		DiscordBot.initialize();
	}

	@Override
	public final void onDisable() {
		DiscordBot.jda.shutdown();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if("link".equals(command.getName())) {
			return MinecraftLinkCommand.execute(sender, command, label, args);
		}

		return false;
	}
}
