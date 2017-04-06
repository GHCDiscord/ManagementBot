package ManagementBot.Content;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    public abstract void onMessageReceived(MessageReceivedEvent event);
}
