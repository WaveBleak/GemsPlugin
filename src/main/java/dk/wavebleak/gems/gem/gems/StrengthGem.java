package dk.wavebleak.gems.gem.gems;

import dk.wavebleak.gems.Gems;
import dk.wavebleak.gems.gem.GemType;
import dk.wavebleak.gems.gem.Gem;
import dk.wavebleak.gems.utils.TickUtils;
import hm.zelha.particlesfx.particles.ParticleBlockDust;
import hm.zelha.particlesfx.shapers.ParticleSpiral;
import hm.zelha.particlesfx.util.CircleInfo;
import hm.zelha.particlesfx.util.LocationSafe;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

public class StrengthGem extends Gem {

    private int cooldown = 0;
    @Override
    public GemType gemType() {
        return GemType.STRENGTH;
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
        if(cooldown > 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You must wait " + TickUtils.ticksToSeconds(cooldown) + " seconds!"));
            return;
        }
        cooldown = 200;

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

    @Override
    public void onTick(Player player) {
        if(cooldown > 0) {
            cooldown--;
        }
    }
}
