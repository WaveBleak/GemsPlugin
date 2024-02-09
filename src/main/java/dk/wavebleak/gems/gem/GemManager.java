package dk.wavebleak.gems.gem;

import dk.wavebleak.gems.Gems;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("deprecation")
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
                    GemRunnable runnable = (gem) -> {
                        gem.onTick(player);
                    };

                    executePerGem(player.getInventory(), runnable);
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
    public void onPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;

        AtomicInteger currentGemsInInventory = new AtomicInteger();
        final int maxGemsInInventory = 1;

        GemRunnable runnable = (gem) -> {
            currentGemsInInventory.getAndIncrement();
        };

        executePerGem(player.getInventory(), runnable);

        if(currentGemsInInventory.get() >= maxGemsInInventory) {
            event.setCancelled(isGem(event.getItem().getItemStack()));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();

        GemRunnable runnable = (gem) -> {
            event.setCancelled(true);
        };

        executeIfGem(itemInHand, runnable);
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        GemRunnable runnable = (gem) -> {
            gem.onBreak(event);
        };

        executePerGem(event.getPlayer().getInventory(), runnable);
    }




    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player attacker)) return;
        if(!(event.getEntity() instanceof LivingEntity victim)) return;

        GemRunnable runnable = (gem) -> {
            gem.onAttack(attacker, victim);
        };

        executePerGem(attacker.getInventory(), runnable);
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

                GemRunnable runnable = (gem) -> {
                    gem.onRightClick(event.getPlayer());
                };

                executeIfGem(item, runnable);
            }
        }
    }


    public boolean isGem(ItemStack item) {
        Optional<Gem> optional = gems.stream().filter(gem -> {
            if(gem.getAsItem().getItemMeta() == null) {
                return false;
            }
            return gem.getAsItem().getItemMeta().getCustomModelData() == Objects.requireNonNull(item.getItemMeta()).getCustomModelData();
        }).findFirst();

        return optional.isPresent();
    }

    public void executePerGem(Inventory inventory, GemRunnable runnable) {
        for(ItemStack item : inventory) {
            if(item == null) continue;
            executeIfGem(item, runnable);
        }
    }

    public void executeIfGem(ItemStack item, GemRunnable runnable) {
        if(!hasGemModelData(item)) return;
        gems.stream().filter(gem -> {
            if (gem.getAsItem().getItemMeta() == null) {
                return false;
            }
            return gem.getAsItem().getItemMeta().getCustomModelData() == Objects.requireNonNull(item.getItemMeta()).getCustomModelData();
        }).findFirst().ifPresent(runnable::run);
    }

    public boolean hasGemModelData(ItemStack item) {
        if(item.getItemMeta() == null) {
            return true;
        }
        return item.getItemMeta().hasCustomModelData();
    }

}
