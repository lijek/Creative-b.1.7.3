package paulevs.creative.mixin;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.CreativePlayer;

@Mixin(ContainerBase.class)
public abstract class ContainerBaseMixin extends ScreenBase {

    @Shadow protected abstract Slot getSlot(int mouseX, int mouseY);

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void creative_mouseClick(int mouseX, int mouseY, int button, CallbackInfo ci){
        if(((CreativePlayer)minecraft.player).isCreative() && button == 2){
            Slot slot = getSlot(mouseX, mouseY);
            if(slot != null && slot.hasItem()){
                ItemInstance item = slot.getItem();
                minecraft.player.inventory.setCursorItem(new ItemInstance(item.itemId, item.getType().getMaxStackSize(), item.getDamage()));
            }
        }
    }
}
