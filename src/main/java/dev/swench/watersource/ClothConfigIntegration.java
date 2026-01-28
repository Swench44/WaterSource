package dev.swench.watersource;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;

public class ClothConfigIntegration {
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new TranslatableText("watersource.config.title"))
            .setSavingRunnable(Config::save);
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        ConfigCategory water = builder.getOrCreateCategory(new TranslatableText("watersource.config.category.water"));
        
        water.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("watersource.config.water_enabled"), Config.waterEnabled)
            .setDefaultValue(true)
            .setSaveConsumer(value -> Config.waterEnabled = value)
            .build());

        water.addEntry(entryBuilder.startIntSlider(new TranslatableText("watersource.config.water_radius"), Config.waterRadius, 10, 100)
                .setDefaultValue(30)
                .setSaveConsumer(value -> Config.waterRadius = value)
                .build());
        
        water.addEntry(entryBuilder.startEnumSelector(new TranslatableText("watersource.config.water_color"), Config.HighlightColor.class, Config.waterColor)
            .setDefaultValue(Config.HighlightColor.RED)
            .setEnumNameProvider(color -> {
                Config.HighlightColor hc = (Config.HighlightColor) color;
                return new TranslatableText(hc.getTranslationKey()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(hc.getValue())));
            })
            .setSaveConsumer(value -> Config.waterColor = (Config.HighlightColor)value)
            .build());
        
        water.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("watersource.config.water_fill"), Config.waterFill)
            .setDefaultValue(false)
            .setSaveConsumer(value -> Config.waterFill = value)
            .build());

        water.addEntry(entryBuilder.startIntSlider(new TranslatableText("watersource.config.water_alpha"), Config.waterAlpha, 0, 100)
                .setDefaultValue(0)
                .setTooltip(new TranslatableText("watersource.config.alpha.tooltip"))
                .setSaveConsumer(value -> Config.waterAlpha = value)
                .build());

        ConfigCategory lava = builder.getOrCreateCategory(new TranslatableText("watersource.config.category.lava"));

        lava.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("watersource.config.lava_enabled"), Config.lavaEnabled)
            .setDefaultValue(true)
            .setSaveConsumer(value -> Config.lavaEnabled = value)
            .build());

        lava.addEntry(entryBuilder.startIntSlider(new TranslatableText("watersource.config.lava_radius"), Config.lavaRadius, 10, 100)
                .setDefaultValue(30)
                .setSaveConsumer(value -> Config.lavaRadius = value)
                .build());
        
        lava.addEntry(entryBuilder.startEnumSelector(new TranslatableText("watersource.config.lava_color"), Config.HighlightColor.class, Config.lavaColor)
            .setDefaultValue(Config.HighlightColor.CYAN)
            .setEnumNameProvider(color -> {
                Config.HighlightColor hc = (Config.HighlightColor) color;
                return new TranslatableText(hc.getTranslationKey()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(hc.getValue())));
            })
            .setSaveConsumer(value -> Config.lavaColor = (Config.HighlightColor)value)
            .build());
        
        lava.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("watersource.config.lava_fill"), Config.lavaFill)
            .setDefaultValue(false)
            .setSaveConsumer(value -> Config.lavaFill = value)
            .build());

        lava.addEntry(entryBuilder.startIntSlider(new TranslatableText("watersource.config.lava_alpha"), Config.lavaAlpha, 0, 100)
                .setDefaultValue(0)
                .setTooltip(new TranslatableText("watersource.config.alpha.tooltip"))
                .setSaveConsumer(value -> Config.lavaAlpha = value)
                .build());
        
        return builder.build();
    }
}
