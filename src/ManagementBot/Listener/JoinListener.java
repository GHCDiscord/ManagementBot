package ManagementBot.Listener;

import ManagementBot.Content.Content;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class JoinListener extends ListenerAdapter{

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel gerneral = event.getGuild().getTextChannelsByName("justcodingthings", true).get(0);
        gerneral.sendMessage(Content.getJoinMessage(event)).queue();
        Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRolesByName("Neuling", true).get(0));

    }
}
