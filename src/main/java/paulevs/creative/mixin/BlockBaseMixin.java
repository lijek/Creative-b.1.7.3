package paulevs.creative.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.CreativePlayer;

@Mixin(BlockBase.class)
public abstract class BlockBaseMixin {

    @Inject(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;drop(Lnet/minecraft/level/Level;IIII)V", shift = At.Shift.BEFORE), cancellable = true)
    private void creative_cancelDrop(Level level, PlayerBase playerBase, int x, int y, int z, int meta, CallbackInfo ci){
        if(!((CreativePlayer)playerBase).isCreative())
            return;

        ci.cancel();
    }

}
