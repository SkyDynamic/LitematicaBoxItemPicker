package dev.skydynamic.litematicaboxitempicker;

import java.util.List;
import com.google.common.collect.ImmutableList;
import dev.skydynamic.litematicaboxitempicker.config.Configs;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;

public class LitematicaShulkerBoxPickerInputHandler implements IKeybindProvider{

    private static final LitematicaShulkerBoxPickerInputHandler INSTANCE = new LitematicaShulkerBoxPickerInputHandler();

    private LitematicaShulkerBoxPickerInputHandler() {
        super();
    }

    public static LitematicaShulkerBoxPickerInputHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        manager.addKeybindToMap(Configs.Hotkeys.OPEN_GUI_MAIN_MENU.getKeybind());
        manager.addKeybindToMap(Configs.Hotkeys.ENABLE_LSBP.getKeybind());
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        List<? extends IHotkey> hotkeys = ImmutableList.of(
            Configs.Hotkeys.OPEN_GUI_MAIN_MENU,
            Configs.Hotkeys.ENABLE_LSBP
        );
        manager.addHotkeysForCategory(Reference.MOD_NAME, "hotkeys.category.generic_hotkeys", hotkeys);
    }

}
