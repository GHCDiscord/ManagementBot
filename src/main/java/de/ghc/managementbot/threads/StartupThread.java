package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Guild;

public class StartupThread implements Runnable {

    private final Guild ghc;

    public StartupThread(Guild ghc) {
        this.ghc = ghc;
    }
    @Override
    public void run() {
        ghc.getTextChannelById(Data.Channel.saves).getHistory().getRetrievedHistory().forEach(m -> {
            String[] message = m.getContent().split(Data.SPLITTER);
            if (message.length == 2) {
                switch (message[0]) {
                    case YouTubeThread.TOKEN:
                        YouTubeThread.getInstance().addChannel(ghc.getTextChannelById(message[1]));
                        break;
                    case TwitterThread.TOKEN:
                        TwitterThread.getInstance().addChannel(ghc.getTextChannelById(message[1]));
                        break;
                    case MarketAPIThread.TOKEN:
                        MarketAPIThread.getInstance().addChannel(ghc.getTextChannelById(message[1]));
                        break;
                }
            }
        });
    }
}
