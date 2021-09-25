package paulevs.creative.gui;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.util.CharacterUtils;
import org.lwjgl.opengl.GL11;

public class SearchTextbox extends DrawableHelper {
    private final TextRenderer font;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private String text;
    private int maxLength;
    private int ticksRan;
    public boolean selected = false;
    public boolean enabled = true;
    private ScreenBase parentScreen;

    public SearchTextbox(ScreenBase screen, TextRenderer font, int x, int y, int width, int height) {
        this.parentScreen = screen;
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setText("");
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void tick() {
        ++this.ticksRan;
    }

    public void keyPressed(char c, int i) {
        if (this.enabled && this.selected) {
            if (c == '\t') {
                this.parentScreen.handleTab();
            }

            if (c == 22) {
                String var3 = ScreenBase.getClipboardContents();
                if (var3 == null) {
                    var3 = "";
                }

                int var4 = 32 - this.text.length();
                if (var4 > var3.length()) {
                    var4 = var3.length();
                }

                if (var4 > 0) {
                    this.text = this.text + var3.substring(0, var4);
                }
            }

            if (i == 14 && this.text.length() > 0) {
                this.text = this.text.substring(0, this.text.length() - 1);
            }

            if (CharacterUtils.validCharacters.indexOf(c) >= 0 && (this.text.length() < this.maxLength || this.maxLength == 0)) {
                this.text = this.text + c;
            }

        }
    }

    public void mouseClicked(int i, int j, int k) {
        boolean var4 = this.enabled && i >= this.x && i < this.x + this.width && j >= this.y && j < this.y + this.height;
        this.setSelected(var4);
    }

    public void setSelected(boolean flag) {
        if (flag && !this.selected) {
            this.ticksRan = 0;
        }

        this.selected = flag;
    }

    public void draw() {
        /*this.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
        this.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);*/
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if (this.enabled) {
            boolean var1 = this.selected && this.ticksRan / 6 % 2 == 0;
            this.drawTextWithShadow(this.font, this.text + (var1 ? "_" : ""), this.x + 4, this.y + (this.height - 8) / 2, -1);
        } else {
            this.drawTextWithShadow(this.font, this.text, this.x + 4, this.y + (this.height - 8) / 2, 7368816);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);

    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
