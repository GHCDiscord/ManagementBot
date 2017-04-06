package de.ghc.managementbot.content;

abstract class AddIP extends Command {

    static boolean checkIP(String s) {
        String[] numbers = s.split("\\.");
        if (numbers.length != 4)
            return false;
        try {
            for (String strg : numbers) {
                Integer.parseInt(strg);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected static int setGuild(int i, String[] command, IPEntry entry) {
        Guild guild = new Guild(command[++i]);
        if (command[++i].equalsIgnoreCase("-gn"))
            guild.setName(command[++i]);
        else if (command[i].equalsIgnoreCase("-gk"))
            guild.setKey(command[++i]);
        else
            i--;
        entry.setGuild(guild);
        return i;
    }

    protected static void addIPtoDB(IPEntry entry) {
        //TODO
    }
}
