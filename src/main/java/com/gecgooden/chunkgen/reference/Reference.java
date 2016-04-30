package com.gecgooden.chunkgen.reference;

import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public class Reference {
    public static Integer x;
    public static Integer z;
    public static Integer height;
    public static Integer width;
    public static double numChunksPerTick;
    public static boolean pauseForPlayers;
    

    public static final String MOD_ID = "chunkgen";
    public static final String VERSION = "1.8.9-1.3.1";
    public static final String GUI_FACTORY = "com.gecgooden.chunkgen.client.gui.GuiFactory";

    public static boolean toGenerate;
    public static int dimID;

    public static DecimalFormat decimalFormat;

    public static Logger logger;
    public static int skipChunks;
    
	public static int startX;
	public static int startZ;
	public static int stopX;
	public static int stopZ;
}
