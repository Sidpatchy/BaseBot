package com.sidpatchy.basebot;

import com.sidpatchy.Robin.Discord.ParseCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class RegisterSlashCommands {
    private static final ParseCommands parseCommands = new ParseCommands(Main.getCommandsFile());

    public static void DeleteSlashCommands() {
        JDA jda = getPrimaryJDA();
        if (jda != null) {
            jda.updateCommands().addCommands().queue();
        }
    }

    /**
     * Register slash commands while feeling like you're doing it wrong no matter how you do it!
     * <p>
     * Only called on startup.
     */
    public static void RegisterSlashCommand() {
        JDA jda = getPrimaryJDA();
        if (jda == null) throw new NullPointerException("No available JDA shard to register commands");

        // Create the command list in the help command without repeating the same thing 50 million times.
        List<Command.Choice> helpCommandChoices = new ArrayList<>();
        for (String s : Main.commandList) {
            helpCommandChoices.add(new Command.Choice(parseCommands.getCommandName(s), parseCommands.getCommandName(s)));
        }

        OptionData helpOption = new OptionData(OptionType.STRING, "command-name", "Command to get more info on", false)
                .addChoices(helpCommandChoices);

        CommandData help = Commands.slash(parseCommands.getCommandName("help"), parseCommands.getCommandHelp("help")).addOptions(helpOption);

        jda.updateCommands().addCommands(help).queue();
    }

    private static JDA getPrimaryJDA() {
        if (Main.getShardManager() == null) return null;
        if (Main.getShardManager().getShards().isEmpty()) return null;
        return Main.getShardManager().getShards().get(0);
    }
}
