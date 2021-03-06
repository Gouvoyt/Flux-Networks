package sonar.fluxnetworks.client.gui.basic;

import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.gui.EnumNavigationTabs;
import sonar.fluxnetworks.client.gui.button.FluxTextWidget;
import sonar.fluxnetworks.client.gui.popups.PopUpCore;
import sonar.fluxnetworks.common.registry.RegistrySounds;

import javax.annotation.Nullable;

/**fixes some of the vanilla FocusableGui implementation*/
public abstract class GuiFocusable<T extends Container> extends GuiDraw<T> {

    public GuiFocusable(T container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    /**de-focus other text elements**/
    @Override
    public void setFocused(@Nullable IGuiEventListener listener) {
        super.setFocused(listener);
        children.forEach(child -> {
            if(child != listener && child instanceof FluxTextWidget){
                FluxTextWidget textWidget = (FluxTextWidget) child;
                if(textWidget.isFocused()) {
                    ((FluxTextWidget) child).setFocused2(false);
                }
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
        if (getFocused() != null) {
            if(keyCode == 256) {
                this.setFocused(null);
                return true;
            }
            if(this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
                return false; //allows the typing of "E"
            }
        }else if(keyCode == 256 || this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)){
            if(this instanceof PopUpCore){
                ((PopUpCore) this).host.closePopUp();
                return true;
            }
            if(this instanceof GuiTabCore){
                GuiTabCore core = (GuiTabCore) this;
                if(core.getNavigationTab() == EnumNavigationTabs.TAB_HOME){
                    minecraft.player.closeScreen();
                }else {
                    GuiTabCore.switchTab(EnumNavigationTabs.TAB_HOME, core.player, core.connector);
                    if (FluxConfig.enableButtonSound) {
                        minecraft.getSoundHandler().play(SimpleSound.master(RegistrySounds.BUTTON_CLICK, 1.0F));
                    }
                }
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
