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
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.MovementType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class DancefloorBotClient implements ClientModInitializer {
	public static boolean GlassFound = false;
	public static BlockPos BlockPosition = new BlockPos(0, 0, 0);
	public static boolean botEnabled = false; // Variable to track bot status
	public static boolean walkForward = false;
	public static boolean walkBackwards = false;
	public static boolean walkLeft = false;
	public static boolean walkRight = false;
	public static boolean isOnBlock = false;
	public static boolean enterBlock = false;

	public static boolean jump = false;
	public static boolean sneak = false;
	public static boolean punch = false;
	public static long onBlockTime = 0;

	public static boolean ShouldSneak()
	{
		return isOnBlock && sneak;
	}

	public static boolean ShouldJump()
	{
		if(isOnBlock && jump)
		{
			jump = false;
			return true;
		}
		return false;
	}

	public static boolean ShouldPunch()
	{
		if(punch)
		{
			if((System.currentTimeMillis() - onBlockTime) > 330) {
				return false;
			}
		}
		if(isOnBlock && punch)
		{
			if((System.currentTimeMillis() - onBlockTime) < 250)
			{
				return false;
			}

			return true;
		}
		return false;
	}

	public static void EnteredBlock()
	{
		onBlockTime = System.currentTimeMillis();
		System.out.println("Entered Block");
	}

	public static void LeftBlock()
	{

	}

	private static void onStartTick(MinecraftServer server) {
		walkForward = false;
		walkBackwards = false;
		walkLeft = false;
		walkRight = false;
		if (!botEnabled) return; // Only execute if bot is enabled

		if(isOnBlock)
		{
			if(!enterBlock)
			{
				EnteredBlock();
				enterBlock = true;
			}
		}else{
			enterBlock = false;
		}

		BlockPos closest_glass = new BlockPos(0, 0, 0);
		boolean found_glass = false;

		ClientPlayerEntity pe = MinecraftClient.getInstance().player;

		if(pe != null) {
			if(DancefloorBotClient.botEnabled)
			{
			}
			if(botEnabled)pe.setYaw(0);
			found_glass = false;
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
		walkForward = false;
		if(GlassFound) {
			if(pe != null) {
				//Set a
				Vec3d a = pe.getPos();
				//Set b
				BlockPos b = BlockPosition;
				double uwu = Math.atan2(a.getZ() - (b.getZ() + .5f), a.getX() - (b.getX() + .5f));
				uwu *= 180;
				uwu /= Math.PI;

				//pe.setYaw((float)uwu - 270);
				Vec3d diff = new Vec3d(b.getX()+.5f,b.getY(),b.getZ()+.5f).subtract(a);
				diff = new Vec3d(Math.min(1,diff.getX()),Math.min(1,diff.getY()),Math.min(1,diff.getZ()));
				//pe.setYaw((float)uwu-270.0f);

				isOnBlock = !((Math.abs(diff.getX()) > .2f || Math.abs(diff.getZ()) > .2f) && GlassFound);

				if((Math.abs(diff.getX()) > .2f || Math.abs(diff.getZ()) > .2f) && GlassFound)
				{
					if(diff.getZ() > .2f)
					{
						walkForward = true;
					}
					if(diff.getZ() < -.2f)
					{
						walkBackwards = true;
					}
					if(diff.getX() > .2f)
					{
						walkLeft = true;
					}
					if(diff.getX() < -.2f)
					{
						walkRight = true;
					}
				}
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
				botEnabled = !botEnabled; // Toggle bot status
				client.player.sendMessage(Text.of("DancefloorBot " + (botEnabled ? "Enabled" : "Disabled")), true);
			}
		});

	}

	public static void TitelEmpfangen(Text txt)
	{
		sneak = false;
		jump = false;
		punch = false;
		MinecraftClient.getInstance().player.sendMessage(txt,true);
		String t = txt.getLiteralString();
		if (t.toLowerCase().contains("sneak"))
		{
			sneak = true;
		}
		if (t.toLowerCase().contains("jump"))
		{
			jump = true;
		}
		if (t.toLowerCase().contains("punch"))
		{
			punch = true;
		}
	}
}