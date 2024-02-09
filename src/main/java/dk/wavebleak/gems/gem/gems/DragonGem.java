package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.GemWithCooldown;
import hm.zelha.particlesfx.particles.ParticlePortal;
import hm.zelha.particlesfx.particles.ParticleSonicBoom;
import hm.zelha.particlesfx.particles.parents.Particle;
import hm.zelha.particlesfx.shapers.ParticleLine;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DragonGem extends GemWithCooldown {
    public DragonGem() {
        super(100);
    }

    @Override
    public GemType gemType() {
        return GemType.DRAGON;
    }

    @Override
    public Material itemType() {
        return Material.DRAGON_BREATH;
    }

    @Override
    public String name() {
        return "&d&lDragon Gem";
    }

    @Override
    public int modelData() {
        return 10;
    }


    @Override
    public void onRightClick(Player player) {
        if(checkCooldown()) {
            sendCooldownMessage(player);
            return;
        }
        cooldown = maxCooldown;
        ParticlePortal portalParticle = new ParticlePortal();
        final float distance = 20f;
        final float stepDistance = 0.5f;

        Location loc = player.getEyeLocation();
        Vector direction = loc.getDirection().normalize().multiply(distance);
        List<Location> steps = new ArrayList<>();
        Vector step = direction.clone().normalize().multiply(stepDistance);

        float total = 0;
        Location dummy = loc.clone();
        while(total < distance) {
            steps.add(dummy.clone());
            dummy.add(step);
            total += stepDistance;
        }

        List<ParticleLine> lines = new ArrayList<>();
        new BukkitRunnable() {
            int i = 0;
            Location previousLoc = loc;
            @Override
            public void run() {
                try {
                    Location currentLoc = steps.get(i);
                    i++;
                    ParticlePortal clonedPortal = portalParticle.clone();

                    ParticleLine line = new ParticleLine(clonedPortal, 5, new LocationSafe(previousLoc), new LocationSafe(loc));

                    line.display();

                    lines.add(line);

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
                            AreaEffectCloud cloud = (AreaEffectCloud) hitEntity.getWorld().spawnEntity(hitEntity.getLocation().add(0.5, 1, 0.5), EntityType.AREA_EFFECT_CLOUD);
                            cloud.setDuration(100);
                            cloud.setColor(Color.PURPLE);
                            cloud.setBasePotionType(PotionType.INSTANT_DAMAGE);
                            throw new IndexOutOfBoundsException();
                        }
                    }
                    if(ray.getHitBlock() != null) {
                        Block block = ray.getHitBlock();

                        AreaEffectCloud cloud = (AreaEffectCloud) block.getWorld().spawnEntity(block.getLocation().add(0.5, 1, 0.5), EntityType.AREA_EFFECT_CLOUD);
                        cloud.setDuration(100);
                        cloud.setColor(Color.PURPLE);
                        cloud.setBasePotionType(PotionType.INSTANT_DAMAGE);
                        throw new IndexOutOfBoundsException();
                    }

                }catch (IndexOutOfBoundsException e) {
                    cancel();
                    for(ParticleLine line : lines) {
                        line.stop();
                    }
                }
            }
        }.runTaskTimer(Gems.instance, 1, 1);
    }
}
