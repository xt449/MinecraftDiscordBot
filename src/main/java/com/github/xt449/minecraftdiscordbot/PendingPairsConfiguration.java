package com.github.xt449.minecraftdiscordbot;

import com.github.xt449.bukkitutilitylibrary.AbstractConfiguration;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class PendingPairsConfiguration extends AbstractConfiguration {

	private final String path_discord_pairs = "discord";
	private final String path_minecraft_pairs = "minecraft";

	private final MinecraftDiscordBot minecraftDiscordBot;

	private Map<String, UUID> discord_pairs;
	private Map<UUID, String> minecraft_pairs;

	PendingPairsConfiguration(MinecraftDiscordBot plugin) {
		super(plugin, "pending.db");

		minecraftDiscordBot = plugin;
	}

	@Override
	protected void setDefaults() {
		config.createSection(path_discord_pairs);
		config.createSection(path_minecraft_pairs);
	}

	@Override
	protected void getValues() {
		discord_pairs = config.getConfigurationSection(path_discord_pairs).getValues(false).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> UUID.fromString((String) entry.getValue())));
		minecraft_pairs = config.getConfigurationSection(path_discord_pairs).getValues(false).entrySet().stream().collect(Collectors.toMap(entry -> UUID.fromString(entry.getKey()), entry -> (String) entry.getValue()));
	}

	boolean pairFromMinecraft(OfflinePlayer player, User user) {
		final UUID playerID = player.getUniqueId();
		final String userID = user.getId();

		if(discord_pairs.containsKey(userID) && discord_pairs.get(userID).equals(playerID)) {
			minecraftDiscordBot.configPairs.pair(player, user);
			config.getConfigurationSection(path_discord_pairs).set(userID, null);
			discord_pairs.remove(userID);
			save();
			return true;
		} else {
			config.getConfigurationSection(path_minecraft_pairs).set(playerID.toString(), userID);
			minecraft_pairs.put(playerID, userID);
			save();
			return false;
		}

		//save();
		//readValues();
	}

	boolean pairFromDiscord(OfflinePlayer player, User user) {
		final UUID playerID = player.getUniqueId();
		final String userID = user.getId();

		if(minecraft_pairs.containsKey(playerID) && minecraft_pairs.get(playerID).equals(userID)) {
			minecraftDiscordBot.configPairs.pair(player, user);
			config.getConfigurationSection(path_minecraft_pairs).set(playerID.toString(), null);
			minecraft_pairs.remove(playerID);
			save();
			return true;
		} else {
			config.getConfigurationSection(path_discord_pairs).set(userID, playerID.toString());
			discord_pairs.put(userID, playerID);
			save();
			return false;
		}

		//save();
		//readValues();
	}
}
