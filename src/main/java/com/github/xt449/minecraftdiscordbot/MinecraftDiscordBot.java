package com.github.xt449.minecraftdiscordbot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
public class MinecraftDiscordBot extends JavaPlugin implements Listener {

	@Override
	public void onLoad() {
		final DiscordConfiguration config = new DiscordConfiguration(this);
		config.initialize();
		DiscordBot.initialize(config);
	}

	@Override
	public final void onEnable() {
		AccountLinking.load();

		DiscordBot.start();

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public final void onDisable() {
		AccountLinking.save();
		DiscordBot.jda.shutdown();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if("link".equals(command.getName())) {
			return MinecraftLinkCommand.execute(sender, command, label, args);
		} else if("whois".equals(command.getName())) {
			return MinecraftWhoisCommand.execute(sender, command, label, args);
		}

		return false;
	}
}
