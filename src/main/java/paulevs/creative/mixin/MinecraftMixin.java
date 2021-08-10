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

	@Inject(method = "method_2103", at = @At("HEAD"))
	private void creative_clickMiddleMouseButton(CallbackInfo ci){
		ItemInstance newItem = creative_getItemFromHitResult();
		player.inventory.main[player.inventory.selectedHotbarSlot] = newItem;
	}

	private ItemInstance creative_getItemFromHitResult(){
		int blockID, meta;
		if(hitResult != null) {
			blockID = level.getTileId(this.hitResult.x, this.hitResult.y, this.hitResult.z);

			if (blockID == BlockBase.GRASS.id) {
				blockID = BlockBase.DIRT.id;
			}

			if (blockID == BlockBase.DOUBLE_STONE_SLAB.id) {
				blockID = BlockBase.STONE_SLAB.id;
			}

			if (blockID == BlockBase.BEDROCK.id) {
				blockID = BlockBase.STONE.id;
			}
		}else{
			blockID = 0;
		}

		if(blockID == 0)
			return null;
		else
			return new ItemInstance(blockID, 1, 0);
	}
}
