package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
abstract class MinecraftWhoisCommand {

	static boolean execute(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length > 0) {
			final Player player = Bukkit.getPlayer(args[0]);

			if(player == null) {
				sender.sendMessage(ChatColor.GOLD + "Invalid player name!");
				return true;
			}

			final User user = DiscordBot.jda.getUserById(AccountLinking.getLink(player.getUniqueId()));

			if(user == null) {
				sender.sendMessage(ChatColor.GOLD + "This user is no longer part of the Discord server");
				return true;
			}

			sender.sendMessage(ChatColor.AQUA + user.getAsTag());
			return true;
		} else {
			return false;
		}
	}
}
