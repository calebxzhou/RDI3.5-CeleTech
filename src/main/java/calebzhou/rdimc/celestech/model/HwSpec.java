package calebzhou.rdimc.celestech.model;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class HwSpec implements Serializable {
    public String brand;
    public String os;
    public String board;
    public String mem;
    public String disk;
    public String cpu;
    public String gpu;
    public String mods;

}
