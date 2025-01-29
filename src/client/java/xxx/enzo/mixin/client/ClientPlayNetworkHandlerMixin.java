package xxx.enzo.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xxx.enzo.DancefloorBotClient;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onTitle")
    public void onTitle(CallbackInfo info, TitleS2CPacket packet) {
        DancefloorBotClient.TitelEmpfangen(packet.text());
    }
}
