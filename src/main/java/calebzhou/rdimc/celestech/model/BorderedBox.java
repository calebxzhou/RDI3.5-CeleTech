package calebzhou.rdimc.celestech.model;

import calebzhou.rdimc.celestech.RDICeleTech;

import java.io.Serializable;

public class BorderedBox implements Serializable {
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;

    public BorderedBox(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public static BorderedBox fromString(String boxStr) {
        String[] split = boxStr.split(",");
        int x1=Integer.parseInt(split[0]);
        int y1=Integer.parseInt(split[1]);
        int z1=Integer.parseInt(split[2]);
        int x2=Integer.parseInt(split[3]);
        int y2=Integer.parseInt(split[4]);
        int z2=Integer.parseInt(split[5]);
        return new BorderedBox(x1,y1,z1,x2,y2,z2);
    }
    public String getSizeStringX(){
        return String.format("%dx%dx%d",Math.abs(x1-x2),Math.abs(y1-y2),Math.abs(z1-z2));
    }
    public CoordLocation randomLocation(){
        int ranX = x1<x2?RDICeleTech.RANDOM.nextInt(x1, x2):x1;
        int ranY = y1<y2?RDICeleTech.RANDOM.nextInt(y1, y2):y1;
        int ranZ = z1<z2?RDICeleTech.RANDOM.nextInt(z1, z2):z1;
        return new CoordLocation(ranX,ranY,ranZ);
    }

    public boolean isSizeSmallerThan(int sizeX,int sizeY,int sizeZ){
        return Math.abs(x1-x2)<=sizeX && Math.abs(y1-y2)<=sizeY && Math.abs(z1-z2)<=sizeZ;
    }
    @Override
    public String toString() {
        return String.format("%d,%d,%d,%d,%d,%d",x1,y1,z1,x2,y2,z2);
    }
}
