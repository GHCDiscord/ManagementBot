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

    protected static void addIPtoDB(IPEntry entry) {
        //TODO
    }
}
