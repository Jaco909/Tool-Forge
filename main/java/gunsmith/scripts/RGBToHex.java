package gunsmith.scripts;

import java.awt.*;

public class RGBToHex {
    public static String RGBToHex(Color color){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();
        return RGBToHex(r,g,b,a);
    }
    public static String RGBToHex(int r,int g, int b){
        return RGBToHex(r,g,b,255);
    }
    public static String RGBToHex(int r,int g, int b,int a){
        int argb = new Color(r,g,b,a).getRGB();
        String hexString = Integer.toHexString(argb);
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
