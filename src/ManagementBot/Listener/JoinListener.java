package ManagementBot.Listener;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        /*TextChannel general = event.getGuild().getTextChannelsByName("general", true).get(0);
        general.sendMessage(new MessageBuilder().append("Willkommen, ")
                .append(event.getMember())
                .append(". Bevor du uns Fragen vorlegst, wirf bitte unbedingt zunächst einen Blick in die ")
                .append(event.getGuild().getTextChannelsByName("regeln", true).get(0))
                .append("\nViel Spaß bei der GHC | German Hackers Community")
                .build();).queue();
        Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("Neuling", true).get(0)); */
    }
}
