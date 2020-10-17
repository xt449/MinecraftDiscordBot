package com.github.xt449.minecraftdiscordbot;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.awt.*;

/**
 * @author xt449 / BinaryBanana
 */
public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerJoin(PlayerJoinEvent event) {
		if(!AccountLinking.hasLink(event.getPlayer().getUniqueId())) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if(!AccountLinking.hasLink(event.getPlayer().getUniqueId())) {
			if(!(event.getMessage().startsWith("/link") || event.getMessage().startsWith("/pair"))) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");
			}
		} else {
			DiscordBot.guild.retrieveMemberById(AccountLinking.getDiscordLink(event.getPlayer().getUniqueId())).queue(member -> {
				final Color color = member.getColor();
				Bukkit.spigot().broadcast(new ComponentBuilder("<").append(event.getPlayer().getName()).color(net.md_5.bungee.api.ChatColor.of(color == null ? Color.WHITE : color)).append("> ").reset().append(event.getMessage()).create());
			});
		}

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerAnimation(PlayerAnimationEvent event) {
		if(!AccountLinking.hasLink(event.getPlayer().getUniqueId())) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");

			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerMove(PlayerMoveEvent event) {
		if(!AccountLinking.hasLink(event.getPlayer().getUniqueId())) {
			final Location to = event.getTo();
			if(to != null) {
				final Location from = event.getFrom();
				if((int) from.getX() - (int) to.getX() != 0 || (int) from.getZ() - (int) to.getZ() != 0) {
					event.getPlayer().sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");

					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerInteract(PlayerInteractEvent event) {
		if(!AccountLinking.hasLink(event.getPlayer().getUniqueId())) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "To begin linking your Discord and Minecraft accounts, run the command " + ChatColor.AQUA + "/link <discord username#tag>" + ChatColor.GRAY + "\n(ie: /link xt449#8551)");

			event.setCancelled(true);
		}
	}
}
