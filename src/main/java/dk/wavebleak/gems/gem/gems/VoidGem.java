package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.GemWithCooldown;
import hm.zelha.particlesfx.particles.ParticleDustColored;
import hm.zelha.particlesfx.shapers.ParticleCircle;
import hm.zelha.particlesfx.util.Color;
import hm.zelha.particlesfx.util.LocationSafe;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class VoidGem extends GemWithCooldown {
    public VoidGem() {
        super(100);
    }

    @Override
    public GemType gemType() {
        return GemType.VOID;
    }
    @Override
    public Material itemType() {
        return Material.IRON_HORSE_ARMOR;
    }

    @Override
    public String name() {
        return "&d&lVoid Gem";
    }

    @Override
    public int modelData() {
        return 4;
    }

    @Override
    public void onRightClick(Player player) {
        if(checkCooldown()) {
            sendCooldownMessage(player);
            return;
        }
        Location loc = player.getEyeLocation();
        final Location finalLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        Vector dir = loc.getDirection().normalize().multiply(0.5f);
        float yaw = loc.getYaw();

        List<Location> locationsToCheck = new ArrayList<>();

        if(!player.getTargetBlock(null, 1).getType().isSolid()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You're not looking at a block"));
            return;
        }

        for(int i = 0; i < 5; i++) {
            loc.add(dir);
            locationsToCheck.add(loc.clone());
        }

        boolean hasTeleported = false;
        Location correctLocation = null;
        for (Location up : locationsToCheck) {
            Location down = new Location(up.getWorld(), up.getX(), up.getY() - 1, up.getZ());
            correctLocation = down;

            if(player.getWorld().getBlockAt(up).getType().isAir() && player.getWorld().getBlockAt(down).getType().isAir()) {
                player.teleport(down);
                correctLocation = up;
                hasTeleported = true;
            }
        }

        if(!hasTeleported) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Couldn't reach the other side"));
        } else {
            cooldown = maxCooldown;
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.1f);

            Location start = finalLoc.clone().add(0, 0.33, 0);

            Location end = start.clone().add(dir.normalize().multiply(finalLoc.distance(correctLocation)));

            List<ParticleCircle> circles = new ArrayList<>();

            double distance = start.distance(end);

            int steps = (int) (distance / 0.2);

            Vector vector = end.toVector().subtract(start.toVector()).normalize().multiply(0.2);

            for (int i = 0; i < steps; i++) {
                Location location = start.clone().add(vector.clone().multiply(i));
                ParticleCircle currentCircle = new ParticleCircle(new ParticleDustColored(Color.MAGENTA), new LocationSafe(location), 1);
                currentCircle.rotate(90, yaw, 0);
                currentCircle.setXRadius(0.5);
                currentCircle.setZRadius(1.2);
                circles.add(currentCircle);
            }

            for(ParticleCircle circle : circles) {
                circle.display();
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(ParticleCircle circle : circles) {
                        circle.stop();
                    }
                }
            }.runTaskLater(Gems.instance, 10);
        }
    }
}
