package dk.wavebleak.gems.commands;

import dk.wavebleak.gems.gem.GemManager;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveGemCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
            GemType[] gems = GemType.values();

            StringBuilder sb = new StringBuilder();

            for(GemType gem : gems) {
                sb.append(StringUtils.beautify(gem.name())).append("/");
            }

            String gemList = sb.substring(0, sb.length() - 1);

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCorrect usage: /givegem <" + gemList + "> [Integer: amount]"));
            return false;
        }

        int amount = 1;
        if(args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAmount must be a valid integer"));
                return false;
            }
        }
        GemType gem;
        try {
            gem = GemType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid gem type"));
            return false;
        }

        Player player = (Player) sender;
        ItemStack gemItem = GemManager.fromEnum(gem).getAsItem();
        gemItem.setAmount(amount);
        player.getInventory().addItem(gemItem);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aGave you the " + gemItem.getItemMeta().getDisplayName() + "!"));
        return true;
    }
}
