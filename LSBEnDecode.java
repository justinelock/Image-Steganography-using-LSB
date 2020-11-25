package com.code;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LSBEnDecode {
    static final String MESSAGEFILE = "D:\\test\\message.txt";
    static final String COVERIMAGEFILE = "D:\\test\\photo5084660457607899174.jpg";
    static final String STEGIMAGEFILE = "D:\\test\\steg.png";
    static final int BIT = 16;

    static final String DECODEDMESSAGEFILE = "D:\\test\\message_dec.txt";

    public static String b_msg = "";
    public static int len = 0;

    public static void main(String[] args) {
        String contentOfMessageFile = "班农战斗室第527期P4：如果如多米尼CEO在证词中说的那样，多米尼投票机使用了来自中国元器件，那么他们必须让我们知道到底谁是来自中国的供应商。 Bannon War Room EP527 P4: If as Dominion’s CEO said in the testimony that there are Chinese components used in Dominion mashion, they must let us know who exactly are the providers. ";

        en(contentOfMessageFile);
        de();
    }

    public static void de() {
        long st = System.currentTimeMillis();
        try {
            BufferedImage yImage = readImageFile(STEGIMAGEFILE);

            DecodeTheMessage(yImage);
            String msg = "";
            for (int i = 0; i < len * BIT; i = i + BIT) {
                String sub = b_msg.substring(i, i + BIT);
                int m = Integer.parseInt(sub, 2);
                char ch = (char) m;
                //System.out.println("m " + m + " c " + ch);
                msg += ch;
            }
            System.err.println("msg====" + msg);
            //PrintWriter out = new PrintWriter(new FileWriter(DECODEDMESSAGEFILE, true), true);
            //out.write(msg);
            //out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long et = (System.currentTimeMillis() - st) / 1000;
        System.err.println("......done." + (et > 60 ? et / 60 + "m" : et + "s"));
    }

    public static void en(String contentOfMessageFile) {
        long st = System.currentTimeMillis();
        try {
            //zh2Binary("p啊");
            //String contentOfMessageFile = readMessageFile();
            //contentOfMessageFile = "战";
            int[] bits = bit_Msg(contentOfMessageFile);
            //int[] bits = parse(contentOfMessageFile);
            System.err.println("msg in file:" + contentOfMessageFile);
            for (int i = 0; i < bits.length; i++) {
                System.out.print(bits[i]);
            }
            System.out.println();
            BufferedImage theImage = readImageFile(COVERIMAGEFILE);
            hideTheMessage(bits, theImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long et = (System.currentTimeMillis() - st) / 1000;
        System.err.println("......done." + (et > 60 ? et / 60 + "m" : et + "s"));
    }

    public static String readMessageFile() throws FileNotFoundException {
        String contentOfMessageFile = "";
        File a = new File(MESSAGEFILE);
        Scanner scan = new Scanner(a);
        while (scan.hasNextLine()) {
            String next = scan.nextLine();
            contentOfMessageFile += next;
            if (scan.hasNextLine()) {
                contentOfMessageFile += "\n";
            }
        }
        scan.close();
        return contentOfMessageFile;
    }

    public static int[] bit_Msg(String msg) {
        System.err.println(msg.length());
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < msg.length(); i++) {
            int x = msg.charAt(i);
            String x_s = Integer.toBinaryString(x);
            System.err.println(x_s);
            while (x_s.length() != BIT) {
                x_s = '0' + x_s;
            }
            System.out.println("dec value for " + x + " is " + x_s);
            for (int i1 = 0; i1 < BIT; i1++) {
                list.add(Integer.parseInt(String.valueOf(x_s.charAt(i1))));
            }
        }
        int size = list.size();
        int[] b_msg = new int[size];
        for (int i = 0; i < size; i++) {
            b_msg[i] = list.get(i);
        }
        return b_msg;
    }

    public static int[] zh2Binary(String s) {
        char[] charArray = s.toCharArray();
        System.out.println(charArray.length);
        int[] b_msg = new int[charArray.length * 8];
        for (int i = 0; i < charArray.length; i++) {
            System.out.println((int) charArray[i]);
            String zf = Integer.toBinaryString(charArray[i]);
            System.out.println(zf);
        }
        return b_msg;
    }

   
    public static int[] parse(String str) {
        int length = str.length();
        int[] result = new int[length]; 
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            result[i] = Character.getNumericValue(c);
        }
        return result;
    }

    public static BufferedImage readImageFile(String COVERIMAGEFILE) {
        BufferedImage theImage = null;
        File p = new File(COVERIMAGEFILE);
        try {
            theImage = ImageIO.read(p);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return theImage;
    }


    public static void hideTheMessage(int[] bits, BufferedImage theImage) throws Exception {
        File f = new File(STEGIMAGEFILE);
        BufferedImage sten_img = null;
        int bit_l = bits.length / BIT;
        int[] bl_msg = new int[BIT];
        System.err.println("bit lent=" + bit_l);
        String bl_s = Integer.toBinaryString(bit_l);
        while (bl_s.length() != BIT) {
            bl_s = '0' + bl_s;
        }
        for (int i1 = 0; i1 < BIT; i1++) {
            bl_msg[i1] = Integer.parseInt(String.valueOf(bl_s.charAt(i1)));
        }
        int j = 0;
        int b = 0;
        int currentBitEntry = BIT;

        for (int x = 0; x < theImage.getWidth(); x++) {
            for (int y = 0; y < theImage.getHeight(); y++) {
                if (x == 0 && y < BIT) {
                    int currentPixel = theImage.getRGB(x, y);
                    //int ori = currentPixel;
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    String sten_s = x_s.substring(0, x_s.length() - 1);
                    sten_s = sten_s + bl_msg[b];

                    int s_pixel = Integer.parseInt(sten_s, 2);
                    int a = 255;
                    int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
                    theImage.setRGB(x, y, rgb);
                    //System.out.println("original " + ori + " after=" + theImage.getRGB(x, y));
                    ImageIO.write(theImage, "png", f);
                    b++;
                } else if (currentBitEntry < bits.length + BIT) {
                    int currentPixel = theImage.getRGB(x, y);
                    //int ori = currentPixel;
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    String sten_s = x_s.substring(0, x_s.length() - 1);
                    sten_s = sten_s + bits[j];
                    j++;
                    int s_pixel = Integer.parseInt(sten_s, 2);

                    int a = 255;
                    int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
                    theImage.setRGB(x, y, rgb);
                    //System.err.println("original " + ori + " after=" + theImage.getRGB(x, y));
                    ImageIO.write(theImage, "png", f);
                    currentBitEntry++;
                    //System.err.println("curre=" + currentBitEntry);
                }
            }
        }
    }


    public static void DecodeTheMessage(BufferedImage yImage) throws Exception {
        int j = 0;
        int currentBitEntry = 0;
        String bx_msg = "";
        for (int x = 0; x < yImage.getWidth(); x++) {
            for (int y = 0; y < yImage.getHeight(); y++) {
                if (x == 0 && y < BIT) {
                    //System.out.println("enc "+yImage.getRGB(x, y)+" dec "+yImage.getRGB(x, y)+" "+b_msg);
                    int currentPixel = yImage.getRGB(x, y);
                    //int red = currentPixel >> 16;
                    //red = red & 255;
                    //int green = currentPixel >> 8;
                    //green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    bx_msg += x_s.charAt(x_s.length() - 1);
                    len = Integer.parseInt(bx_msg, 2);
                } else if (currentBitEntry < len * BIT) {
                    //System.out.println("enc "+yImage.getRGB(x, y)+" dec "+yImage.getRGB(x, y)+" "+b_msg);
                    int currentPixel = yImage.getRGB(x, y);
                    //int red = currentPixel >> 16;
                    //red = red & 255;
                    //int green = currentPixel >> 8;
                    //green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    b_msg += x_s.charAt(x_s.length() - 1);

                    currentBitEntry++;
                    //System.out.println("curre "+currentBitEntry);
                }
            }
        }
        //System.out.println("bin value of msg hided in img is " + b_msg);
    }
}