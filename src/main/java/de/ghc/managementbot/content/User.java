package de.ghc.managementbot.content;

import java.util.Random;

public class User {

    enum Role {
        Admin, Moderator, User
    }

    private String Username;
    private String Password;
    private String EMail;
    private Role Role;
    private net.dv8tion.jda.core.entities.User discordUser;

    public User (String username) {
        Username = username;
        Password = generatePassword();
        EMail = "";
        Role = Role.User;
    }

    private String generatePassword() {
        Random rand = new Random();
        int lenght = 5 + rand.nextInt(6);
        StringBuilder pw = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
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

    public User.Role getRole() {
        return Role;
    }

    public String getEMail() {
        return EMail;
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

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }
}
