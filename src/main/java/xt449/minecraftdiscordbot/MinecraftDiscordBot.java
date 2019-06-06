package xt449.minecraftdiscordbot;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
public class MinecraftDiscordBot extends JavaPlugin {

	static PairsConfiguration configPairs;
	static PendingPairsConfiguration configPendingPairs;
	static DiscordConfiguration configDiscord;
	private static RolesConfiguration configRoles;

	static DiscordBot discordBot;

	Permission permissions;

	@Override
	public final void onLoad() {
		final RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if(rsp != null) {
			permissions = rsp.getProvider();
		} else {
			getLogger().severe("Error initializing Vault permissions hook!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		// code
	}

	@Override
	public final void onEnable() {
		configPairs = new PairsConfiguration(this);
		configPairs.initialize();

		configPendingPairs = new PendingPairsConfiguration(this);
		configPendingPairs.initialize();

		getCommand("pair").setExecutor(new MinecraftPairCommand());

		getCommand("syncroles").setExecutor((sender, command, label, args) -> {
			Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
				for(final User user : luckPerms.getUsers().parallelStream().map(u -> configPairs.getDiscordUser(u.getUuid())).filter(Objects::nonNull).collect(Collectors.toList())) {
					updateUserDiscordRoles(user, true);
				}

				sender.sendMessage("All paired accounts have had their roles updated!");
			});

			return true;
		});

		luckPerms = LuckPerms.getApi();
		contextManager = luckPerms.getContextManager();

		luckPerms.getEventBus().subscribe(me.lucko.luckperms.api.event.user.UserDataRecalculateEvent.class, (event) -> {
			final net.dv8tion.jda.core.entities.User user = configPairs.getDiscordUser(event.getUser().getUuid());
			if(user != null) {
				updateUserDiscordRoles(user, false);
			}
		});

		configDiscord = new DiscordConfiguration(this);
		configDiscord.initialize();

		discordBot = new DiscordBot();

		configRoles = new RolesConfiguration(this);
		configRoles.initialize();
	}

	@Override
	public final void onDisable() {
		discordBot.jda.shutdownNow();
	}

	private static Set<User> updatingUsers = new HashSet<>();

	static void updateUserDiscordRoles(User user, boolean force) {
		final OfflinePlayer minecraftPlayer = configPairs.getMinecraftPlayer(user.getId());
		if(minecraftPlayer != null) {
			final me.lucko.luckperms.api.User permsUser = luckPerms.getUser(minecraftPlayer.getUniqueId());
			if(permsUser != null) {
				if(updatingUsers.contains(user) || force) {
					final Member member = discordBot.guild.getMember(user);

					// Get Group Nodes
					final List<String> groups = permsUser.getPermissions().parallelStream().filter(Node::isGroupNode).map(Node::getGroupName).collect(Collectors.toList());

					boolean nicknameChanged = false;

					// Remove roles
					for(final Role role : configRoles.roles) {
						if(groups.contains(role.name)) {
							discordBot.guild.getController().addSingleRoleToMember(member, discordBot.guild.getRoleById(role.id)).queue();

							if(permsUser.getPrimaryGroup().equals(role.name)) {
								discordBot.guild.getController().setNickname(member, role.group.getCachedData().getMetaData(contextManager.getStaticContexts()).getPrefix() + ' ' + member.getUser().getName()).queue();
								nicknameChanged = true;
							}
						} else {
							discordBot.guild.getController().removeSingleRoleFromMember(member, discordBot.guild.getRoleById(role.id)).queue();
						}
					}

					if(!nicknameChanged) {
						discordBot.guild.getController().setNickname(member, null).queue();
					}

					updatingUsers.remove(user);
				} else {
					updatingUsers.add(user);
				}
			}
		}
	}
}
