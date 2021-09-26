package paulevs.creative;

import net.minecraft.client.render.block.GrassColour;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;

public class Utility {

	public static boolean isMinecraftInitialized = false;
	
	public static int getColor(int r, int g, int b, int a) {
		return a << 24 | r << 16 | g << 8 | b;
	}

	public static boolean isEmpty(ItemInstance item){
		return item == null || ItemBase.byId[item.itemId] == null;
	}

	public static SimpleColor getGrassColor(){
		int colorRGB = GrassColour.get(0.75F, 0.75F);
		float r = ((colorRGB >> 16) & 255) / 255F;
		float g = ((colorRGB >> 8) & 255) / 255F;
		float b = (colorRGB & 255) / 255F;
		return new SimpleColor(r, g, b);
	}

	public static final class SimpleColor {
		public final float r;
		public final float g;
		public final float b;

		public SimpleColor(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
}
