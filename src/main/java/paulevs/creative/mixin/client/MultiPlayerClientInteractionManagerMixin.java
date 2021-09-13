package paulevs.creative.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiPlayerClientInteractionManager;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.play.PlayerDigging0xEC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(MultiPlayerClientInteractionManager.class)
public class MultiPlayerClientInteractionManagerMixin extends BaseClientInteractionManager {

    @Shadow private ClientPlayNetworkHandler networkHandler;
    private int blocksToRemove = 0;

    public MultiPlayerClientInteractionManagerMixin(Minecraft minecraft) {
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
        this.networkHandler.sendPacket(new PlayerDigging0xEC2SPacket(0, i, j, k, i1));
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
            this.networkHandler.sendPacket(new PlayerDigging0xEC2SPacket(0, i, j, k, i1));
            creative_clickBlockCreative(i, j, k, i1);
        }
        ci.cancel();
    }

    @Inject(method = "method_1713", at = @At("HEAD"), cancellable = true)
    private void creative_placeBlock(PlayerBase arg, Level arg1, ItemInstance item, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir){
        if(!((CreativePlayer) arg).isCreative())
            return;
        if(item != null) {
            int count = item.count;
            int damage = item.getDamage();
            cir.setReturnValue(item.useOnTile(arg, arg1, i, j, k, i1));
            item.count = count;
            item.setDamage(damage);
            cir.cancel();
        }
    }
}
