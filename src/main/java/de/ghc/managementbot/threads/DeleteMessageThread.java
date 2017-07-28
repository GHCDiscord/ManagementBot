package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class DeleteMessageThread implements Runnable{
    int duration;
    Message message;

    public DeleteMessageThread(int duration, Message message) {
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
            try {
                message.delete().queue();
            }catch (PermissionException e) {
                Content.sendException(e, DeleteMessageThread.class);
            }
        }
    }
}
