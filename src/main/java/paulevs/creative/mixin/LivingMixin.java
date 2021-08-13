package paulevs.creative.mixin;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulevs.creative.CreativePlayer;

@Mixin(Living.class)
public abstract class LivingMixin extends EntityBase {

    @Shadow public abstract boolean method_932();

    public LivingMixin(Level level) {
        super(level);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_1334()Z"))
    private boolean creative_travel_isInWater(Living living){
        if(this instanceof CreativePlayer && ((CreativePlayer)this).isFlying())
            return false;
        return this.method_1334();
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_1335()Z"))
    private boolean creative_travel_isInLava(Living living){
        if(this instanceof CreativePlayer && ((CreativePlayer)this).isFlying())
            return false;
        return this.method_1335();
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_932()Z"))
    private boolean creative_travel_isOnLadder(Living living){
        if(this instanceof CreativePlayer && ((CreativePlayer)this).isFlying())
            return false;
        return this.method_932();
    }
}
