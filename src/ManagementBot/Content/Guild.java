package ManagementBot.Content;

public class Guild {
    String name;
    String key;

    Guild() {
        this("", "");
    }

    Guild(String key, String name) {
        this.key = key;
        this.name = name;
    }

    Guild(String key) {
       this(key, key);
    }

    void setName(String name) {
        this.name = name;
    }

    void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key + ": " + name;
    }
}
