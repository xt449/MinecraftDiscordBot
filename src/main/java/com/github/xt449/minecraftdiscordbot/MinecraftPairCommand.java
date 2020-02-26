package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class MinecraftPairCommand implements CommandExecutor {

	private final MinecraftDiscordBot minecraftDiscordBot;

	MinecraftPairCommand(MinecraftDiscordBot plugin) {
		minecraftDiscordBot = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 0) {
				String[] tag = args[0].split("#");
				if(tag.length < 2) {
					return false;
				}

				for(User user : minecraftDiscordBot.discordBot.jda.getUsersByName(tag[0], true)) {
					if(user.getDiscriminator().equalsIgnoreCase(tag[1])) {
						Player player = (Player) sender;

						if(minecraftDiscordBot.configPairs.pairs.containsKey(player.getUniqueId())) {
							sender.sendMessage(ChatColor.GOLD + "Your Discord and Minecraft accounts are already paired!");
							return true;
						}

						boolean complete = minecraftDiscordBot.configPendingPairs.pairFromMinecraft(player, user);

						if(complete) {
							sender.sendMessage(ChatColor.GREEN + "Your Discord and Minecraft accounts are now paired!");
						} else {
							sender.sendMessage(ChatColor.AQUA + "Finish pairing your accounts by using " + minecraftDiscordBot.configDiscord.command_prefix + "pair " + player.getName() + " on the Minecraft server!");
						}

						return true;
					}
				}

				sender.sendMessage(ChatColor.RED + "Unknown user. Try joining the Discord server first!");
			}
		} else {
			sender.sendMessage("This command must be used by a player.");
		}

		return false;
	}
}
