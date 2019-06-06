package xt449.minecraftdiscordbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class DiscordPairCommand extends Command {

	DiscordPairCommand() {
		this.name = "pair";
		this.aliases = new String[] {"sync"};
		this.help = "Use this command to pair your Minecraft and Discord accounts";
		this.arguments = " <Minecraft username>";
		//this.userPermissions = Permission.EMPTY_PERMISSIONS;
	}

	@Override
	protected void execute(CommandEvent commandEvent) {
		if(commandEvent.getArgs().length() > 0) {
			for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
				if(player.getName().equalsIgnoreCase(commandEvent.getArgs().split(" ")[0])) {
					if(MinecraftDiscordBot.configPairs.pairs.containsKey(player.getUniqueId())) {
						commandEvent.replySuccess("Your Discord and Minecraft accounts are already paired!");
						return;
					}

					boolean complete = MinecraftDiscordBot.configPendingPairs.pairFromDiscord(player, commandEvent.getAuthor());

					if(complete) {
						commandEvent.replySuccess("Your Discord and Minecraft accounts are now paired!");
					} else {
						commandEvent.replySuccess("Finish pairing your accounts by using `/pair " + commandEvent.getAuthor().getAsTag() + "` on the Minecraft server!");
					}

					return;
				}
			}

			commandEvent.replyError("Unknown username. Try logging onto the Minecraft server first!");
		} else {
			commandEvent.reply('`' + commandEvent.getClient().getPrefix() + name + arguments + "` - " + help);
		}
	}
}
