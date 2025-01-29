package xxx.enzo.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xxx.enzo.DancefloorBotClient;

@Mixin(KeyBinding.class)
public class KeyboardInputMixin {
    @Inject(at = @At("HEAD"), method = "isPressed", cancellable = true)
    private void init(CallbackInfoReturnable<Boolean> info) {
        KeyBinding key = (KeyBinding) (Object) this;
        if(key == MinecraftClient.getInstance().options.forwardKey)
        {
            if(DancefloorBotClient.walkForward){
                info.setReturnValue(true);
                info.cancel();
            }
        }
    }
}
