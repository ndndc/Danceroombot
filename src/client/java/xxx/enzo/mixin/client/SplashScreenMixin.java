package xxx.enzo.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xxx.enzo.DancefloorBotClient;

@Mixin(net.minecraft.client.gui.screen.TitleScreen.class)
public class SplashScreenMixin {
    @Shadow
    private static Text COPYRIGHT;
    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
    }
}
