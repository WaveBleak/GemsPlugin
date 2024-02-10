package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Random;

public class WealthGem extends Gem {
    @Override
    public GemType gemType() {
        return GemType.WEALTH;
    }

    @Override
    public Material itemType() {
        return Material.GREEN_DYE;
    }

    @Override
    public String name() {
        return "&b&lWealth Gem";
    }

    @Override
    public int modelData() {
        return 5;
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        Random random = new Random();
        ItemStack pickaxe = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack gem = this.getAsItem();

        if(!event.getPlayer().getInventory().getItemInOffHand().equals(gem)) return;
        if(!isPickaxe(pickaxe)) return;
        if(!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) return;

        Map<Enchantment, Integer> enchantments = pickaxe.getEnchantments();


        int lowestDrop = 0;
        for(Enchantment enchantment : enchantments.keySet()) {
            if(enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                lowestDrop = enchantments.get(enchantment);
            }
            if(enchantment.equals(Enchantment.SILK_TOUCH)) {
                return;
            }
        }

        event.setDropItems(false);
        int drops = random.nextInt(5) + lowestDrop;

        for(ItemStack item : event.getBlock().getDrops()) {
            ItemStack drop = item.clone();
            drop.setAmount(drops);
            event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5, 0.5, 0.5), drop);
        }

    }


    public boolean isPickaxe(ItemStack in) {
        return switch (in.getType()) {
            case DIAMOND_PICKAXE, GOLDEN_PICKAXE, WOODEN_PICKAXE, STONE_PICKAXE, NETHERITE_PICKAXE -> true;
            default -> false;
        };
    }
}
