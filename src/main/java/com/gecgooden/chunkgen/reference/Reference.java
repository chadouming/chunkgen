package com.gecgooden.chunkgen.reference;

import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Queue;

public class Reference {
    public static Integer x = 0;
    public static Integer z = 0;
    public static Integer height = 0;
    public static Integer width = 0;
    public static Integer dimensionID = 0;
    public static Integer numChunksPerTick = 1;
    public static boolean pauseForPlayers = true;
    public static boolean verbose = false;

    public static Integer backX = 0;
    public static Integer backZ = 0;

    public static final String MOD_ID = "chunkgen";
    public static final String VERSION = "1.8.9-1.3.0";
    public static final String GUI_FACTORY = "com.gecgooden.chunkgen.client.gui.GuiFactory";

    public static boolean toGenerate = false;

    public static double chunkLeft = 0;
    public static double chunkDone = 0;
    public static int tickSkip = 0;

    public static int updateDelay = 50;

    public static DecimalFormat decimalFormat;

    public static Logger logger;
    public static int skipChunks = 0;
}
