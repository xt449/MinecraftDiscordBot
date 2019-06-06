package xt449.minecraftdiscordbot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import xt449.bukkitutilitylibrary.AbstractConfiguration;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class PairsConfiguration extends AbstractConfiguration {

	private final MinecraftDiscordBot minecraftDiscordBot;

	BiMap<UUID, String> pairs;

	PairsConfiguration(MinecraftDiscordBot plugin) {
		super(plugin, "pairs.db");

		minecraftDiscordBot = plugin;
	}

	@Override
	protected void setDefaults() {
		/*UUID uuid = UUID.randomUUID();
		config.addDefault(uuid.toString(), 164126051795L);
		config.addDefault("164126051795", uuid);*/
	}

	@Override
	protected void getValues() {
		pairs = HashBiMap.create(config.getValues(false).entrySet().stream().collect(Collectors.toMap(entry -> UUID.fromString(entry.getKey()), entry -> (String) entry.getValue())));
	}

	@Nullable
	String getDiscordId(UUID id) {
		try {
			return pairs.get(id);
		} catch(NullPointerException exc) {
			return null;
		}
	}

	@Nullable
	User getDiscordUser(UUID id) {
		final String discordId = getDiscordId(id);
		if(discordId != null) {
			try {
				return minecraftDiscordBot.discordBot.jda.getUserById(discordId);
			} catch(NullPointerException exc) {
				return null;
			}
		} else {
			return null;
		}
	}

	@Nullable
	UUID getMinecraftId(String id) {
		try {
			return pairs.inverse().get(id);
		} catch(NullPointerException exc) {
			return null;
		}
	}

	@Nullable
	OfflinePlayer getMinecraftPlayer(String id) {
		final UUID minecraftID = getMinecraftId(id);
		if(minecraftID != null) {
			try {
				return Bukkit.getOfflinePlayer(minecraftID);
			} catch(NullPointerException exc) {
				return null;
			}
		} else {
			return null;
		}
	}

	void pair(OfflinePlayer player, User user) {
		pairs.put(player.getUniqueId(), user.getId());
		config.set(player.getUniqueId().toString(), user.getId());

		minecraftDiscordBot.updateUserDiscordRoles(user, true);

		save();
		//readValues();
	}
}
