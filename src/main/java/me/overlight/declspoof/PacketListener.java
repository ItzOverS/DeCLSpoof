package me.overlight.declspoof;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketHandshakeReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketLoginReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.handshaking.setprotocol.WrappedPacketHandshakingInSetProtocol;
import io.github.retrooper.packetevents.packetwrappers.login.in.start.WrappedPacketLoginInStart;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PacketListener extends PacketListenerAbstract {
    public static HashMap<String, Integer> handshakeVersion = new HashMap<>();
    @Override
    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
        WrappedPacketHandshakingInSetProtocol e = new WrappedPacketHandshakingInSetProtocol(event.getNMSPacket());
        handshakeVersion.put(e.getHostName(), e.getProtocolVersion());
    }


    public static HashMap<String, String> client = new HashMap<>();

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(event.getPacketId() == PacketType.Play.Client.CUSTOM_PAYLOAD){
            WrappedPacketInCustomPayload payload = new WrappedPacketInCustomPayload(event.getNMSPacket());
            Player player = event.getPlayer();
            boolean isNew = ServerData.isNewerThan(ServerData.getServerVersion(), "1.12");
            if(!payload.getChannelName().equals(isNew? "mc:brand": "MC|Brand")) return;

            String brand = new String(payload.getData(), StandardCharsets.UTF_8).substring(1);
            // premium clients
            String[] premCLs = { "CB", "Lunarclient:v", "eyser", "LC"};

            PremiumOnlineUUIDChecker.isPrem s = PremiumOnlineUUIDChecker.isPremium(player);
            if(s != PremiumOnlineUUIDChecker.isPrem.Yes){
                if(indexStartsWith(brand, premCLs)){
                    Bukkit.getScheduler().runTask(DeCLSpoof.ins, () -> {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ChatColor.RED + "Spoofing Client", null, "DeCLSpoof");
                        player.kickPlayer(ChatColor.RED + "Spoofing Client");
                    });
                }
            }
            client.put(player.getName(), brand);
        }
    }

    private boolean indexStartsWith(String text, String[] list){
        for(String s : list){
            if(text.startsWith(s)) return true;
        }
        return false;
    }
}
