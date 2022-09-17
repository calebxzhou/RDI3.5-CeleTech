package calebzhou.rdi.celestech.utils;

public class IpUtils {
    public static void main(String[] args) {
        int address = 0b10001000_11010101_10101010_01010101;
        byte[] addr = new byte[4];
        addr[0] = (byte) ((address >>> 24) & 0xFF);
        addr[1] = (byte) ((address >>> 16) & 0xFF);
        addr[2] = (byte) ((address >>> 8) & 0xFF);
        addr[3] = (byte) (address & 0xFF);
        System.out.println(ip2addr(addr));
    }
    //把ip地址转换成36进制的
    public static String ip2addr(byte[] ip){
        int newip[] = new int[4];
        newip[0]=  (ip[0]&0xff);
        newip[1]=  (ip[1]&0xff);
        newip[2]=  (ip[2]&0xff);
        newip[3]=  (ip[3]&0xff);
        int radix=36;
        return new String(Integer.toString(newip[0],radix)+Integer.toString(newip[1],radix)
                +Integer.toString(newip[2],radix)+Integer.toString(newip[3],radix));
    }
}
