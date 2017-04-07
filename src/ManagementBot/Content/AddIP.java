package ManagementBot.Content;

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
        if (command[i].equalsIgnoreCase("-g")) {
            Guild guild = new Guild();
            for (i++; i < command.length; i++) {
                if (command[i].equalsIgnoreCase("-gn"))
                    guild.setName(command[++i]);
                else if (command[i].equalsIgnoreCase("-gk"))
                    guild.setKey(command[++i]);
                else
                    break;
            }
            entry.setGuild(guild);
        }
        return i;
    }

    protected static void addIPtoDB(IPEntry entry) {
        //TODO
    }
}
