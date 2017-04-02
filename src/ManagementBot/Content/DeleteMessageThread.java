package ManagementBot.Content;

import net.dv8tion.jda.core.entities.Message;

/**
 * Created by Schule on 02.04.2017.
 */
public class DeleteMessageThread implements Runnable{
    int duration;
    Message message;

    DeleteMessageThread(int duration, Message message) {
        this.duration = duration;
        this.message = message;
    }

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
            message.deleteMessage().queue();
        }
    }
}
