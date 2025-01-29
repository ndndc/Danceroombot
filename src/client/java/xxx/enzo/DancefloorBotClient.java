package xxx.enzo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DancefloorBotClient implements ClientModInitializer {
	public static boolean GlassFound = false;
	public static BlockPos BlockPosition = new BlockPos(0,0,0);

	private static void onStartTick(MinecraftServer server) {

		BlockPos closest_glass = new BlockPos(0, 0, 0);
		boolean found_glass = false;

		ClientPlayerEntity pe = MinecraftClient.getInstance().player;

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

		GlassFound = found_glass;
		BlockPosition = closest_glass;
	}

	@Override
	public void onInitializeClient() {
		ServerTickEvents.START_SERVER_TICK.register(DancefloorBotClient::onStartTick);
	}
}