package ManagementBot.Listener;

import ManagementBot.Content.Content;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String msg = event.getMessage().getContent();
		//output
		if (event.getGuild() == null) {
			System.out.printf("[Priv][%s] %s: %s\n", event.getChannel().getName(), event.getAuthor().getName(), msg);
		} else {
			System.out.printf("[%s][%s] %s: %s \n", event.getGuild().getName(),
					event.getChannel().getName(), event.getAuthor().getName(), msg);
		}

		Content.startCommand(event);
	}
}
