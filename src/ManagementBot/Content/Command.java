package ManagementBot.Content;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    void onMessageReceived(MessageReceivedEvent event);
}
