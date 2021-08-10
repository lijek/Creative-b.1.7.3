package paulevs.creative.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.PlayerKeypressManager;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.CreativePlayer;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends PlayerBase {

	private int creative_FlyToggleTimer = 0;
	private boolean creative_lastJumpPressed = false;


	public AbstractClientPlayerMixin(Level arg) {
		super(arg);
	}
	
	@Shadow
	protected Minecraft minecraft;

	@Shadow public PlayerKeypressManager playerKeypressManager;

	@Inject(method = "getCanSuffocate", at = @At("HEAD"), cancellable = true)
	private void creative_getCanSuffocate(int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (((CreativePlayer) this).isCreative()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
	public void creative_onKeyPress(int i, boolean flag, CallbackInfo info) {
		CreativePlayer player = (CreativePlayer) this;
		if (player.isCreative()) {
			/*boolean keyPressed = flag && i == minecraft.options.jumpKey.key;

			if(!keyEnabled){
				if(!moveKeyPressed && keyPressed){
					long now = System.currentTimeMillis();

					if(now > startTime + 300){
						startTime = now;
					}else{
						keyEnabled = true;
						boolean fly = !player.isFlying();
						player.setFlying(fly);
						if (fly) {
							AbstractClientPlayer client = (AbstractClientPlayer) (Object) this;
							client.setPositionAndAngles(client.x, client.y - client.standingEyeHeight + 0.01, client.z, client.yaw, client.pitch);
							client.velocityY = client.velocityY * 0.6D;
						}
						//info.cancel();
					}

					moveKeyPressed = true;
				}else{ // optional
					if(!moveKeyPressed && keyPressed && System.currentTimeMillis() > startTime + 300){
						keyEnabled = false;
						// do stuff to cancel double tap action if key is pressed again...
						// useful if you need to allow the player to override the keypress and cancel the double tap action
					}
				}

				// detect key release
				if(!keyPressed){
					moveKeyPressed = false;
				}
			}*/

			/*if (flag && i == ((FlyOption) minecraft.options).getFlyKey().key) {
				boolean fly = !player.isFlying();
				player.setFlying(fly);
				if (fly) {
					AbstractClientPlayer client = (AbstractClientPlayer) (Object) this;
					client.setPositionAndAngles(client.x, client.y - client.standingEyeHeight + 0.01, client.z, client.yaw, client.pitch);
					client.velocityY = client.velocityY * 0.6D;
				}
				info.cancel();
			}*/
		}
	}

	@Inject(method = "updateDespawnCounter", at = @At("TAIL"))
	private void creative_livingUpdate(CallbackInfo ci){
		CreativePlayer player = (CreativePlayer) this;
		if(!player.isCreative())
			return;

		if (creative_FlyToggleTimer > 0)
			--creative_FlyToggleTimer;

		if(playerKeypressManager.jump && !creative_lastJumpPressed){
			if(creative_FlyToggleTimer == 0)
				creative_FlyToggleTimer = 7;
			else{
				boolean fly = !player.isFlying();
				player.setFlying(fly);
				creative_FlyToggleTimer = 0;
			}
		}

		creative_lastJumpPressed = playerKeypressManager.jump;
	}
}
