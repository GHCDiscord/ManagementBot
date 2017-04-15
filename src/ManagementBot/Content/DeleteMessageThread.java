package ManagementBot.Content;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class DeleteMessageThread implements Runnable{
    int duration;
    Message message;

    /*
    TextChannel channel;
    Collection<Message> messages;
    */

    DeleteMessageThread(int duration, Message message) {
        this.duration = duration;
        this.message = message;
    }

    /*
    DeleteMessageThread(int duration, Collection<Message> messages, TextChannel channel) {
        this.duration = duration;
        this.messages = messages;
        this.channel = channel;
    }
     */

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (duration > 0) {
                    this.wait(1000);
                    duration--;
                }
            }
        } catch (InterruptedException e) {
        }finally {
            try {
                message.deleteMessage().queue();
                //channel.deleteMessages(messages).queue();
            }catch (PermissionException e) {
                e.printStackTrace();
            }
        }
    }
}
