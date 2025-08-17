package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
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
            try {
                if (System.currentTimeMillis() - lastHit >= StickSettings.attack_cooldown) {
                    lastHit = System.currentTimeMillis();
                    switch (Objects.requireNonNull(client.crosshairTarget).getType()) {
                        case ENTITY -> {
                            client.interactionManager.attackEntity(client.player, ((EntityHitResult) client.crosshairTarget).getEntity());
                            client.player.swingHand(Hand.MAIN_HAND);
                            client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        }
                        case BLOCK -> {
                            BlockHitResult bhr = (BlockHitResult) client.crosshairTarget;
                            client.interactionManager.attackBlock(bhr.getBlockPos(), bhr.getSide());
                            client.player.swingHand(Hand.MAIN_HAND);
                            client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        }
                        case MISS -> client.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
            lcStarted = lc;
        }
        if (rc||rcStarted) {
            try {
                if (System.currentTimeMillis() - lastHit >= StickSettings.attack_cooldown || (rcStarted&&!rc)) {
                    if (rc) lastHit = System.currentTimeMillis();
                    client.execute(() -> KeyBinding.setKeyPressed(client.options.useKey.getDefaultKey(), rc));
                    client.player.swingHand(Hand.MAIN_HAND);
                    client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                }
            } catch (Exception e) {
                SidestickMod.LOGGER.error(e.toString());
            }
            rcStarted = rc;
        }
    }
}
