package dev.skydynamic.litematicaboxitempicker.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.skydynamic.litematicaboxitempicker.Reference;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Generic {
        public static final ConfigBoolean ENABLE_LSBP = new ConfigBoolean("开启自动从盒子取出物品", false , "当开启后，使用 Litematica 模组的 轻松放置模式 时,\n若背包中物品不足,则会自动从盒子中取出");
        public static final ConfigInteger LSBP_COUNT = new ConfigInteger("取出数量", 32, 1, 64, "从潜影盒取出一次物品时的数量");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            ENABLE_LSBP,
            LSBP_COUNT
        );
    }

    public static class Hotkeys {

        public static final ConfigHotkey OPEN_GUI_MAIN_MENU = new ConfigHotkey("打开LSBP菜单","X,C", KeybindSettings.RELEASE_EXCLUSIVE, "打开LSBP设置菜单");
        public static final ConfigHotkey ENABLE_LSBP = new ConfigHotkey("开启自动盒子补货", "", KeybindSettings.RELEASE_EXCLUSIVE, "开启自动盒子补货");

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_GUI_MAIN_MENU,
            ENABLE_LSBP
        );

    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
            }
        }

    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
        }
    }

    @Override
    public void onConfigsChanged()
    {
        saveToFile();
        loadFromFile();
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }

}
