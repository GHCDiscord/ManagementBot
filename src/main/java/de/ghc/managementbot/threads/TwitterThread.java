package de.ghc.managementbot.threads;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Secure;
import de.ghc.managementbot.entity.Registrable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TwitterThread implements Runnable, Registrable {

    private Date lastTweet;
    private Twitter twitter;

    private static List<TextChannel> channels = new ArrayList<>();
    private static TwitterThread instance;
    public static final String TOKEN = "TW";

    public TwitterThread() {
        lastTweet = new Date();
        instance = this;
    }

    @Override
    public void run() {
        synchronized (this) {
            ConfigurationBuilder builder = new ConfigurationBuilder()
                    .setDebugEnabled(true)
                    .setOAuthConsumerKey(Secure.ConsumerKey)
                    .setOAuthConsumerSecret(Secure.ConsumerSecret)
                    .setOAuthAccessToken(Secure.AccessToken)
                    .setOAuthAccessTokenSecret(Secure.AccesSecret);
            twitter = new TwitterFactory(builder.build()).getInstance();
            while (true) {
                try {
                    this.wait(60000);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    ResponseList<Status> response = twitter.getUserTimeline(811996248840531969L);
                    for (Status status : response) {
                        if (status.getCreatedAt().after(lastTweet)) {
                            lastTweet = status.getCreatedAt();
                            String content = status.getText();
                            if (content.contains("maintenance"))
                                Content.getGhc().getTextChannelById(Data.general).sendMessage( new MessageBuilder().append(Content.getGhc().getPublicRole()).append(" Achtung: Wartungsarbeiten!").build()).queue();
                            for (TextChannel channel : channels)
                                channel.sendMessage(new EmbedBuilder().setColor(new Color(53, 137, 255))
                                    .setAuthor(status.getUser().getScreenName(), status.getUser().getURL(), status.getUser().getProfileImageURL())
                                    .setDescription(status.getText())
                                    .setImage(status.getMediaEntities().length > 0 ? status.getMediaEntities()[0].getType().equals("photo")? status.getMediaEntities()[0].getMediaURL() : null : null)
                                    .setThumbnail(status.getUser().getProfileBannerURL())
                                    .setFooter("Tweet gesendet: " + Content.formatDate(status.getCreatedAt()), Content.GHCImageURL).build()).queue();
                        }
                    }
                }catch (TwitterException e) {
                    Content.sendException(e, TwitterThread.class);
                }
            }
        }
    }

    @Override
    public void addChannel(TextChannel channel) {
        channels.add(channel);
    }

    @Override
    public void removeChannel(TextChannel channel) {
        channels.remove(channel);
    }

    public static TwitterThread getInstance() {
        return instance;
    }

    @Override
    public List<TextChannel> getChannels() {
        return channels;
    }

    @Override
    public String getToken() {
        return TOKEN;
    }
}
