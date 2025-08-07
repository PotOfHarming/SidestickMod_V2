package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import pot.potionofharming.SidestickMod;
import pot.potionofharming.config.StickSettings;

import java.util.Objects;


public class MouseClicks {
    private static boolean lcStarted = false;
    private static boolean rcStarted = false;
    private static long lastHit = 0;
    public static void press(MinecraftClient client, boolean lc, boolean rc) {
        if (client.player==null || client.interactionManager==null) return;
        if (lc||lcStarted) {
            lcStarted = lc;
            client.execute(() ->KeyBinding.setKeyPressed(client.options.attackKey.getDefaultKey(), lc));
            try {
                switch (Objects.requireNonNull(client.crosshairTarget).getType()) {
                    case ENTITY -> {
                        if (System.currentTimeMillis() - lastHit <= StickSettings.attack_cooldown) break;
                        client.interactionManager.attackEntity(client.player, ((EntityHitResult) client.crosshairTarget).getEntity());
                        lastHit = System.currentTimeMillis();
                    }
                    case BLOCK -> {
                        BlockHitResult bhr = (BlockHitResult) client.crosshairTarget;
                        client.interactionManager.attackBlock(bhr.getBlockPos(), bhr.getSide());
                    }
                    case MISS -> client.player.swingHand(Hand.MAIN_HAND);
                }
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
        if (rc||rcStarted) {
            rcStarted = rc;
            try {
                client.execute(() -> KeyBinding.setKeyPressed(client.options.useKey.getDefaultKey(), rc));
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
        }
    }
}
