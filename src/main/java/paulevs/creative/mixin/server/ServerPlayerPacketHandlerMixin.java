package paulevs.creative.mixin.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulevs.creative.CreativePlayer;

@Mixin(ServerPlayerPacketHandler.class)
public class ServerPlayerPacketHandlerMixin {

    @Shadow private ServerPlayer serverPlayer;

    @Redirect(method = "onBaseOnGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;method_206(Lnet/minecraft/util/maths/Box;)Z"))
    private boolean creative_skipFlyKick(ServerLevel serverLevel, Box box){
        if(((CreativePlayer) serverPlayer).isCreative())
            return true;
        else
            return serverLevel.method_206(box);
    }


    private void creative_playerDigging(){

    }
}
