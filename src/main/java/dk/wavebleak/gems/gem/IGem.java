package dk.wavebleak.gems.gem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface IGem {

    GemType gemType();
    String name();
    String[] lore();
    int modelData();
    void onRightClick(Player player);
    void onTick(Player player);
    void onAttack(Player player, LivingEntity victim);


}
