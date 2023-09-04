package me.overlight.declspoof;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeCLSpoof extends JavaPlugin {

    public static DeCLSpoof ins;

    @Override
    public void onLoad() {
        PacketEvents.create(this); 
        PacketEvents.get().getSettings().checkForUpdates(false).bStats(false).fallbackServerVersion(ServerVersion.getVersion());
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        ins = this;
        PacketEvents.get().init();
        getServer().getPluginManager().registerEvents(new EventHandler(), this);
        PacketEvents.get().getEventManager().registerListener(new PacketListener());
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}
