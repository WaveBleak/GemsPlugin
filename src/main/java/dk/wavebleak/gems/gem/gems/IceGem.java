package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import hm.zelha.particlesfx.particles.ParticleDustColored;
import hm.zelha.particlesfx.shapers.ParticleCircleFilled;

import hm.zelha.particlesfx.util.Color;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class IceGem extends Gem {


    @Override
    public GemType gemType() {
        return GemType.ICE;
    }

    @Override
    public Material itemType() {
        return Material.BLUE_DYE;
    }

    @Override
    public String name() {
        return "&2&lIce Gem";
    }

    @Override
    public int modelData() {
        return 7;
    }

    @Override
    public void onAttack(Player player, LivingEntity victim) {
        final double maxDistance = 0.2;
        final int freezeTime = 10;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));

        if(!Gems.instance.freezedEntities.contains(victim)) {
            Gems.instance.freezedEntities.add(victim);

            ParticleCircleFilled circle = new ParticleCircleFilled(new ParticleDustColored(Color.AQUA), new LocationSafe(victim.getLocation()), 0.5);

            circle.display();
            new BukkitRunnable() {
                int freezeTicks = 0;
                final Location originalLocation = victim.getLocation();
                @Override
                public void run() {
                    freezeTicks++;
                    if(freezeTicks >= freezeTime) {
                        Gems.instance.freezedEntities.remove(victim);
                        circle.stop();
                        cancel();
                    }
                    if(!(originalLocation.distance(victim.getLocation()) > maxDistance)) {
                        return;
                    }

                    Location newLocation = originalLocation.clone();

                    newLocation.setDirection(victim.getLocation().getDirection());

                    victim.teleport(newLocation);

                }
            }.runTaskTimer(Gems.instance, 1 ,1);
        }
    }
}
