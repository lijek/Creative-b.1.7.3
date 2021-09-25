package paulevs.creative.listeners;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.packet.Message;
import paulevs.creative.Creative;
import paulevs.creative.CreativePlayer;

import java.util.ArrayList;
import java.util.List;

import static paulevs.creative.Utility.isEmpty;

public class MessageListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void listenerClient(MessageListenerRegistryEvent event) {
        event.registry.register(Creative.infoPacket, (PlayerBase playerBase, Message msg) -> {
            CreativePlayer player = (CreativePlayer) playerBase;
            player.setCreative(msg.booleans[0]);
            player.setFlying(msg.booleans[1]);
        });

        event.registry.register(Creative.inventoryClickPacket, this::handleInventoryClick);
    }

    @Environment(EnvType.SERVER)
    @EventListener
    public void listenerServer(MessageListenerRegistryEvent event){
        event.registry.register(Creative.toggleFlyPacket, (PlayerBase playerBase, Message msg) -> {
            if (!isCreative(playerBase))
                return;
            CreativePlayer player = (CreativePlayer) playerBase;
            player.setFlying(msg.booleans[0]);
        });

        event.registry.register(Creative.inventoryClickPacket, (PlayerBase playerBase, Message msg) -> {
            handleInventoryClick(playerBase, msg);
            syncInventory((ServerPlayer) playerBase);
        });

        event.registry.register(Creative.duplicateItemStackPacket, (PlayerBase playerBase, Message msg) -> {
            if(!isCreative(playerBase))
                return;
            ItemInstance item = new ItemInstance(msg.ints[0], msg.ints[1], msg.ints[2]);
            playerBase.inventory.setCursorItem(new ItemInstance(item.itemId, item.getType().getMaxStackSize(), item.getDamage()));
            syncInventory(((ServerPlayer) playerBase));
        });
    }

    private boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    private boolean isCreative(PlayerBase playerBase){
        return ((CreativePlayer) playerBase).isCreative();
    }

    @Environment(EnvType.SERVER)
    private void syncInventory(ServerPlayer player){
        List<ItemInstance> newContents = new ArrayList<>();
        for (int i = 0; i < player.container.slots.size(); ++i) {
            newContents.add(((Slot) player.container.slots.get(i)).getItem());
        }
        player.updateContents(player.container, newContents);
    }

    private void handleInventoryClick(PlayerBase playerBase, Message msg){
        if (!isCreative(playerBase))
            return;
        int button = msg.ints[3];
        PlayerInventory inventory = playerBase.inventory;
        ItemInstance cursorItem = inventory.getCursorItem();
        ItemInstance item = new ItemInstance(msg.ints[0], msg.ints[1], msg.ints[2]);

        boolean isSame = !isEmpty(cursorItem) && cursorItem.isDamageAndIDIdentical(item);
        if (msg.booleans[0]) {

            if (isSame) {
                cursorItem.count++;
                if(cursorItem.count > cursorItem.getMaxStackSize())
                    cursorItem.count = cursorItem.getMaxStackSize();
            } else if (isEmpty(cursorItem)) {
                inventory.setCursorItem(item.copy());
            } else {
                inventory.setCursorItem(null);
            }

            if (inventory.getCursorItem() != null && button == 2) {
                inventory.getCursorItem().count = ItemBase.byId[inventory.getCursorItem().itemId].getMaxStackSize();
            }
        }

        if(button == 1 && !isEmpty(cursorItem)) {
            inventory.setCursorItem(null);
        }
    }
}
