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

	public static boolean execute(CommandSender sender, Command command, String alias, String[] args) {
		if(sender instanceof Player) {
			if(args.length > 0) {
				try {
					final User user = DiscordBot.jda.getUserByTag(args[0]);

					if(user == null) {
						throw new IllegalArgumentException();
					}

					if(AccountLinking.beginLinking(((Player) sender).getUniqueId(), user.getId())) {
						user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Are you " + sender.getName() + "?\nUse the command `/link " + ((Player) sender).getUniqueId().toString() + "` in <#743810987714543676> to complete linking!").queue());
						sender.sendMessage("Confirmation message sent to discord!");
					} else {
						sender.sendMessage("Accounts already linked!");
					}
				} catch(IllegalArgumentException exc) {
					sender.sendMessage(ChatColor.GRAY + "Invalid Discord tag!");
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
}
