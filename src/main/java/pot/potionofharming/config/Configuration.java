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
    private static StickSettings config = new StickSettings();

    public static StickSettings getConfig() {
        return config;
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, StickSettings.class);
            } catch (IOException e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        } else {
            saveConfig(); // Create default config
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            SidestickMod.LOGGER.error(e.toString());
        }
    }
}
