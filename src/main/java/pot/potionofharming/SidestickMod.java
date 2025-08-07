package pot.potionofharming;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pot.potionofharming.config.Configuration;
import pot.potionofharming.config.StickSettings;
import pot.potionofharming.movement.Loop;

public class SidestickMod implements ModInitializer {
	public static final String MOD_ID = "sidestickmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean breakLoop = true;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		Configuration.loadConfig();

		if (!initGLFW()) return;
		if (!loadStick()) return;
		LOGGER.info("Successfully loaded stick!");

		// start loop
		ClientPlayConnectionEvents.JOIN.register((handler, sender, cl) -> {
			cl.execute(() -> {
				MinecraftClient client = MinecraftClient.getInstance();
				if (client.player!=null) {
					breakLoop = false;
					LOGGER.info("Starting loop.");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						LOGGER.error(e.toString());
					}
					Loop.startLoop();
				}
			});
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, cl) -> {
			if (MinecraftClient.getInstance().player!=null) return;
			breakLoop = true;
			LOGGER.info("Stopping loop.");
		});
	}

	private static boolean initGLFW() {
		int loops = 0;
		while (!GLFW.glfwInit() || loops < 5) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			loops++;
		}
		if (!GLFW.glfwInit()) {
			LOGGER.error("Unable to init GLFW!");
			return false;
		} else return GLFW.glfwInit();
	}

	public static boolean loadStick() {
		StickSettings.stick = -1;
		for (int i = GLFW.GLFW_JOYSTICK_1; i < GLFW.GLFW_JOYSTICK_LAST; i++) {
			if (GLFW.glfwJoystickPresent(i)) {
				LOGGER.info("Joystick found!\nJoystick: "+ GLFW.glfwGetJoystickName(i));
				StickSettings.stick = i;
				break;
			}
		}
		return StickSettings.stick!=-1;
	}
}