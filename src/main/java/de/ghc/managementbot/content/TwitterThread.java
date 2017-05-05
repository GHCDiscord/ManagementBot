package de.ghc.managementbot.content;

import net.dv8tion.jda.core.EmbedBuilder;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.util.*;

public class TwitterThread implements Runnable {

    private Date lastTweet;

    public TwitterThread() {
        lastTweet = new Date();
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    this.wait(60000);
                } catch (InterruptedException ignore) {}
                ConfigurationBuilder builder = new ConfigurationBuilder()
                        .setDebugEnabled(true)
                        .setOAuthConsumerKey(Secure.ConsumerKey)
                        .setOAuthConsumerSecret(Secure.ConsumerSecret)
                        .setOAuthAccessToken(Secure.AccessToken)
                        .setOAuthAccessTokenSecret(Secure.AccesSecret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance();
                try {
                    ResponseList<Status> response = twitter.getUserTimeline(811996248840531969L);
                    for (Status status : response) {
                        if (status.getCreatedAt().after(lastTweet)) {
                            lastTweet = status.getCreatedAt();
                            Content.getGhc().getTextChannelById(269150030965768193L).sendMessage(new EmbedBuilder().setColor(new Color(53, 137, 255))
                                    .setAuthor(status.getUser().getScreenName(), status.getUser().getURL(), status.getUser().getProfileImageURL())
                                    .setDescription(status.getText())
                                    .setThumbnail(status.getUser().getProfileBannerURL())
                                    .setFooter("Tweet gesendet: " + status.getCreatedAt(), "https://avatars0.githubusercontent.com/u/26769965?v=3&s=200").build()).queue();
                        }
                    }
                }catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
