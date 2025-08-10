package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pot.potionofharming.SidestickMod;
import pot.potionofharming.config.StickSettings;
import pot.potionofharming.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Loop {
    private static void initLoop() {

        // 6 is item up, 7 is item down

        while (!SidestickMod.breakLoop) {
            MinecraftClient client = MinecraftClient.getInstance();
            FloatBuffer aFB = GLFW.glfwGetJoystickAxes(StickSettings.stick);
            ByteBuffer aBB = GLFW.glfwGetJoystickButtons(StickSettings.stick);
            if (client.player == null || aFB == null) {
                //                if (StickSettings.debug) SidestickMod.LOGGER.info("NO CLIENT PLAYER FOUND");
                try {
                    Thread.sleep(250);
                } catch (Exception e) {
                    SidestickMod.LOGGER.error(e.toString());
                }
                continue;
            }

            float[] floatAxes = new float[aFB.limit()]; /* LEFTRIGHT, UPDOWN; RUDDER, THRUSTLEVER */
            for (int a = 0; a < aFB.limit(); a++) {
                if (aFB.get(a) < .15f && aFB.get(a) > -.15f) floatAxes[a] = 0f;
                else floatAxes[a] = aFB.get(a) * 10;
            }
            byte[] byteAxes = new byte[aBB.limit()];
            for (int b = 0; b < aBB.limit(); b++) byteAxes[b] = aBB.get(b);

            // debugging for checking which button does what
            if (StickSettings.debug) {
                StringBuilder sb = new StringBuilder("FLOATS: ");
                for (float x : floatAxes) sb.append(x).append(", ");
                sb.append("BYTES: ");
                for (byte b : byteAxes) sb.append(b).append(", ");
                client.player.sendMessage(Text.literal(sb.toString()), true);
            }


            if (floatAxes[0] != 0 || floatAxes[1] != 0) {
                if (StickSettings.debug)
                    client.player.sendMessage(Text.literal("Updating pitch..."), false);
                PitchYaw.updatePY(client, -floatAxes[1], floatAxes[0]);
                if (StickSettings.debug) client.player.sendMessage(Text.literal("Updated pitch!"), false);
            }

            float fb = (float) (Byte.toUnsignedInt(byteAxes[17]) - Byte.toUnsignedInt(byteAxes[19]));
            float lr = (float) (Byte.toUnsignedInt(byteAxes[18]) - Byte.toUnsignedInt(byteAxes[20]));
            float js = floatAxes[2];
            if (StickSettings.debug) client.player.sendMessage(Text.literal("Updating movement..."), false);
            Walking.move(client, fb, lr, js);
            if (StickSettings.debug) client.player.sendMessage(Text.literal("Updated movement!"), false);
            if (StickSettings.debug) client.player.sendMessage(Text.literal("Updating clicks..."), false);
            MouseClicks.press(client, byteAxes[0] == 1, byteAxes[1] == 1);
            if (StickSettings.debug) client.player.sendMessage(Text.literal("Updated clicks..."), false);
            try {
                float frameTime = Utils.getFrameTime();
                if (client.player != null)
                    if (StickSettings.debug) client.player.sendMessage(Text.literal("Waiting for " + frameTime + "ms."), false);
                if (frameTime >= 5000) frameTime = 100;
                Thread.sleep((long) frameTime);
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
    }

    public static void startLoop() {
        Thread thread = new Thread(Loop::initLoop);
        thread.setDaemon(true);
        thread.start();
    }
}
