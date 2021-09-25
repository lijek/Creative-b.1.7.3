package paulevs.creative.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import paulevs.creative.Creative;
import paulevs.creative.CreativePlayer;

public class ServerPlayerInfoSender {

    @EventListener
    public void playerLogin(PlayerLoginEvent event){
        CreativePlayer player = (CreativePlayer) event.player;
        Message msg = new Message(Creative.infoPacket);
        msg.booleans = new boolean[]{player.isCreative(), player.isFlying()};
        PacketHelper.sendTo(event.player, msg);
    }
}
