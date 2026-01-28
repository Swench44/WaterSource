package dev.swench.watersource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Config {
    public enum HighlightColor {
        CYAN("color.watersource.cyan", 0x00FFFF),
        RED("color.watersource.red", 0xFF0000),
        GREEN("color.watersource.green", 0x00FF00),
        BLUE("color.watersource.blue", 0x0000FF),
        YELLOW("color.watersource.yellow", 0xFFFF00),
        PURPLE("color.watersource.purple", 0x800080),
        WHITE("color.watersource.white", 0xFFFFFF);

        private final String translationKey;
        private final int value;

        HighlightColor(String translationKey, int value) {
            this.translationKey = translationKey;
            this.value = value;
        }

        public String getTranslationKey() { return translationKey; }
        public int getValue() { return value; }
    }

    public static boolean waterEnabled = true;
    public static int waterRadius = 30;
    public static HighlightColor waterColor = HighlightColor.RED;
    public static boolean waterFill = false;
    public static int waterAlpha = 0;

    public static boolean lavaEnabled = true;
    public static int lavaRadius = 30;
    public static HighlightColor lavaColor = HighlightColor.CYAN;
    public static boolean lavaFill = false;
    public static int lavaAlpha = 0;

    public static KeyBinding configKeyBinding;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    private static class ConfigData {
        boolean waterEnabled = false;
        int waterRadius = 30;
        HighlightColor waterColor = HighlightColor.RED;
        boolean waterFill = false;
        int waterAlpha = 0;

        boolean lavaEnabled = false;
        int lavaRadius = 30;
        HighlightColor lavaColor = HighlightColor.CYAN;
        boolean lavaFill = false;
        int lavaAlpha = 0;
    }

    public static void init() {
        if (configFile == null) {
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null && client.runDirectory != null) {
                    Path configDir = client.runDirectory.toPath().resolve("config");
                    configDir.toFile().mkdirs();
                    configFile = configDir.resolve("watersource.json").toFile();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void save() {
        init();
        if (configFile == null) return;
        try {
            ConfigData data = new ConfigData();
            data.waterEnabled = waterEnabled;
            data.waterRadius = waterRadius;
            data.waterColor = waterColor;
            data.waterFill = waterFill;
            data.waterAlpha = waterAlpha;

            data.lavaEnabled = lavaEnabled;
            data.lavaRadius = lavaRadius;
            data.lavaColor = lavaColor;
            data.lavaFill = lavaFill;
            data.lavaAlpha = lavaAlpha;

            FileWriter writer = new FileWriter(configFile);
            GSON.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
        }
    }

    public static void load() {
        init();
        if (configFile == null || !configFile.exists()) {
            waterEnabled = false;
            waterRadius = 30;
            waterColor = HighlightColor.RED;
            waterFill = false;
            waterAlpha = 0;

            lavaEnabled = false;
            lavaRadius = 30;
            lavaColor = HighlightColor.CYAN;
            lavaFill = false;
            lavaAlpha = 0;

            save();
            return;
        }

        try {
            FileReader reader = new FileReader(configFile);
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            reader.close();

            if (data != null) {
                waterEnabled = data.waterEnabled;
                waterRadius = Math.max(10, Math.min(100, data.waterRadius));
                waterColor = data.waterColor != null ? data.waterColor : HighlightColor.RED;
                waterFill = data.waterFill;
                waterAlpha = Math.max(0, Math.min(100, data.waterAlpha));

                lavaEnabled = data.lavaEnabled;
                lavaRadius = Math.max(10, Math.min(100, data.lavaRadius));
                lavaColor = data.lavaColor != null ? data.lavaColor : HighlightColor.CYAN;
                lavaFill = data.lavaFill;
                lavaAlpha = Math.max(0, Math.min(100, data.lavaAlpha));
            }
        } catch (IOException e) {
        }
    }
}
