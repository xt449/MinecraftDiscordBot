package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author xt449 / BinaryBanana
 */
abstract class MinecraftLinkCommand {

	static boolean execute(CommandSender sender, Command command, String alias, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 0) {
				try {
					final User user = DiscordBot.jda.getUserByTag(String.join(" ", args));

					if(user == null) {
						sender.sendMessage(ChatColor.GOLD + "Invalid Discord tag!");
						return true;
					}

					if(AccountLinking.beginLinking(((Player) sender).getUniqueId(), user.getId())) {
						user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Did you just begin linking your Discord and Minecraft accounts?\nReply to this with the username of your Minecraft account to complete linking!\n(Note: You must be currently logged onto the server to confirm the account linking.)").queue());
						sender.sendMessage(ChatColor.GREEN + "Confirmation message sent to discord!");
					} else {
						sender.sendMessage(ChatColor.GOLD + "Accounts already linked!");
					}
				} catch(IllegalArgumentException exc) {
					sender.sendMessage(ChatColor.GOLD + "Invalid Discord tag!");
					return true;
				}
			} else {
				return false;
			}
		}
		return true;
	}
}
