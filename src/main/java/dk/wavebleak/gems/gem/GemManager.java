package dk.wavebleak.gems.gem;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.gems.PuffGem;
import dk.wavebleak.gems.gem.gems.ShadowGem;
import dk.wavebleak.gems.gem.gems.StrengthGem;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;

import java.io.File;
import java.net.URL;
import java.util.*;

public class GemManager implements Listener {

    public static List<Gem> gems = new ArrayList<>();

    public static Gem fromEnum(GemType gemType) {
        return gems.stream().filter(x -> x.gemType().equals(gemType)).findFirst().orElse(null);
    }

    public GemManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    for(ItemStack item : player.getInventory()) {
                        if(item == null) {
                            continue;
                        }
                        if(item.getItemMeta() == null) {
                            continue;
                        }
                        if(!item.getItemMeta().hasCustomModelData()) {
                            continue;
                        }
                        gems.stream().filter(x -> {
                            ItemStack gemItem = x.getAsItem();
                            if(gemItem.getItemMeta() == null) {
                                return false;
                            }
                            return gemItem.getItemMeta().getCustomModelData() == item.getItemMeta().getCustomModelData();
                        }).findFirst().ifPresent(gem -> {
                            gem.onTick(player);
                        });
                    }
                }
            }
        }.runTaskTimer(Gems.instance, 1, 1);

        Reflections reflections = new Reflections("dk.wavebleak.gems");

        Set<Class<? extends Gem>> subTypes = reflections.getSubTypesOf(Gem.class);

        for (Class<? extends Gem> gemClass : subTypes) {
            try {
                Gem gem = gemClass.newInstance();
                gems.add(gem);
            } catch (InstantiationException | IllegalAccessException e) {
                Gems.instance.getLogger().warning(e.getMessage());
            }
        }
    }


    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player attacker)) return;
        if(!(event.getEntity() instanceof LivingEntity victim)) return;

        for(ItemStack item : attacker.getInventory()) {
            if(item == null) {
                continue;
            }
            if(item.getItemMeta() == null) {
                continue;
            }
            if(!item.getItemMeta().hasCustomModelData()) {
                continue;
            }
            gems.stream().filter(x -> {
                ItemStack gemItem = x.getAsItem();
                if(gemItem.getItemMeta() == null) {
                    return false;
                }
                return gemItem.getItemMeta().getCustomModelData() == item.getItemMeta().getCustomModelData();
            }).findFirst().ifPresent(gem -> {
                gem.onAttack(attacker, victim);
            });
        }
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                if(item.getItemMeta() == null) {
                    return;
                }
                if(!item.getItemMeta().hasCustomModelData()) {
                    return;
                }

                gems.stream().filter(x -> {
                    if(x.getAsItem().getItemMeta() == null) {
                        return false;
                    }
                    return x.getAsItem().getItemMeta().getCustomModelData() == item.getItemMeta().getCustomModelData();
                }).findFirst().ifPresent(gem -> gem.onRightClick(event.getPlayer()));

            }
        }
    }

}
