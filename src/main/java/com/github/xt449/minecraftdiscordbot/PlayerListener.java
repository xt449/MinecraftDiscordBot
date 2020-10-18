package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.awt.Color;

/**
 * @author xt449 / BinaryBanana
 */
public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		if(!AccountLinking.hasLink(player.getUniqueId())) {
			sendLinkingMessage(player);
		} else if(DiscordBot.jda.getUserById(AccountLinking.getDiscordLink(player.getUniqueId())) == null) {
			sendRejoinMessage(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();

		if(!AccountLinking.hasLink(player.getUniqueId())) {
			if(!(event.getMessage().startsWith("/link") || event.getMessage().startsWith("/pair"))) {
				sendLinkingMessage(player);
			}
		} else {
			final User user = DiscordBot.jda.getUserById(AccountLinking.getDiscordLink(player.getUniqueId()));
			if(user == null) {
				sendRejoinMessage(player);
			} else {
				DiscordBot.guild.retrieveMember(user).queue(member -> {
					final Color color = member.getColor();
					Bukkit.spigot().broadcast(new ComponentBuilder("<").append(player.getName()).color(ChatColor.of(color == null ? Color.WHITE : color)).append("> ").reset().append(event.getMessage()).create());
				});
			}
		}

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		final Player player = event.getPlayer();

		if(!AccountLinking.hasLink(player.getUniqueId())) {
			sendLinkingMessage(player);

			event.setCancelled(true);
		} else if(DiscordBot.jda.getUserById(AccountLinking.getDiscordLink(player.getUniqueId())) == null) {
			sendRejoinMessage(player);

			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();

		if(!AccountLinking.hasLink(player.getUniqueId())) {
			final Location to = event.getTo();
			if(to != null) {
				final Location from = event.getFrom();
				if((int) from.getX() - (int) to.getX() != 0 || (int) from.getZ() - (int) to.getZ() != 0) {
					sendLinkingMessage(player);

					event.setCancelled(true);
				}
			}
		} else if(DiscordBot.jda.getUserById(AccountLinking.getDiscordLink(player.getUniqueId())) == null) {
			final Location to = event.getTo();
			if(to != null) {
				final Location from = event.getFrom();
				if((int) from.getX() - (int) to.getX() != 0 || (int) from.getZ() - (int) to.getZ() != 0) {
					sendRejoinMessage(player);

					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();

		if(!AccountLinking.hasLink(player.getUniqueId())) {
			sendLinkingMessage(player);

			event.setCancelled(true);
		} else if(DiscordBot.jda.getUserById(AccountLinking.getDiscordLink(player.getUniqueId())) == null) {
			sendRejoinMessage(player);

			event.setCancelled(true);
		}
	}

	private void sendLinkingMessage(Player player) {
		player.sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, join the Discord server @ " + ChatColor.AQUA + DiscordBot.inviteLink + ChatColor.YELLOW + " and then run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");
	}

	private void sendRejoinMessage(Player player) {
		player.sendMessage(ChatColor.RED + "Your linked Discord account is not in the server!\nPlease rejoin the Discord server @ " + ChatColor.AQUA + DiscordBot.inviteLink + ChatColor.YELLOW + " to regain access");
	}
}
