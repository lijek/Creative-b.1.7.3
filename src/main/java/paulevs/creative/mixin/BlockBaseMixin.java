package paulevs.creative.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(BlockBase.class)
public class BlockBaseMixin {

    @Inject(method = "getHardness()F", at = @At("HEAD"), cancellable = true)
    private void creative_getHardness(CallbackInfoReturnable<Float> cir){
        if(FabricLoader.getInstance().getGameInstance() instanceof Minecraft){
            Minecraft game = (Minecraft) FabricLoader.getInstance().getGameInstance();
            if(!game.level.isClient){
                PlayerBase player = game.player;
                if(player instanceof CreativePlayer){
                    if(((CreativePlayer) player).isCreative()){
                        cir.setReturnValue(0F);
                        cir.cancel();
                    }
                }
            }
        }
    }

    /*@Inject(at = @At("INVOKE"), method = "getHardness(Lnet/minecraft/entity/player/PlayerBase;)F", cancellable = true)
    private void creative_getHardness(PlayerBase arg, CallbackInfoReturnable<Float> cir){
        System.out.println("wywolano gethardness");
        if(arg instanceof CreativePlayer){
            System.out.println("jest");
            if(((CreativePlayer) arg).isCreative()){
                System.out.println("tak");
                cir.setReturnValue(Float.POSITIVE_INFINITY);
                cir.cancel();
            }
        }
    }*/
}
