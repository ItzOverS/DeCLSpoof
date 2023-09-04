package me.overlight.declspoof;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventHandler implements Listener {
    @org.bukkit.event.EventHandler
    public void event(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskLaterAsynchronously(DeCLSpoof.ins, () -> {
            if(PacketListener.client.getOrDefault(e.getPlayer().getName(), null) == null)
                Bukkit.getScheduler().runTask(DeCLSpoof.ins, () -> e.getPlayer().kickPlayer(ChatColor.RED + "Please Rejoin"));
        }, 1000);
    }
}
