package paulevs.creative.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Grass;
import net.minecraft.block.material.Material;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.creative.Utility;

@Mixin(Grass.class)
public class GrassMixin extends BlockBase {

    protected GrassMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    public int getTextureForSide(int side) {
        if(Utility.isMinecraftInitialized) {
            if (side == 1) {
                Utility.SimpleColor grassColor = Utility.getGrassColor();
                GL11.glColor3f(grassColor.r, grassColor.g, grassColor.b);
            } else {
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
            }
        }
        return side == 0 ? 2 : side == 1 ? 0 : 3;
    }
}
