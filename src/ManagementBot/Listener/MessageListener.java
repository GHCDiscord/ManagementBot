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
		
		//TODO
	}
}
