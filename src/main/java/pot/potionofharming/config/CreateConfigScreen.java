package pot.potionofharming.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import pot.potionofharming.SidestickMod;

public class CreateConfigScreen {
    public static Screen openConfigScreen(Screen parent) {
        SidestickMod.LOGGER.debug("IN BETA");
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.sidestickmod.config"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("sensitivity.sidestickmod.config"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startIntSlider(Text.translatable("sensitivity.sidestickmod.config"), StickSettings.sensitivity, 10, 1000)
                .setDefaultValue(StickSettings.sensitivity) // Recommended: Used when user click "Reset"
                .setTooltip(Text.translatable("sensitivity_tooltip.sidestickmod.config")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(newVal -> {
                    StickSettings.sensitivity = newVal;
                    Configuration.saveConfig();
                })
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startLongSlider(Text.translatable("attack_cooldown.sidestickmod.config"), StickSettings.attack_cooldown, 10, 1000)
                .setDefaultValue(StickSettings.attack_cooldown) // Recommended: Used when user click "Reset"
                .setTooltip(Text.translatable("attack_cooldown_tooltip.sidestickmod.config")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(newVal -> {
                    StickSettings.attack_cooldown = newVal;
                    Configuration.saveConfig();
                })
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("debug_mode.sidestickmod.config"), StickSettings.debug)
                .setDefaultValue(StickSettings.debug)
                .setTooltip(Text.translatable("debug_mode_tooltip.sidestickmod.config"))
                .setSaveConsumer(newVal -> {
                    StickSettings.debug = newVal;
                    Configuration.saveConfig();
                })
                .build());

        return builder.build();
    }
}
