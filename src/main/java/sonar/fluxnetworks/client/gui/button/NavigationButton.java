package sonar.fluxnetworks.client.gui.button;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import sonar.fluxnetworks.api.gui.EnumNavigationTabs;
import sonar.fluxnetworks.client.gui.ScreenUtils;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.basic.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class NavigationButton extends GuiButtonCore {

    public EnumNavigationTabs tab;
    public boolean isCurrentTab = false;

    public NavigationButton(int x, int y, EnumNavigationTabs tab) {
        super(x, y, 16, 16, 0);
        this.tab = tab;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, int guiLeft, int guiTop) {

        GlStateManager.pushMatrix();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        mc.getTextureManager().bindTexture(ScreenUtils.BUTTONS);
        blit(x, y, 16 * tab.ordinal(), 16 * getHoverState(isCurrentTab || isMouseHovered(mc, mouseX, mouseY)), 16, 16);

        if(isMouseHovered(mc, mouseX-guiLeft, mouseY-guiTop)) {
            FontRenderer fontRenderer = mc.fontRenderer;
            String text = tab.getTranslatedName();
            fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2 + 8,  + y - 10, 0xFFFFFF);
        }
        GlStateManager.popMatrix();
    }

    public NavigationButton setMain() {
        isCurrentTab = true;
        return this;
    }

}
