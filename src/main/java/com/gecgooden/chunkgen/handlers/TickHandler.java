package com.gecgooden.chunkgen.handlers;

import com.gecgooden.chunkgen.reference.Reference;
import com.gecgooden.chunkgen.handlers.TickStats;
import com.gecgooden.chunkgen.util.Utilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

	@SubscribeEvent
	public final void onServerTick(TickEvent.ServerTickEvent event) {
		final World world = MinecraftServer.getServer().getEntityWorld();
		if (Reference.pauseForPlayers && world.playerEntities.size() > 0) return;

		if(Reference.toGenerate == true) {
			double tick = TickStats.getTickAvg();
			double tps = TickStats.getTPS();

			if(TickStats.getLastTickWasLong()) {
				Reference.logger.info("A single tick took more than a second. Dropping chunks and skipping a few ticks of generation.");
				Reference.numChunksPerTick = 1;
				Utilities.unloadChunks(Reference.dimensionID);
				Reference.tickSkip = 50;
				return;
			} else if (Reference.tickSkip-- > 1) {
			} else if(tps <= 18 && Reference.numChunksPerTick > 1) {
	                        Reference.logger.info("Tick average time is too slow (" + tick +"ms). Decreasing speed.");
       	                	Reference.numChunksPerTick--;
       		        } else if(tps >= 20 && Reference.numChunksPerTick < 50 && (Reference.chunkDone % Reference.updateDelay == 0)) {
                        	Reference.logger.info("Tick average time is fast enough (" + tick + "ms), increasing speed.");
                        	Reference.numChunksPerTick++;
                	}

			Utilities.generateChunks(Reference.x, Reference.z, Reference.dimensionID, Reference.width, Reference.height, Reference.numChunksPerTick);
			if(Reference.chunkLeft % Reference.updateDelay == 0) {
				double completedPercentage = 1.00 - (double)Reference.chunkLeft/(double)(Reference.width * Reference.height);
				Reference.logger.info("Current chunk/tick: " + Reference.numChunksPerTick +
					"  TPS : " + tps);
				ChatComponentTranslation chatTranslation = new ChatComponentTranslation("");
				Reference.logger.info("Chunkgen: " + (float)(completedPercentage * 100) + "% completed");
				Reference.logger.info("Chunk generated: " + (long)Reference.chunkDone);
			}

			if(Reference.chunkLeft % (15 * Reference.updateDelay) == 0)
				Utilities.unloadChunks(Reference.dimensionID);
		}
	}
}
