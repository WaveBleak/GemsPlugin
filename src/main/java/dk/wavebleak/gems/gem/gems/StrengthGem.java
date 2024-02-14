package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.GemWithCooldown;
import hm.zelha.particlesfx.particles.ParticleBlockDust;
import hm.zelha.particlesfx.shapers.ParticleSpiral;
import hm.zelha.particlesfx.util.CircleInfo;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class StrengthGem extends GemWithCooldown {
    public StrengthGem() {
        super(200);
    }

    @Override
    public GemType gemType() {
        return GemType.STRENGTH;
    }
    @Override
    public Material itemType() {
        return Material.LEATHER;
    }

    @Override
    public String name() {
        return "&4&lStrength Gem";
    }


    @Override
    public int modelData() {
        return 3;
    }

    @Override
    public void onRightClick(Player player) {
        if(checkCooldown()) {
            sendCooldownMessage(player);
            return;
        }

        cooldown = maxCooldown;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8 * 20, 1));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);

        CircleInfo info1 = new CircleInfo(new LocationSafe(player.getLocation()), 1);
        CircleInfo info2 = new CircleInfo(new LocationSafe(player.getEyeLocation()), 1);

        ParticleSpiral spiral = new ParticleSpiral(new ParticleBlockDust(Material.REDSTONE_BLOCK).setSpeed(0), 1, info1, info2);

        spiral.display();

        new BukkitRunnable() {
            @Override
            public void run() {
                spiral.stop();
            }
        }.runTaskLater(Gems.instance, 20);
    }
}
