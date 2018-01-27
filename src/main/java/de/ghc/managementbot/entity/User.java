package de.ghc.managementbot.entity;

import java.util.Random;

public class User {

    private String Username;
    private String Password;
    private net.dv8tion.jda.core.entities.User discordUser;

    public User (String username) {
        Username = username;
        Password = generatePassword();
    }

    private String generatePassword() {
        Random rand = new Random();
        int length = 5 + rand.nextInt(6);
        StringBuilder pw = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int type = rand.nextInt(3);
            switch (type) {
                case 0:
                    pw.append((char) (65 + rand.nextInt(26)));
                    break;
                case 1:
                    pw.append((char) (97 + rand.nextInt(26)));
                    break;
                case 2:
                    pw.append(rand.nextInt(10));
                    break;
            }
        }
        return pw.toString();
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public net.dv8tion.jda.core.entities.User getDiscordUser() {
        return discordUser;
    }

    public void setDiscordUser(net.dv8tion.jda.core.entities.User discordUser) {
        this.discordUser = discordUser;
    }
}
