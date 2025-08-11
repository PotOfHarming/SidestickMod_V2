package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import pot.potionofharming.SidestickMod;

public class Walking {
    private static boolean fbStarted = false;
    private static boolean lrStarted = false;
    private static boolean jsStarted = false;
    public static void move(MinecraftClient client, float fb, float lr, float js) {
        if (client.player==null) return;
        if (js<-2.5 || js>2.5 || jsStarted) {
            jsStarted = js>2.5||js<-2.5;
            try {
                client.execute(() -> KeyBinding.setKeyPressed(client.options.jumpKey.getDefaultKey(), js>0.5));
                client.execute(() -> KeyBinding.setKeyPressed(client.options.sneakKey.getDefaultKey(), js<-0.5));
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
