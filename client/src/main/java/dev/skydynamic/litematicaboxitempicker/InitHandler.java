package dev.skydynamic.litematicaboxitempicker;

import dev.skydynamic.litematicaboxitempicker.config.Configs;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());
        InputEventHandler.getKeybindManager().registerKeybindProvider(LitematicaShulkerBoxPickerInputHandler.getInstance());

        Configs.Hotkeys.OPEN_GUI_MAIN_MENU.getKeybind().setCallback(new KeyCallbackHotkeys());
        Configs.Hotkeys.ENABLE_LSBP.getKeybind().setCallback(new KeyCallbackToggleBooleanConfigWithMessage(Configs.Generic.ENABLE_LSBP));
    }

    private static class KeyCallbackHotkeys implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (key == Configs.Hotkeys.OPEN_GUI_MAIN_MENU.getKeybind()) {
                GuiBase.openGui(new LitematicaShulkerBoxPickerConfigGui());
            }
            return true;
        }
    }
}
