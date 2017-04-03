package ManagementBot.Listener;

import ManagementBot.Content.Content;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		String msg = message.getContent();
		String[] command = msg.split(" ");

		//output
		/*if (event.getGuild() == null) {
			System.out.printf("[Priv][%s] %s: %s\n", event.getChannel().getName(), event.getAuthor().getName(), msg);
		} else {
			System.out.printf("[%s][%s] %s: %s \n", event.getGuild().getName(),
					event.getChannel().getName(), event.getAuthor().getName(), msg);
		} */

		//handle Messages
		if (msg.equalsIgnoreCase("!stats")) {
			channel.sendMessage(Content.getStats()).queue();
		} else if (msg.equalsIgnoreCase(".c3po")) { //verify
			Content.verify(event);
		} else if (msg.equalsIgnoreCase("!topGuilds")) {
			channel.sendMessage(Content.getTopGuilds()).queue();
		} else if (msg.equalsIgnoreCase("!help")) {
			Content.sendhelpMessage(event.getAuthor(), event.getMember());
		} else if (command[0].equalsIgnoreCase("!regeln") || command[0].equalsIgnoreCase("!rules")) {
			Content.rules(event);
		} else if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
			Content.tutorial(event);
		}
	}
}
