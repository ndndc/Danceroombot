package xxx.enzo.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xxx.enzo.DancefloorBot;
import xxx.enzo.DancefloorBotClient;

@Mixin(KeyBinding.class)
public class KeyboardInputMixin {
    @Inject(at = @At("HEAD"), method = "isPressed", cancellable = true)
    private void init(CallbackInfoReturnable<Boolean> info) {
        if(DancefloorBotClient.botEnabled) {
            KeyBinding key = (KeyBinding) (Object) this;
            if (key == MinecraftClient.getInstance().options.forwardKey) {
                if (DancefloorBotClient.walkForward && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if (key == MinecraftClient.getInstance().options.backKey) {
                if (DancefloorBotClient.walkBackwards && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if (key == MinecraftClient.getInstance().options.leftKey) {
                if (DancefloorBotClient.walkLeft && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if (key == MinecraftClient.getInstance().options.rightKey) {
                if (DancefloorBotClient.walkRight && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if(key == MinecraftClient.getInstance().options.sneakKey) {
                if(DancefloorBotClient.ShouldSneak() && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if (key == MinecraftClient.getInstance().options.jumpKey)
            {
                if(DancefloorBotClient.ShouldJump() && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }

            if(key == MinecraftClient.getInstance().options.attackKey)
            {
                if(DancefloorBotClient.ShouldPunch() && DancefloorBotClient.botEnabled) {
                    info.setReturnValue(true);
                    info.cancel();
                }
            }
        }
    }
}
