package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import hm.zelha.particlesfx.particles.ParticleSonicBoom;
import hm.zelha.particlesfx.shapers.ParticleLine;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WardenGem extends Gem {

    public int cooldown = 0;

    @Override
    public GemType gemType() {
        return GemType.WARDEN;
    }

    @Override
    public Material itemType() {
        return Material.ECHO_SHARD;
    }

    @Override
    public String name() {
        return "&3&lWarden Gem";
    }

    @Override
    public int modelData() {
        return 6;
    }

    @Override
    public void onRightClick(Player player) {
        if(cooldown > 0) {
            sendCooldownMessage(player, cooldown);
            return;
        }
        cooldown = 200;

        final float distance = 10f;
        final float stepDistance = 0.5f;
        final float damage = 5;

        Location loc = player.getEyeLocation();
        Vector direction = loc.getDirection().normalize().multiply(distance);
        List<Location> steps = new ArrayList<>();
        Vector step = direction.clone().normalize().multiply(stepDistance);



        ParticleSonicBoom boomParticle = new ParticleSonicBoom();

        float total = 0;
        Location dummy = loc.clone();
        while(total < distance) {
            steps.add(dummy.clone());
            dummy.add(step);
            total += stepDistance;
        }


        new BukkitRunnable() {
            int i = 0;
            Location previousLoc = loc;
            @Override
            public void run() {
                try {
                    Location currentLoc = steps.get(i);
                    i++;
                    ParticleSonicBoom clonedBoom = boomParticle.clone();
                    clonedBoom.display(currentLoc);

                    RayTraceResult ray = player.getWorld().rayTrace(
                            previousLoc,
                            direction,
                            previousLoc.distance(currentLoc),
                            FluidCollisionMode.NEVER,
                            true,
                            1,
                            entity -> entity != player

                    );

                    previousLoc = currentLoc;

                    if(ray == null) return;

                    if(ray.getHitEntity() != null) {
                        Entity entity = ray.getHitEntity();
                        if(entity instanceof LivingEntity hitEntity) {
                            hitEntity.getWorld().playSound(hitEntity.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 0.1f);
                            hitEntity.damage(damage, player);
                            cancel();
                        }
                    }
                    if(ray.getHitBlock() != null) {
                        Block block = ray.getHitBlock();

                        block.getWorld().createExplosion(block.getLocation(), 2);
                        cancel();
                    }

                }catch (IndexOutOfBoundsException e) {
                    //previousLoc.getWorld().createExplosion(previousLoc, 2);
                    cancel();
                }
            }
        }.runTaskTimer(Gems.instance, 1, 1);

    }


    @Override
    public void onTick(Player player) {
        if(cooldown > 0) {
            cooldown--;
        }
    }
}
