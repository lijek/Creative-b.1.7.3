package paulevs.creative.mixin.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.CreativePlayer;


@Mixin(class_70.class)
public abstract class class_70Mixin {

    @Shadow public PlayerBase field_2309;

    @Shadow private ServerLevel field_2310;

    @Shadow public abstract boolean method_1834(int i, int j, int k);

    @Inject(method = "method_1830", at = @At(target = "Lnet/minecraft/server/level/ServerLevel;getTileId(III)I", value = "INVOKE", shift = At.Shift.BEFORE), cancellable = true)
    private void creative_onBlockClicked(int i, int j, int k, int i1, CallbackInfo ci){
        if(!((CreativePlayer) field_2309).isCreative())
            return;
        int blockID = field_2310.getTileId(i, j, k);
        if (blockID > 0) {
            BlockBase.BY_ID[blockID].activate(field_2310, i, j, k, field_2309);
        }
        method_1834(i, j, k);
        ci.cancel();
    }

    @Redirect(method = "method_1831", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;use(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;)Lnet/minecraft/item/ItemInstance;"))
    private ItemInstance creative_revertItemOnUse(ItemInstance itemInstance, Level arg, PlayerBase arg1){
        if(!((CreativePlayer)arg).isCreative()){
            itemInstance.use(arg, arg1);
            return itemInstance;
        }

        int count = itemInstance.count;
        int damage = itemInstance.getDamage();

        itemInstance.use(arg, arg1);

        itemInstance.count = count;
        if(itemInstance.hasDurability())
            itemInstance.setDamage(damage);

        return itemInstance;
    }

    @Redirect(method = "method_1832", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;useOnTile(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;IIII)Z"))
    private boolean creative_revertItemOnUseOnTile(ItemInstance itemInstance, PlayerBase arg, Level arg1, int x, int y, int z, int i1){
        if(!((CreativePlayer)arg).isCreative())
            return itemInstance.useOnTile(arg, arg1, x, y, z, i1);
        int count = itemInstance.count;
        int damage = itemInstance.getDamage();
        boolean returnValue = itemInstance.useOnTile(arg, arg1, x, y, z, i1);
        itemInstance.count = count;
        if(itemInstance.hasDurability())
            itemInstance.setDamage(damage);

        return returnValue;
    }
}
