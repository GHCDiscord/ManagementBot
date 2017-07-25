package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.JDA;

public class StartupThread implements Runnable {

    private final JDA jda;

    public StartupThread(JDA jda) {
        this.jda = jda;
    }
    @Override
    public void run() {
        jda.getGuildById(Data.test).getTextChannelById(Data.saves).getHistory().getRetrievedHistory().forEach(m -> {
            String[] message = m.getContent().split(Data.SPLITTER);
            if (message.length == 2) {
                if (message[0].equals(YouTubeThread.TOKEN))
                    YouTubeThread.getInstance().addChannel(jda.getTextChannelById(message[1]));
                if (message[0].equals(TwitterThread.TOKEN))
                    TwitterThread.getInstance().addChannel(jda.getTextChannelById(message[1]));
                if (message[0].equals(MarketAPIThread.TOKEN))
                    MarketAPIThread.getInstance().addChannel(jda.getTextChannelById(message[1]));
            }
        });
    }
}
