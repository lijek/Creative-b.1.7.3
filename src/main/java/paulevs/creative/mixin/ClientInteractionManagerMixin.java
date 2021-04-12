package paulevs.creative.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin  {
	@Final
	@Shadow
	protected Minecraft minecraft;
	
	@Inject(method = "method_1722", at = @At("HEAD"), cancellable = true)
	private void creative_renderHud(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(!((CreativePlayer) minecraft.player).isCreative());
	}

	@Inject(method = "method_1713", at = @At("HEAD"), cancellable = true)
	private void creative_onPlayerRightClick(PlayerBase player, Level level, ItemInstance itemInstance, int x, int y, int z, int i1, CallbackInfoReturnable<Boolean> cir){
		if(!((CreativePlayer) minecraft.player).isCreative())
			return;

		int i = level.getTileId(x, y, z);

		if (i > 0 && BlockBase.BY_ID[i].canUse(level, x, y, z, player))
		{
			cir.setReturnValue(true);
			cir.cancel();
		}

		if (itemInstance == null)
		{
			cir.setReturnValue(false);
			cir.cancel();
		}
		else
		{
			int j = itemInstance.getDamage();
			int k = itemInstance.count;
			boolean flag = itemInstance.useOnTile(player, level, x, y, z, i1);
			itemInstance.setDamage(j);
			itemInstance.count = k;
			cir.setReturnValue(flag);
			cir.cancel();
		}
	}
}
