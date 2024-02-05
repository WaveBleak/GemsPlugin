package dk.wavebleak.gems;

import dk.wavebleak.gems.commands.GiveGemCommand;
import dk.wavebleak.gems.commands.GiveGemCompletion;
import dk.wavebleak.gems.gem.GemManager;
import hm.zelha.particlesfx.util.ParticleSFX;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Gems extends JavaPlugin {



    public static Gems instance;

    @Override
    public void onEnable() {
        instance = this;

        ParticleSFX.setPlugin(this);

        Bukkit.getPluginManager().registerEvents(new GemManager(), this);

        Objects.requireNonNull(getCommand("givegem")).setExecutor(new GiveGemCommand());
        Objects.requireNonNull(getCommand("givegem")).setTabCompleter(new GiveGemCompletion());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
