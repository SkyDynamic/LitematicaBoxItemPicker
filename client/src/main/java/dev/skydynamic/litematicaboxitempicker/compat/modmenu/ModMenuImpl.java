package dev.skydynamic.litematicaboxitempicker.compat.modmenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.skydynamic.litematicaboxitempicker.LitematicaShulkerBoxPickerConfigGui;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) ->{
            LitematicaShulkerBoxPickerConfigGui gui = new LitematicaShulkerBoxPickerConfigGui();
            gui.setParent(screen);
            return gui;
        };
    }
}
