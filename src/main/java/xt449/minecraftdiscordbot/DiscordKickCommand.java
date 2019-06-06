package xt449.minecraftdiscordbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class DiscordKickCommand extends Command {

	DiscordKickCommand() {
		this.name = "kick";
		this.aliases = new String[0];
		this.help = "Use this command to kick a user from the Discord server.";
		this.arguments = " <@User>";
		this.userPermissions = new Permission[] {Permission.ADMINISTRATOR};
	}

	@Override
	protected void execute(CommandEvent commandEvent) {
		if(commandEvent.getArgs().length() > 0) {
			final List<Member> members = commandEvent.getMessage().getMentionedMembers();
			if(members.size() > 0) {
				MinecraftDiscordBot.discordBot.guild.getController().kick(members.get(0)).queue();
			} else {
				commandEvent.reply(MinecraftDiscordBot.discordBot.getHelp(this));
			}
		} else {
			commandEvent.reply(MinecraftDiscordBot.discordBot.getHelp(this));
		}
	}
}
