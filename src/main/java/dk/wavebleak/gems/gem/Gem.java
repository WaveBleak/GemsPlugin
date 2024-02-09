package dk.wavebleak.gems.gem;

import dk.wavebleak.gems.utils.TickUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Gem implements IGem {
    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(itemType());
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return new ItemStack(Material.STONE);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name()));
        List<String> lore = new ArrayList<>();
        for(String line : lore()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        meta.setCustomModelData(modelData());

        item.setItemMeta(meta);

        return item;
    }

    public String[] lore() {
        return new String[0];
    }


    public void onRightClick(Player player) {

    }

    public void onTick(Player player) {

    }

    public void onAttack(Player player, LivingEntity victim) {

    }

    public void onBreak(BlockBreakEvent event) {

    }


}
