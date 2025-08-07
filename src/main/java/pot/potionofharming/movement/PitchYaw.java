package pot.potionofharming.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import pot.potionofharming.config.StickSettings;
import pot.potionofharming.utils.Utils;

public class PitchYaw {
    public static void updatePY(MinecraftClient client, float p, float y) {
        if (client.player==null) return;
        ClientPlayerEntity player = client.player;
        if (Float.isNaN(player.getPitch()) || Float.isNaN(player.getYaw())) return;

        float dt = Utils.getFrameTime();
        float sensitivity = (float)StickSettings.sensitivity/100;
        float currentPitch = player.getPitch();
        float currentYaw = player.getYaw();
        float additionP = sensitivity*dt*p/100;
        float additionY = sensitivity*dt*y/100;
        float newPitch = currentPitch+additionP;
        float newYaw = currentYaw+additionY;

        if (newPitch>90) newPitch=90f;
        if (newPitch<-90) newPitch=-90f;
        if (Float.isNaN(newPitch) || Float.isNaN(newYaw)) return;

        player.setPitch(newPitch);
        player.setYaw(newYaw);
        if (client.getNetworkHandler()!=null&&additionP!=0&&additionY!=0) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(player.getYaw(), player.getPitch(), player.isOnGround(), player.horizontalCollision));
        }
    }
}
