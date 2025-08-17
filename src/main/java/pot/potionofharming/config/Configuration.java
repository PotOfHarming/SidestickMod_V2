package pot.potionofharming.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pot.potionofharming.SidestickMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/sidestick_mod.json");

    // Helper data holder for saving/loading
    private static class StickSettingsData {
        int sensitivity = StickSettings.sensitivity;
        double jsSensitivity = StickSettings.jsSensitivty;
        long attack_cooldown = StickSettings.attack_cooldown;
        int stick = StickSettings.stick;
        boolean rightHanded = StickSettings.rightHanded;
        boolean debug = StickSettings.debug;
    }

    public static void saveConfig() {
        try {
            SidestickMod.LOGGER.info("Saving config!");
            // Make sure parent folder exists
            CONFIG_FILE.getParentFile().mkdirs();

            // Save static values into a data object
            StickSettingsData data = new StickSettingsData();

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            SidestickMod.LOGGER.error("Failed to save config: " + e);
        }
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                StickSettingsData data = GSON.fromJson(reader, StickSettingsData.class);
                if (data != null) {
                    StickSettings.sensitivity = data.sensitivity;
                    StickSettings.jsSensitivty = data.jsSensitivity;
                    StickSettings.attack_cooldown = data.attack_cooldown;
                    StickSettings.stick = data.stick;
                    StickSettings.rightHanded = data.rightHanded;
                    StickSettings.debug = data.debug;
                }
            } catch (IOException e) {
                SidestickMod.LOGGER.error("Failed to load config: " + e);
            }
        } else {
            saveConfig(); // create defaults
        }
    }
}
