package xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
public class MinecraftDiscordBot extends JavaPlugin {

	/*static*/ PairsConfiguration configPairs;
	/*static*/ PendingPairsConfiguration configPendingPairs;
	/*static*/ DiscordConfiguration configDiscord;
	private /*static*/ RolesConfiguration configRoles;

	/*static*/ DiscordBot discordBot;

	private Permission permissionHook;
	private Chat chatHook = null;

	@Override
	public final void onLoad() {
		final RegisteredServiceProvider<Permission> rspPermission = Bukkit.getServicesManager().getRegistration(Permission.class);
		if(rspPermission != null) {
			permissionHook = rspPermission.getProvider();
		} else {
			getLogger().severe("Error initializing Vault permission hook!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		final RegisteredServiceProvider<Chat> rspChat = Bukkit.getServicesManager().getRegistration(Chat.class);
		if(rspChat != null) {
			chatHook = rspChat.getProvider();
		} else {
			getLogger().severe("Error initializing Vault chat hook!");
			// TODO - Use permissionHook in abscence of chatHook
			/*Bukkit.getPluginManager().disablePlugin(this);
			return;*/
		}

		// code
	}

	@Override
	public final void onEnable() {
		configPairs = new PairsConfiguration(this);
		configPairs.initialize();

		configPendingPairs = new PendingPairsConfiguration(this);
		configPendingPairs.initialize();

		getCommand("pair").setExecutor(new MinecraftPairCommand(this));

		getCommand("syncroles").setExecutor((sender, command, label, args) -> {
			Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
				for(final User user : Arrays.stream(Bukkit.getOfflinePlayers()).map(player -> configPairs.getDiscordUser(player.getUniqueId())).filter(Objects::nonNull).collect(Collectors.toList())) {
					updateUserDiscordRoles(user, true);
				}

				sender.sendMessage("All paired accounts have had their roles updated!");
			});

			return true;
		});

		configDiscord = new DiscordConfiguration(this);
		configDiscord.initialize();

		discordBot = new DiscordBot(this);

		configRoles = new RolesConfiguration(this);
		configRoles.initialize();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

		}, 0, 20 * 60 * 5 /*5 minutes*/);
	}

	@Override
	public final void onDisable() {
		discordBot.jda.shutdownNow();
	}

	/*@NotNull*/ String[] getGroups() {
		return permissionHook.getGroups();
	}

	/*@NotNull*/ String[] getGroups(@NotNull OfflinePlayer player) {
		return permissionHook.getPlayerGroups(null, player);
	}

	/*@NotNull*/ String getPrimaryGroup(@NotNull OfflinePlayer player) {
		return permissionHook.getPrimaryGroup(null, player);
	}

	@SuppressWarnings("ALL")
		/*@NotNull*/ String getGroupPrefix(@NotNull OfflinePlayer player) {
		if(chatHook != null) {
			String prefix = chatHook.getPlayerPrefix(null, player);

			if(prefix == null) {
				prefix = chatHook.getGroupPrefix((World) null, permissionHook.getPrimaryGroup(null, player));

				if(prefix == null) {
					if(player.isOnline()) {
						prefix = chatHook.getPlayerPrefix(player.getPlayer());
					}
				}
			}

			return prefix;
		} else {
			return getPrimaryGroup(player);
		}
	}

	private static Set<User> updatingUsers = new HashSet<>();

	void updateUserDiscordRoles(User user, boolean force) {
		final OfflinePlayer player = configPairs.getMinecraftPlayer(user.getId());
		if(player != null) {
			final String prefix = getGroupPrefix(player);
			if(prefix != null) {
				if(updatingUsers.contains(user) || force) {
					final Member member = discordBot.guild.getMember(user);

					if(member == null) {
						Bukkit.getLogger().severe("MinecraftDiscordBot: Invalid user member (" + user.getId() + ")!");
						return;
					}

					final String group = getPrimaryGroup(player);

					// Get Group Names
					final List<String> groups = Arrays.asList(getGroups(player));

					boolean nicknameChanged = false;

					// Remove roles
					for(final Role role : configRoles.roles) {
						final net.dv8tion.jda.api.entities.Role discordRole = discordBot.guild.getRoleById(role.id);

						if(discordRole == null) {
							Bukkit.getLogger().severe("MinecraftDiscordBot: Invalid role ID (" + role.id + ")!");
							continue;
						}

						if(groups.contains(role.name)) {
							discordBot.guild.addRoleToMember(member, discordRole).queue();

							if(group.equals(role.name)) {
								discordBot.guild.modifyNickname(member, prefix + ' ' + member.getUser().getName()).queue();
								nicknameChanged = true;
							}
						} else {
							discordBot.guild.removeRoleFromMember(member, discordRole).queue();
						}
					}

					if(!nicknameChanged) {
						discordBot.guild.modifyNickname(member, null).queue();
					}

					updatingUsers.remove(user);
				} else {
					updatingUsers.add(user);
				}
			}
		}
	}
}
