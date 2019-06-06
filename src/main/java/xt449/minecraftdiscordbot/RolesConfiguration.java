package xt449.minecraftdiscordbot;

import me.lucko.luckperms.api.Group;
import org.bukkit.plugin.Plugin;
import xt449.bukkitutilitylibrary.AbstractConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class RolesConfiguration extends AbstractConfiguration {

	private final String path_prefix = "prefix";
	private final String path_color = "color";
	private final String path_id = "id";

	List<Role> roles = new ArrayList<>();

	RolesConfiguration(Plugin plugin) {
		super(plugin, "roles.yml");
	}

	@Override
	public void setDefaults() {
		for(Group group : MinecraftDiscordBot.luckPerms.getGroups()) {
			final String name = group.getName();
			if(config.get(name) == null) {
				final List<net.dv8tion.jda.core.entities.Role> discordRoles = MinecraftDiscordBot.discordBot.guild.getRolesByName(name, true);
				if(discordRoles.size() > 0) {
					config.addDefault(name + '.' + path_id, discordRoles.get(0).getIdLong());
				}
			}
		}

		/*config.addDefault("thing2." + path_color, ChatColor.GOLD.name());

		final ConfigurationSection section = config.createSection("thing");
		section.addDefault(path_color, ChatColor.BLUE.name());
		config.addDefault("thing", section);*/

		//config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(filePath))));
	}

	@Override
	public void getValues() {
		for(String name : config.getKeys(false)) {
			final Group group = MinecraftDiscordBot.luckPerms.getGroup(name);

			if(group == null) {
				plugin.getLogger().warning("Unrecognized group name in " + filePath + "! " + name + " is not found in LuckPerms.");
			} else {
				//final String prefix = group.getCachedData().getMetaData(MinecraftDiscordBot.contextManager.getApplicableContexts(group)).getPrefix();
				long id = config.getLong(name + '.' + path_id);
				//ChatColor color;

				//Role role = new Role(name, id);

				roles.add(new Role(name, id, group));
			}
		}
	}
}
