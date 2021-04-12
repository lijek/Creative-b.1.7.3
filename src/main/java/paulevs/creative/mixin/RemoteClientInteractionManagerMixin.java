package paulevs.creative.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RemoteClientInteractionManager;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.CreativePlayer;

@Mixin(RemoteClientInteractionManager.class)
public abstract class RemoteClientInteractionManagerMixin extends ClientInteractionManager {

    private int blocksToRemove = 0;

    public RemoteClientInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    private void creative_clickBlockCreative(int i, int j, int k, int i1){
        minecraft.level.method_172(minecraft.player, i, j, k, i1);
        Level var5 = this.minecraft.level;
        BlockBase var6 = BlockBase.BY_ID[var5.getTileId(i, j, k)];
        var5.playLevelEvent(2001, i, j, k, var6.id + var5.getTileMeta(i, j, k) * 256);
        int var7 = var5.getTileMeta(i, j, k);
        boolean var8 = var5.setTile(i, j, k, 0);
        if (var6 != null && var8) {
            var6.method_1612(var5, i, j, k, var7);
        }
    }

    @Inject(method = "method_1707", at = @At("HEAD"), cancellable = true)
    private void creative_clickBlock(int i, int j, int k, int i1, CallbackInfo ci){
        if(!((CreativePlayer)minecraft.player).isCreative())
            return;
        creative_clickBlockCreative(i, j, k, i1);
        blocksToRemove = 5;
        ci.cancel();
    }

    @Inject(method = "method_1721", at = @At("HEAD"), cancellable = true)
    private void creative_sendBlockRemoving(int i, int j, int k, int i1, CallbackInfo ci){
        if(!((CreativePlayer)minecraft.player).isCreative())
            return;
        --this.blocksToRemove;
        if (this.blocksToRemove <= 0) {
            this.blocksToRemove = 5;
            creative_clickBlockCreative(i, j, k, i1);
        }
        ci.cancel();
    }
}
