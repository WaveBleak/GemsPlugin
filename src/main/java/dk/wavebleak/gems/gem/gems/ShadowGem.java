package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import hm.zelha.particlesfx.particles.ParticleSculkSoul;
import hm.zelha.particlesfx.shapers.ParticleLineCurved;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ShadowGem extends Gem {
    @Override
    public GemType gemType() {
        return GemType.SHADOW;
    }

    @Override
    public String name() {
        return "&8&lShadow gem";
    }

    @Override
    public Material itemType() {
        return Material.GHAST_TEAR;
    }


    @Override
    public int modelData() {
        return 2;
    }

    @Override
    public void onAttack(Player player, LivingEntity victim) {
        ItemStack gem = getAsItem();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        Random random = new Random();

        if(gem.getItemMeta() == null || offhand.getItemMeta() == null) return;

        if(!gem.getItemMeta().hasCustomModelData() || !offhand.getItemMeta().hasCustomModelData()) return;

        if(!(gem.getItemMeta().getCustomModelData() == offhand.getItemMeta().getCustomModelData())) return;

        victim.removePotionEffect(PotionEffectType.DARKNESS);

        Location loc1 = player.getEyeLocation();
        Location curve = loc1.clone();
        Location loc2 = victim.getEyeLocation();

        double distance = loc1.distance(loc2);

        Vector dir = player.getEyeLocation().getDirection();

        float xoffset = random.nextFloat(2) - 1;
        float yoffset = random.nextFloat(2) - 1;
        float zoffset = random.nextFloat(2) - 1;

        float multiplier = random.nextFloat(0.5f) + 0.5f;

        Vector curveDir = dir.clone().rotateAroundAxis(dir.clone().crossProduct(new Vector(xoffset, yoffset, zoffset)), Math.PI / 2);
        curve.add(dir.normalize().multiply(distance / 2));
        curve.add(curveDir.multiply(multiplier));

        LocationSafe curvedLoc = new LocationSafe(curve);

        ParticleSculkSoul soul = new ParticleSculkSoul();
        soul.setVelocity(dir.normalize().multiply(0.1));
        ParticleLineCurved curved = new ParticleLineCurved(soul, 10, new LocationSafe(player.getEyeLocation()), curvedLoc, new LocationSafe(victim.getEyeLocation()));

        curved.display();


        new BukkitRunnable() {
            public void run() {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 3 * 20, 1));
            }
        }.runTaskLater(Gems.instance, 1);

        new BukkitRunnable() {
            public void run() {
                curved.stop();
            }
        }.runTaskLater(Gems.instance, 5);

    }
}
