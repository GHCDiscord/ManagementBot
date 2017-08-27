package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.Registrable;
import de.ghc.managementbot.threads.MarketAPIThread;
import de.ghc.managementbot.threads.TwitterThread;
import de.ghc.managementbot.threads.YouTubeThread;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterChannel implements Command {

    private static final List<String> YOUTUBE_STRINGS = Arrays.asList("!requestyoutubeupdates", "!registeryoutubeupdates");
    private static final List<String> TWITTER_STRINGS = Arrays.asList("!requesttwitterupdates", "!registertwitterupdates");
    private static final List<String> PLAY_STRINGS = Arrays.asList("!requestgplayupdates", "!registergplayupdates", "!requestplayupdates", "!registerplayupdates", "!requestgoogleplayupdates", "!registergoogleplayupdates");

    private Registrable registrable;
    public RegisterChannel(Registrable registrable) {
        this.registrable = registrable;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
            event.getMessage().getMentionedChannels().forEach(ch -> {
                if (!registrable.getChannels().contains(ch)) {
                    registrable.addChannel(ch);
                    Content.getJda().getTextChannelById(Data.Channel.saves).sendMessage(registrable.getToken() + Data.SPLITTER + ch.getId()).queue();
                }
            });
    }

    @Override
    public List<String> getCallers() {
        List<String> callers = new ArrayList<>(YOUTUBE_STRINGS);
        callers.addAll(TWITTER_STRINGS);
        callers.addAll(PLAY_STRINGS);
        return callers;
    }

    @Override
    public boolean isCalled(String msg) {
        List<String> callers = getCallers();
        return callers.contains(msg.toLowerCase().split(" ")[0]);
    }

    @Override
    public Command createCommand(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent().split(" ")[0].toLowerCase();

        if (YOUTUBE_STRINGS.contains(msg))
            return new RegisterChannel(YouTubeThread.getInstance());
        if (TWITTER_STRINGS.contains(msg))
            return new RegisterChannel(TwitterThread.getInstance());
        if (PLAY_STRINGS.contains(msg))
            return new RegisterChannel(MarketAPIThread.getInstance());
        return Content.doNothing;
    }
}
