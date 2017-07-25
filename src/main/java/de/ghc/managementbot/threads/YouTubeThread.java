package de.ghc.managementbot.threads;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Secure;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class YouTubeThread implements Runnable{

    private long lastVideo = System.currentTimeMillis();
    private long lastLiveStream = System.currentTimeMillis();
    private YouTube youTube;
    private Channel okitoo;

    @Override
    public void run() {
        youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
                    }).setApplicationName("GHC Bot").build();
        try {
            YouTube.Channels.List channels = youTube.channels().list("id, snippet");
            channels.setId("UCC_ds4x9Iv3tcvKi-JdQ-Qw");
            channels.setKey(Secure.YouTubeKey);

            List<Channel> channelResponse = channels.execute().getItems();
            okitoo = channelResponse.get(0);
        } catch (IOException e) {
            Content.getGhc().getTextChannelById(Data.botLog).sendMessage("YouTubeThread: IOException: " + e.getLocalizedMessage()).queue();
        }
        while (true) {
            try {
                YouTube.Search.List search = makeSearchRequest();

                List<SearchResult> results = search.execute().getItems();

                for (SearchResult result : results) {
                    if (result.getSnippet().getPublishedAt().getValue() > lastVideo) {
                        lastVideo = result.getSnippet().getPublishedAt().getValue();
                        sendMessage("Video hochgeladen: ", result);
                    }
                }

                YouTube.Search.List live = makeSearchRequest();
                live.setEventType("live");

                List<SearchResult> resultsLive = search.execute().getItems();

                for (SearchResult result : resultsLive) {
                    if (result.getSnippet().getPublishedAt().getValue() > lastLiveStream) {
                        lastLiveStream = result.getSnippet().getPublishedAt().getValue();
                        sendMessage("Livestream gestartet: ", result);
                    }
                }

                synchronized (this) {
                    this.wait(60001);
                }
            } catch (IOException e) {
                Content.getGhc().getTextChannelById(Data.botLog).sendMessage("YouTubeThread: IOException: " + e.getLocalizedMessage()).queue();
                synchronized (this) {
                    try {
                        this.wait(30000);
                    } catch (InterruptedException e1) {
                        return;
                    }
                }
            } catch (InterruptedException ignore) {
                return;
            }
        }
    }

    private void sendMessage(String footer, SearchResult result) {
        Content.getGhc().getTextChannelById(Data.general).sendMessage(new EmbedBuilder()
                .setAuthor(result.getSnippet().getChannelTitle(), "https://www.youtube.com/channel/UCC_ds4x9Iv3tcvKi-JdQ-Qw", okitoo.getSnippet().getThumbnails().getDefault().getUrl())
                .setTitle(result.getSnippet().getTitle(), "https://youtube.com/watch?v=" + result.getId().getVideoId())
                .setDescription(result.getSnippet().getDescription())
                .setImage(result.getSnippet().getThumbnails().getDefault().getUrl())
                .setColor(Color.RED)
                .setFooter(footer + Content.formatDate(result.getSnippet().getPublishedAt()), Content.GHCImageURL)
                .setThumbnail("https://yt3.ggpht.com/OF3m9O73nRiHTCfP1kG7HDOnPHvIt8FCqEuamB7_Ia9BSLz8AAJVlY_Hb92BRNXz-CoNA3Ai")
                .build()
        ).queue();
    }

    private YouTube.Search.List makeSearchRequest() throws IOException {
        YouTube.Search.List request = youTube.search().list("id, snippet");
        request.setKey(Secure.YouTubeKey);
        request.setChannelId("UCC_ds4x9Iv3tcvKi-JdQ-Qw");
        request.setType("video");
        request.setMaxResults(10L);
        return request;
    }
}