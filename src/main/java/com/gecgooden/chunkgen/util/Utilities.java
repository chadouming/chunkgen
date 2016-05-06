package com.gecgooden.chunkgen.util;

import com.gecgooden.chunkgen.reference.Reference;
import com.gecgooden.chunkgen.ChunkGen;

import net.minecraft.server.MinecraftServer;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import net.minecraftforge.common.chunkio.ChunkIOExecutor;
import net.minecraftforge.common.MinecraftForge;

public class Utilities implements Runnable {

	static boolean doneLoading = true;
	//static int _x = 0;
	//static int _z = 0;

	@Override
	public void run() {
		ChunkProviderServer cps = MinecraftServer.getServer().worldServerForDimension(Reference.dimensionID).theChunkProviderServer;

		// Increase the ammount of done chunk
		Reference.chunkDone++;

		// Request the next chunk
		doneLoading = true;

		if(Reference.verbose)
			Reference.logger.info("Chunk done : " + Reference.chunkDone);
	}


	private static boolean chunksExist(int x, int z, int dimensionID) {
		WorldServer world = null;

		world = DimensionManager.getWorld(dimensionID);

		return RegionFileCache.createOrLoadRegionFile(world.getChunkSaveLocation(), x, z).chunkExists(x & 0x1F, z & 0x1F);
	}

	public static void generateChunks(int x, int z, int dimensionID, int width, int height, int chunkToGenerate) {
		if(doneLoading) {
			for(int i = (x - width/2) + Reference.backX; i <= (x + width/2); i++) {
				for(int j = (z - height/2) + Reference.backZ; j <= (z + height/2); j++) {
					generateChunk(i, j, dimensionID);
//					_z = i; _x = j;
					Reference.chunkLeft--;
                                	Reference.backZ++;

					if((Reference.chunkLeft % chunkToGenerate) == 0)
                                        	return;
				}
				Reference.backZ = 0;
				Reference.backX++;
			}
			Reference.toGenerate = false;
		}
	}

	private static void generateChunk(int x, int z, int dimensionID) {

		ChunkProviderServer cps = MinecraftServer.getServer().worldServerForDimension(dimensionID).theChunkProviderServer;

		if(!chunksExist(x, z, dimensionID)) {

			// Queue the desired chunk to be loaded
			ChunkIOExecutor.queueChunkLoad(DimensionManager.getWorld(dimensionID), (AnvilChunkLoader)cps.chunkLoader, cps, x, z, new Utilities());
			doneLoading = false;

			if(Reference.verbose)
				Reference.logger.info("Chunk created at x=" + x + " and z=" + z);
		}
	}

	public static void unloadChunks(int dimensionID) {
                ChunkProviderServer cps = MinecraftServer.getServer().worldServerForDimension(dimensionID).theChunkProviderServer;

		// Mark all chunks for unloading
		cps.unloadAllChunks();
                // Unload all chunks that are marked to be unloaded
                cps.unloadQueuedChunks();

		cps.saveChunks(true, null);

                Reference.logger.info("Unloading chunks. Chunk loaded : " + cps.getLoadedChunkCount());
	}
}
