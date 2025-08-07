package pot.potionofharming.utils;

import net.minecraft.client.MinecraftClient;

public class Utils {

    public static int getFps() {
        return MinecraftClient.getInstance().getCurrentFps();
    }

    public static float getFrameTime() {
        return 1000/(float)getFps();
    }
}
