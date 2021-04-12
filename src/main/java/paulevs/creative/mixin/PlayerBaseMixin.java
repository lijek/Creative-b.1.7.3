package paulevs.creative.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(PlayerBase.class)
public abstract class PlayerBaseMixin extends Living implements CreativePlayer {
	private boolean isCreative;
	private boolean flying;
	private int jumpTicks;
	
	public PlayerBaseMixin(Level arg) {
		super(arg);
	}
	
	@Override
	public boolean isCreative() {
		return isCreative;
	}
	
	@Override
	public void setCreative(boolean creative) {
		this.isCreative = creative;
	}
	
	@Override
	public boolean isFlying() {
		return flying;
	}
	
	@Override
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	
	@Override
	public int getJumpTicks() {
		return jumpTicks;
	}
	
	@Override
	public void setJumpTicks(int ticks) {
		this.jumpTicks = ticks;
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void creative_damage(EntityBase target, int amount, CallbackInfoReturnable<Boolean> info) {
		if (this.isCreative()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void creative_applyDamage(int damageAmount, CallbackInfo info) {
		if (this.isCreative()) {
			info.cancel();
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void creative_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("Creative", isCreative());
		tag.put("Flying", isFlying());
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void creative_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		setCreative(tag.getBoolean("Creative"));
		setFlying(tag.getBoolean("Flying"));
	}

	@Override
	public void movementInputToVelocity(float f, float f1, float f2){
		float f3 = MathHelper.sqrt(f * f + f1 * f1);
		if (f3 >= 0.01F) {
			if (f3 < 1.0F) {
				f3 = 1.0F;
			}

			f3 = f2 / f3;
			f *= f3;
			f1 *= f3;
			float f4 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
			float f5 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);
			double speed = 1;

			if(this.isFlying())
				speed = 2;

			this.velocityX += (double) (f * f5 - f1 * f4) * speed;
			this.velocityZ += (double) (f1 * f5 + f * f4) * speed;
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void creative_tick(CallbackInfo info) {
		if (this.onGround || !this.isCreative()) {
			this.setFlying(false);
		}
		if (this.isCreative()) {
			if (this.isFlying() && !this.isSleeping()) {
				if (this.jumping) {
					this.velocityY = 0.5;
				}
				else if (this.method_1373()) {
					this.velocityY = -0.5;
				}
				else
					this.velocityY = 0;
				
				if (this.onGround) {
					this.setFlying(false);
					this.velocityX = 0;
					this.velocityY = 0;
					this.velocityZ = 0;
				}
			}
		}
	}
	
	@Inject(method = "canRemoveBlock", at = @At("HEAD"), cancellable = true)
	private void creative_canRemoveBlock(BlockBase block, CallbackInfoReturnable<Boolean> info) {
		if (this.isCreative()) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "getStrengh", at = @At("HEAD"), cancellable = true)
	private void creative_getStrengh(BlockBase block, CallbackInfoReturnable<Float> info) {
		if (this.isCreative()) {
			info.setReturnValue(Float.MAX_VALUE);
			info.cancel();
		}
	}
}
