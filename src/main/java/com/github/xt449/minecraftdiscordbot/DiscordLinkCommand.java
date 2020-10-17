package com.github.xt449.minecraftdiscordbot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.UUID;

/**
 * @author xt449 / BinaryBanana
 */
abstract class DiscordLinkCommand {

	/*@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		event.getGuild().loadMembers().onSuccess(list -> System.out.println("Discord Guild Member list cached!"));
	}*/

	public static void execute(GuildMessageReceivedEvent event) {
		// ignore all messages not in the #bot-commands channel
		if(event.getChannel().getIdLong() != 743810987714543676L) {
			return;
		}

		// ignore messages from bots
		if(event.getAuthor().isBot()) {
			return;
		}

		final String content = event.getMessage().getContentRaw();
		// ignore messages without text content (files, system messages, embeds)
		if(content.length() == 0) {
			return;
		}

		if(DiscordBot.COMMAND_PREFIX == content.charAt(0)) {
			final String[] parts = content.split(" ");

			if(parts[0].substring(1).equalsIgnoreCase("link")) {
				if(parts.length == 2) {
					try {
						if(AccountLinking.finishLinking(UUID.fromString(parts[1]), event.getAuthor().getId())) {
							event.getMessage().delete().queue();
							event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Your Discord and Minecraft accounts have been linked.").queue());
						} else {
							event.getMessage().delete().queue();
							event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You must begin the linking process by running the command `/link " + event.getAuthor().getAsTag() + "` in the Minecraft server.").queue());
						}
					} catch(IllegalArgumentException exc) {
						event.getMessage().delete().queue();
						event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Invalid Minecraft UUID!").queue());
					}
				} else {
					event.getMessage().delete().queue();
					event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("To begin the linking process, run the command `/link " + event.getAuthor().getAsTag() + "` in the Minecraft server.").queue());
				}
			}
		}
	}
}
