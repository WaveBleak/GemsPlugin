package dk.wavebleak.gems.gem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Gem implements IGem {
    public ItemStack getAsItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
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

}
