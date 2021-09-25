package paulevs.creative.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.PlayerKeypressManager;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.creative.Creative;
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
				Message message = new Message(Creative.toggleFlyPacket);
				message.booleans = new boolean[]{player.isFlying()};
				PacketHelper.send(message);
				creative_FlyToggleTimer = 0;
			}
		}

		creative_lastJumpPressed = playerKeypressManager.jump;
	}
}
