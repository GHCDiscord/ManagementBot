package ManagementBot.Content;

import net.dv8tion.jda.core.entities.User;

import java.io.Serializable;
import java.util.Date;

public final class IPEntry implements Serializable {
    private String name;
    private final String IP;
    private int miners;
    private int repopulation;
    private String description;
    private User addedBy;
    private String guildTag;
    private Date updatedAdd;

    public IPEntry(String IP) {
        this(IP, "");
    }
    public IPEntry(String IP, String name) {
        this.IP = IP;
        this.name = name;
        this.miners = 1;
        this.repopulation = 0;
        this.description = "";
        this.addedBy = null;
        this.guildTag = "";
        updatedAdd = new Date();
    }

    public int getMiners() {
        return miners;
    }

    public int getRepopulation() {
        return repopulation;
    }

    public String getDescription() {
        return description;
    }

    public String getIP() {
        return IP;
    }

    public String getName() {
        return name;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public String getGuildTag() {
        return guildTag;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setMiners(int miners) {
        this.miners = miners;
    }

    void setName(String name) {
        this.name = name;
    }

    void setRepopulation(int repopulation) {
        this.repopulation = repopulation;
    }

    void setUser(User addedBy) {
        this.addedBy = addedBy;
    }

    public void setGuildTag(String guildTag) {
        this.guildTag = guildTag;
    }
}
