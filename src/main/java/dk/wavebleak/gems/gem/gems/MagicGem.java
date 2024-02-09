package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.GemWithCooldown;
import hm.zelha.particlesfx.particles.ParticleWitchMagic;
import hm.zelha.particlesfx.shapers.ParticleCircle;
import hm.zelha.particlesfx.shapers.ParticleCircleFilled;
import hm.zelha.particlesfx.util.LocationSafe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MagicGem extends GemWithCooldown {
    public MagicGem() {
        super(200);
    }

    @Override
    public GemType gemType() {
        return GemType.MAGIC;
    }

    @Override
    public Material itemType() {
        return Material.GLOWSTONE_DUST;
    }

    @Override
    public String name() {
        return "&e&lMagic Gem";
    }

    @Override
    public int modelData() {
        return 9;
    }

    @Override
    public void onRightClick(Player player) {
        if(checkCooldown()) {
            sendCooldownMessage(player);
            return;
        }
        cooldown = maxCooldown;

        final float distance = 5;

        Location destination2D = player.getEyeLocation();
        destination2D.setPitch(0);
        Vector direction = destination2D.getDirection().normalize();
        destination2D.add(direction.multiply(distance));

        List<Location> possibleOutcomes = new ArrayList<>();
        for(int i = 0; i < 255; i++) {
            Location possibleOutcome = destination2D.clone();
            possibleOutcome.setY(i);
            possibleOutcomes.add(possibleOutcome);
        }

        Optional<Location> destinationOptional = possibleOutcomes.stream().filter(location -> {
            World world = location.getWorld();

            assert world != null;
            return world.getBlockAt(location).getType().isAir() && world.getBlockAt(location.add(0, -1, 0)).getType().isSolid();

        }).min(Comparator.comparingDouble(x -> x.distance(player.getLocation())));

        if(destinationOptional.isPresent()) {
            Location destination = destinationOptional.get();
            destination.add(0, 1, 0);
            ParticleCircleFilled filledCircle = new ParticleCircleFilled(new ParticleWitchMagic(), new LocationSafe(destination), 1);

            player.teleport(destination);

            Objects.requireNonNull(destination.getWorld()).playSound(destination, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1, 2);

            filledCircle.display();

            new BukkitRunnable() {
                @Override
                public void run() {
                    filledCircle.stop();
                }
            }.runTaskLater(Gems.instance, 20);
        }else {
            cooldown = 0;
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 0.1f);
        }


    }
}
