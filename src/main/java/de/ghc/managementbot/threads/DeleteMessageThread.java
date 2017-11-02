package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class DeleteMessageThread implements Runnable{
    private final int duration;
    private final Message message;

    public DeleteMessageThread(int duration, Message message) {
        this.duration = duration;
        this.message = message;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            return;
        } finally {
            try {
                message.delete().queue();
            }catch (PermissionException e) {
                Content.sendException(e, DeleteMessageThread.class);
            }
        }
    }
}
