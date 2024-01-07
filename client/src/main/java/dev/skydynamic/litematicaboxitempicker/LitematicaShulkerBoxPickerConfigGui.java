package dev.skydynamic.litematicaboxitempicker;

import java.util.Collections;
import java.util.List;

import dev.skydynamic.litematicaboxitempicker.config.Configs;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;

public class LitematicaShulkerBoxPickerConfigGui extends GuiConfigsBase{

    private static ConfigGuiTab tab = ConfigGuiTab.GENERIC;

    public LitematicaShulkerBoxPickerConfigGui()
    {
        super(10, 50, Reference.MOD_ID, null, "LazyShulkerBoxConfig");
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.clearOptions();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values())
        {
            x += this.createButton(x, y, -1, tab) + 2;
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(LitematicaShulkerBoxPickerConfigGui.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth();
    }

    @Override
    protected int getConfigWidth()
    {
        ConfigGuiTab tab = LitematicaShulkerBoxPickerConfigGui.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            return 200;
        }

        return super.getConfigWidth();
    }

    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs()
    {
        List<? extends IConfigBase> configs;
        ConfigGuiTab tab = LitematicaShulkerBoxPickerConfigGui.tab;

        if (tab == ConfigGuiTab.GENERIC) {
            configs = Configs.Generic.OPTIONS;
        } else if (tab == ConfigGuiTab.HOTKEYS){
            configs = Configs.Hotkeys.HOTKEY_LIST;
        } else {
            return Collections.emptyList();
        }

        return ConfigOptionWrapper.createFor(configs);
    }

    private static class ButtonListener implements IButtonActionListener
    {
        private final LitematicaShulkerBoxPickerConfigGui parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, LitematicaShulkerBoxPickerConfigGui parent)
        {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            LitematicaShulkerBoxPickerConfigGui.tab = this.tab;

            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    public enum ConfigGuiTab
    {
        GENERIC ("基础"),
        HOTKEYS ("热键");

        private final String translationKey;

        ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }

}
