package com.gecgooden.chunkgen.handlers;

import com.gecgooden.chunkgen.util.Utilities;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.System;

public class TickStats {
        private static long lastTick = System.currentTimeMillis();
	private static boolean lastTickWasLong = false;
        private static int[] tickAvg = new int[20];
	private static double _tickAvg = 0;

	@SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
		int currentTick = (int)(System.currentTimeMillis() - lastTick);

		lastTickWasLong = (currentTick >= 1000) ? true : false;

		updateTickAvg(currentTick);

		_tickAvg = tickAvg();

		lastTick = System.currentTimeMillis();
	}

	private static final double tickAvg() {
                double sum = 0;

                for(int avg : tickAvg)
                        sum += avg;

                return sum / (double)tickAvg.length;
        }

        private final void updateTickAvg(int lastTick) {
                for(int i = 1; i < tickAvg.length; i++)
                        tickAvg[i - 1] = tickAvg[i];

                tickAvg[tickAvg.length - 1] = lastTick;
        }

	public static double getTPS() {
                return (1 / (tickAvg() / 1000) > 20) ? 20 : (1 / (tickAvg() / 1000));
        }

	public static double getTickAvg() {
		return _tickAvg;
	}

	public static boolean getLastTickWasLong() {
		return lastTickWasLong;
	}

}
