package xxx.enzo;

import com.mojang.brigadier.Message;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class DancefloorBotClient implements ClientModInitializer {
	public static boolean GlassFound = false;
	public static BlockPos BlockPosition = new BlockPos(0,0,0);

	private static void onStartTick(MinecraftServer server) {

		BlockPos closest_glass = new BlockPos(0, 0, 0);
		boolean found_glass = false;

		ClientPlayerEntity pe = MinecraftClient.getInstance().player;

		if(pe != null) {
			for (int y = -2; y < 2; y++)
				for (int x = -7; x < 7; x++)
					for (int z = -7; z < 7; z++) {
						BlockState block = MinecraftClient.getInstance().world.getBlockState(pe.getBlockPos().add(new BlockPos(x, y, z)));
						if (block.isOf(Blocks.BLUE_STAINED_GLASS) || block.isOf(Blocks.RED_STAINED_GLASS) || block.isOf(Blocks.PINK_STAINED_GLASS) || block.isOf(Blocks.GREEN_STAINED_GLASS)) {
							BlockPos pse = pe.getBlockPos().add(new BlockPos(x, y, z));

							double dist_sqr = pe.getBlockPos().getSquaredDistance(pe.getBlockPos().add(new BlockPos(x, y, z)));
							double clst_sqr = pe.getBlockPos().getSquaredDistance(closest_glass);
							if (dist_sqr < clst_sqr || !found_glass) {
								closest_glass = pse;
							}
							found_glass = true;

						}
					}
		}
		GlassFound = found_glass;
		BlockPosition = closest_glass;

		if(GlassFound) {
			if(pe != null) {
				//Set a
				Vec3d a = pe.getPos();
				//Set b
				BlockPos b = BlockPosition;
				double uwu = Math.atan2(a.getZ() - (b.getZ() - .5f), a.getX() - (b.getX() - .5f));
				uwu *= 180;
				uwu /= Math.PI;
				MinecraftClient.getInstance().player.sendMessage(Text.of("Glass found At " + BlockPosition.toString()+" YAW: "+uwu),true);
				pe.setYaw((float)uwu - 270);

			}
		}
	}



	@Override
	public void onInitializeClient() {
		ServerTickEvents.START_SERVER_TICK.register(DancefloorBotClient::onStartTick);
		WorldRenderEvents.AFTER_ENTITIES.register(after_entities -> {
		});
		KeyBinding myKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.dancefloorbot.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				"category.dancefloorbot.general"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (myKeyBinding.wasPressed()) {
				client.player.sendMessage(Text.of("DancefloorBot Toggled"),true);
			}
		});
	}
}