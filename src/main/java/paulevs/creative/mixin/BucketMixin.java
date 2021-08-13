package paulevs.creative.mixin;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Bucket;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(Bucket.class)
public class BucketMixin {

    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void creative_use(ItemInstance item, Level level, PlayerBase player, CallbackInfoReturnable<ItemInstance> cir){
        if(((CreativePlayer)player).isCreative())
            cir.setReturnValue(item);
    }
}
