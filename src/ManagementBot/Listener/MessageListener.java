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

		if (msg.equalsIgnoreCase("!stats")) {
			channel.sendMessage(Content.getStats()).queue();
		} else if (msg.equalsIgnoreCase(".c3po")) { //verify
			Content.verify(event);
		}
		
		//TODO
	}
}
