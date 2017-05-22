package de.ghc.managementbot.threads;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Secure;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class YouTubeThread implements Runnable{

    private long lastVideo = System.currentTimeMillis();
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
            e.printStackTrace();
        }
        try {
            while (true) {
                YouTube.Search.List search = youTube.search().list("id, snippet");
                search.setKey(Secure.YouTubeKey);
                search.setChannelId("UCC_ds4x9Iv3tcvKi-JdQ-Qw");
                search.setType("video");
                search.setMaxResults(10L);

                List<SearchResult> results = search.execute().getItems();

                for (SearchResult result : results) {
                    if (result.getSnippet().getPublishedAt().getValue() > lastVideo) {
                        lastVideo = result.getSnippet().getPublishedAt().getValue();
                        Content.getGhc().getTextChannelById(269150030965768193L).sendMessage(new EmbedBuilder()
                                .setAuthor(result.getSnippet().getChannelTitle(), "https://www.youtube.com/channel/UCC_ds4x9Iv3tcvKi-JdQ-Qw", okitoo.getSnippet().getThumbnails().getDefault().getUrl())
                                .setTitle(result.getSnippet().getTitle(), "https://youtube.com/watch?v=" + result.getId().getVideoId())
                                .setDescription(result.getSnippet().getDescription())
                                .setImage(result.getSnippet().getThumbnails().getDefault().getUrl())
                                .setColor(new Color(255, 44, 52))
                                .setFooter("Video hochgeladen: " + Content.formatDate(result.getSnippet().getPublishedAt()), Content.GHCImageURL)
                                .setThumbnail("https://yt3.ggpht.com/OF3m9O73nRiHTCfP1kG7HDOnPHvIt8FCqEuamB7_Ia9BSLz8AAJVlY_Hb92BRNXz-CoNA3Ai=w1920-fcrop64=1,00000000ffffffff-nd-c0xffffffff-rj-k-no")
                                .build()
                        ).queue();
                    }
                }
                synchronized (this) {
                    this.wait(60000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ignore) {}
    }
}