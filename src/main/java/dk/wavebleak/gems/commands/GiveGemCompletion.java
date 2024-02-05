package dk.wavebleak.gems.commands;

import dk.wavebleak.gems.gem.GemType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GiveGemCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            GemType[] gems = GemType.values();
            List<String> options = new ArrayList<>();
            for(GemType gem : gems) {
                options.add(gem.name());
            }
            return options;
        }
        return new ArrayList<>();
    }
}
