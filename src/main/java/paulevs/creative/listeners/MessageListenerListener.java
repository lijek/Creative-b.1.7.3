package paulevs.creative.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.creative.Creative;
import paulevs.creative.CreativePlayer;

public class MessageListenerListener {

    @EventListener
    public void listenToListener(MessageListenerRegistryEvent event){
        event.registry.register(Identifier.of(Creative.MODID, Creative.infoPacketID), (PlayerBase playerBase, Message msg) -> {
            System.out.println(playerBase.toString());
            CreativePlayer player = (CreativePlayer) playerBase;
            player.setCreative(msg.booleans[0]);
            player.setFlying(msg.booleans[1]);
        });

        /*event.registry.register(Identifier.of(Creative.MODID, Creative.toggleFlyPacketID), (PlayerBase playerBase, Message msg) -> {
            if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                System.out.println("client received");
                return;
            }
            System.out.println("toggled fly");
            CreativePlayer player = (CreativePlayer) playerBase;
            player.setFlying(msg.booleans[0]);
        });*/
    }
}
