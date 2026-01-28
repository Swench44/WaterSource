package dev.swench.watersource;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class WaterSource implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Config.load();

        Config.configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.watersource.config",
                InputUtil.Type.KEYSYM,
                -1,
                "category.watersource.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Config.configKeyBinding.wasPressed()) {
                if (client.currentScreen == null) {
                    client.openScreen(ClothConfigIntegration.createConfigScreen(null));
                }
            }
        });
    }
}

