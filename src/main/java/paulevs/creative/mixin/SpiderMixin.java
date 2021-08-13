package paulevs.creative.mixin;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.MonsterBase;
import net.minecraft.entity.monster.Spider;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulevs.creative.CreativePlayer;

@Mixin(Spider.class)
public class SpiderMixin extends MonsterBase {

    public SpiderMixin(Level arg) {
        super(arg);
    }

    @Redirect(method = "getAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getClosestPlayerTo(Lnet/minecraft/entity/EntityBase;D)Lnet/minecraft/entity/player/PlayerBase;"))
    private PlayerBase creative_getClosestVulnerablePlayer(Level level, EntityBase entity, double maxDistance){
        double distance = -1D;
        PlayerBase player = null;

        for (int i = 0; i < level.players.size(); i++)
        {
            PlayerBase player1 = (PlayerBase)level.players.get(i);

            if (((CreativePlayer)player1).isCreative())
            {
                continue;
            }

            double d1 = player1.squaredDistanceTo(entity.x, entity.y, entity.z);

            if ((maxDistance < 0.0D || d1 < maxDistance * maxDistance) && (distance == -1D || d1 < distance))
            {
                distance = d1;
                player = player1;
            }
        }

        return player;
    }
}
