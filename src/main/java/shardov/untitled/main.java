package shardov.untitled;

import Event.JoinEvent;
import Event.SheepANDZombie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SheepANDZombie(this), this);

    }
    }



