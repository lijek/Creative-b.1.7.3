package paulevs.creative.mixin.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.Creative;
import paulevs.creative.CreativePlayer;

@Mixin(ContainerBase.class)
public abstract class ContainerBaseMixin extends ScreenBase {

    @Shadow
    protected abstract Slot getSlot(int mouseX, int mouseY);

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void creative_mouseClick(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (button != 2 || !((CreativePlayer) minecraft.player).isCreative())
            return;

        Slot slot = getSlot(mouseX, mouseY);
        if (slot != null && slot.hasItem()) {
            ItemInstance item = slot.getItem();
            if (!minecraft.level.isClient)
                minecraft.player.inventory.setCursorItem(new ItemInstance(item.itemId, item.getType()
                        .getMaxStackSize(), item.getDamage()));
            else {
                Message message = new Message(Creative.duplicateItemStackPacket);
                message.ints = new int[]{item.itemId, item.count, item.getDamage()};
                PacketHelper.send(message);
            }
        }
    }
}
