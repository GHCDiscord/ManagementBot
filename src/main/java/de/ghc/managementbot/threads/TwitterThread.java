package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Secure;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.util.Date;

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
                            String content = status.getText();
                            if (content.contains("maintenance"))
                                Content.getGhc().getTextChannelById(Data.general).sendMessage( new MessageBuilder().append(Content.getGhc().getPublicRole()).append(" Achtung: Wartungsarbeiten!").build()).queue();
                            Content.getGhc().getTextChannelById(Data.general).sendMessage(new EmbedBuilder().setColor(new Color(53, 137, 255))
                                    .setAuthor(status.getUser().getScreenName(), status.getUser().getURL(), status.getUser().getProfileImageURL())
                                    .setDescription(status.getText())
                                    .setImage(status.getMediaEntities().length > 0 ? status.getMediaEntities()[0].getType().equals("photo")? status.getMediaEntities()[0].getMediaURL() : null : null)
                                    .setThumbnail(status.getUser().getProfileBannerURL())
                                    .setFooter("Tweet gesendet: " + Content.formatDate(status.getCreatedAt()), Content.GHCImageURL).build()).queue();
                        }
                    }
                }catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
