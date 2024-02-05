package dk.wavebleak.gems.gem;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface IGem {

    GemType gemType();
    Material itemType();
    String name();
    String[] lore();
    int modelData();
    void onRightClick(Player player);
    void onTick(Player player);
    void onAttack(Player player, LivingEntity victim);
    void onBreak(BlockBreakEvent event);


}
