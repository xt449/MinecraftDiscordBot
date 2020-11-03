package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Jonathan Taclott (xt449 / BinaryBanana)
 */
abstract class DiscordLinkCommand {

	public static void execute(PrivateMessageReceivedEvent event) {
		// ignore messages from bots
		if(event.getAuthor().isBot()) {
			return;
		}

		final String content = event.getMessage().getContentRaw();
		// ignore messages without text content (files, system messages, embeds)
		if(content.length() == 0) {
			return;
		}

		final Player player = Bukkit.getPlayer(content);

		if(player == null) {
			event.getChannel().sendMessage("Invalid username!").queue();
			return;
		}

		if(AccountLinking.finishLinking(player.getUniqueId(), event.getAuthor().getId())) {
			event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Your Discord and Minecraft accounts have been linked.").queue());
			if(DiscordBot.role != null) {
				DiscordBot.guild.addRoleToMember(DiscordBot.guild.getMember(event.getAuthor()), DiscordBot.role).queue();
			}
			player.sendMessage(ChatColor.GREEN + "Your Discord and Minecraft accounts have been linked.");
		}
	}
}
