package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.CountryRoleManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DeleteRole extends CountryRoleManager implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        roleCommandExecution(event, Content::removeRoles);
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!removerole", "!deleterole");
    }

    @Override
    public boolean isCalled(String msg) {
        List<String> callers = getCallers();
        callers.replaceAll(String::toLowerCase);
        return callers.contains(msg.toLowerCase().split(" ")[0]);
    }
}
