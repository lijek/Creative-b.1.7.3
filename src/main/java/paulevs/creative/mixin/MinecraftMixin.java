package paulevs.creative.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.creative.CreativePlayer;
import paulevs.creative.api.CreativeTabs;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow public HitResult hitResult;

	@Shadow public Level level;

	@Shadow public AbstractClientPlayer player;

	@Inject(method = "init", at = @At("TAIL"))
	private void creative_init(CallbackInfo info) {
		CreativeTabs.initVanilla();
		CreativeTabs.initTabs();
	}

	@Inject(method = "method_2103", at = @At("HEAD"), cancellable = true)
	private void creative_clickMiddleMouseButton(CallbackInfo ci){
		if(((CreativePlayer)player).isCreative()) {
			int targetSlot = -1;
			ItemInstance newItem = creative_getItemFromHitResult();
			if(newItem == null) {
				ci.cancel();
				return;
			}
			for(int i = 0; i < 9; i++) {
				if (player.inventory.main[i] == null) {
					if(targetSlot == -1)
						targetSlot = i;
					continue;
				}
				ItemInstance item = player.inventory.main[i];
				if (item.itemId == newItem.itemId && item.getDamage() == newItem.getDamage()) {
					player.inventory.selectedHotbarSlot = i;
					ci.cancel();
					return;
				}
			}
			if(targetSlot == -1)
				targetSlot = player.inventory.selectedHotbarSlot;
			player.inventory.selectedHotbarSlot = targetSlot;
			player.inventory.main[targetSlot] = newItem;
			ci.cancel();
		}
	}

	private ItemInstance creative_getItemFromHitResult(){
		int blockID, meta;
		if(hitResult != null) {
			blockID = level.getTileId(this.hitResult.x, this.hitResult.y, this.hitResult.z);
			meta = ((BlockBaseInvoker)BlockBase.BY_ID[blockID]).droppedMetaInvoker(level.getTileMeta(this.hitResult.x, this.hitResult.y, this.hitResult.z));
		}else{
			blockID = 0;
			meta = 0;
		}

		if(blockID == 0)
			return null;
		else
			return new ItemInstance(blockID, 1, meta);
	}
}
