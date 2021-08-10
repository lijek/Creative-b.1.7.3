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
        Level level = this.minecraft.level;
        BlockBase block = BlockBase.BY_ID[level.getTileId(i, j, k)];
        if(block == null)
            return;
        level.playLevelEvent(2001, i, j, k, block.id + level.getTileMeta(i, j, k) * 256);
        int meta = level.getTileMeta(i, j, k);
        boolean var8 = level.setTile(i, j, k, 0);
        if (block != null && var8) {
            block.activate(level, i, j, k, meta);
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
