package xt449.minecraftdiscordbot;

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

	private final MinecraftDiscordBot minecraftDiscordBot;

	List<Role> roles = new ArrayList<>();

	RolesConfiguration(MinecraftDiscordBot plugin) {
		super(plugin, "roles.yml");

		minecraftDiscordBot = plugin;
	}

	@Override
	public void setDefaults() {
		for(String group : minecraftDiscordBot.getGroups()) {
			if(config.get(group) == null) {
				final List<net.dv8tion.jda.api.entities.Role> discordRoles = minecraftDiscordBot.discordBot.guild.getRolesByName(group, true);
				if(discordRoles.size() > 0) {
					config.addDefault(group + '.' + path_id, discordRoles.get(0).getIdLong());
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
			/*if(group == null) {
				plugin.getLogger().warning("Unrecognized group name in " + filePath + "! " + name + " is not found in LuckPerms.");
			} else {*/
			//final String prefix = group.getCachedData().getMetaData(MinecraftDiscordBot.contextManager.getApplicableContexts(group)).getPrefix();
			long id = config.getLong(name + '.' + path_id);
			//ChatColor color;

			//Role role = new Role(name, id);

			roles.add(new Role(name, id));
			/*}*/
		}
	}
}
