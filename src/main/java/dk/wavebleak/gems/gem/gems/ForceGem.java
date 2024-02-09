package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.GemWithCooldown;
import hm.zelha.particlesfx.particles.ParticleFlameSoul;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ForceGem extends GemWithCooldown {

    public ForceGem() {
        super(200);
    }

    @Override
    public GemType gemType() {
        return GemType.FORCE;
    }

    @Override
    public Material itemType() {
        return Material.NETHER_STAR;
    }

    @Override
    public String name() {
        return "&6&lForce Gem";
    }

    @Override
    public int modelData() {
        return 8;
    }

    @Override
    public void onRightClick(Player player) {
        if(checkCooldown()) {
            return;
        }
        cooldown = maxCooldown;

        Location playerLocation = player.getLocation();
        player.getWorld().strikeLightning(playerLocation);
        final float force = 2;

        Collection<Entity> nearbyEntities = playerLocation.getWorld().getNearbyEntities(playerLocation, 5 ,1 ,5);

        for (Entity entity : nearbyEntities) {
            if(entity == player) continue;
            if (entity instanceof LivingEntity livingEntity) {
                Location to = livingEntity.getLocation();
                Location from = playerLocation.clone();

                Vector velocity = to.clone().toVector().subtract(from.clone().toVector());

                Location dummy = to.clone();

                dummy.setDirection(velocity);

                dummy.setPitch(-40);

                livingEntity.setVelocity(dummy.getDirection().normalize().multiply(force));
            }
        }


        ParticleFlameSoul particle = new ParticleFlameSoul();

        for(int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 360; j++) {
                        double xOffset = Math.sin(j);
                        double zOffset = Math.cos(j);

                        Location tempLoc = playerLocation.clone().add(xOffset, 0, zOffset);

                        Vector velocity = tempLoc.toVector().subtract(playerLocation.toVector()).normalize();

                        velocity = velocity.multiply(10);

                        particle.setVelocity(velocity);
                        particle.display(playerLocation);
                    }
                }
            }.runTaskLater(Gems.instance, 1);
        }
    }
}
