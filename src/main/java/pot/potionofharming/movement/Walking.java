package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import pot.potionofharming.SidestickMod;
import pot.potionofharming.config.StickSettings;

public class Walking {
    private static boolean fbStarted = false;
    private static boolean lrStarted = false;
    private static boolean jsStarted = false;
    public static void move(MinecraftClient client, float fb, float lr, float js) {
        if (client.player==null) return;
        int jsInt;
        if (js>StickSettings.jsSensitivty) jsInt=1;
        else if (js<(-StickSettings.jsSensitivty)) jsInt=-1;
        else jsInt=0;

        if (js!=0||jsStarted) {
            jsStarted = jsInt!=0;
            try {
                client.execute(() -> KeyBinding.setKeyPressed(client.options.jumpKey.getDefaultKey(), jsInt==1));
                client.execute(() -> KeyBinding.setKeyPressed(client.options.sneakKey.getDefaultKey(), jsInt==-1));
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
        if (fb!=0||fbStarted) {
            fbStarted = fb!=0;
            try {
                client.execute(() -> KeyBinding.setKeyPressed(client.options.forwardKey.getDefaultKey(), fb == 1));
                client.execute(() -> KeyBinding.setKeyPressed(client.options.backKey.getDefaultKey(), fb == -1));
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
        if (lr!=0||lrStarted) {
            lrStarted = lr!=0;
            try {
                client.execute(() -> KeyBinding.setKeyPressed(client.options.leftKey.getDefaultKey(), lr==-1));
                client.execute(() -> KeyBinding.setKeyPressed(client.options.rightKey.getDefaultKey(), lr==1));
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
    }
}
