package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.utils.TickUtils;
import hm.zelha.particlesfx.particles.ParticleCloud;
import hm.zelha.particlesfx.particles.parents.Particle;
import hm.zelha.particlesfx.shapers.ParticleCircleFilled;
import hm.zelha.particlesfx.shapers.ParticleCylinder;
import hm.zelha.particlesfx.util.CircleInfo;
import hm.zelha.particlesfx.util.LocationSafe;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class PuffGem extends Gem {

    private int cooldown = 0;

    @Override
    public GemType gemType() {
        return GemType.PUFF;
    }

    @Override
    public Material itemType() {
        return Material.WHITE_WOOL;
    }

    @Override
    public String name() {
        return "&f&lPuff Gem";
    }

    @Override
    public int modelData() {
        return 0;
    }

    @Override
    public void onRightClick(Player player) {
        if (cooldown > 0) {
            sendCooldownMessage(player, cooldown);
            return;
        }
        cooldown = 300;

        Location location = player.getLocation();

        location.setPitch(-10);

        Vector dir = location.getDirection().normalize();
        player.setVelocity(dir.multiply(5));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1f);

        ParticleCircleFilled start = new ParticleCircleFilled(new ParticleCloud().setSpeed(0), new LocationSafe(player.getLocation()), 1.5);


        start.display();

        new BukkitRunnable() {
            @Override
            public void run() {
                start.stop();
            }
        }.runTaskLater(Gems.instance, 60);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.isOnGround()) {
                    ParticleCircleFilled end = new ParticleCircleFilled(new ParticleCloud().setSpeed(0), new LocationSafe(player.getLocation()), 1.5);
                    end.display();
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.1f);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            end.stop();
                        }
                    }.runTaskLater(Gems.instance, 60);
                    cancel();
                }
            }
        }.runTaskTimer(Gems.instance, 3, 3);
    }

    @Override
    public void onTick(Player player) {
        if(cooldown > 0) {
            cooldown--;
        }
    }
}
