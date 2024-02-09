package dk.wavebleak.gems.gem;

import dk.wavebleak.gems.utils.TickUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class GemWithCooldown extends Gem {
    protected int cooldown = 0;
    protected int maxCooldown;
    public GemWithCooldown(int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    @Override
    public void onTick(Player player) {
        if(cooldown > 0) {
            cooldown--;
        }
    }

    protected boolean checkCooldown() {
        return cooldown > 0;
    }

    protected void sendCooldownMessage(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You must wait " + TickUtils.ticksToSeconds(cooldown) + " seconds!"));
    }

}
