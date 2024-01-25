package me.weishu.kernelsu.utils;

import java.util.ArrayList;
import java.util.Random;

public class NumberUtils {
    public static String getIMEI() {
        String str = "";
        for (byte b = 0; b < 15; b++)
            str = str + String.valueOf((int)(Math.random() * 10.0D));
        return str;
    }

    public static String getRandom4() {
        char[] arrayOfChar = "0123456789abcdefgh".toCharArray();
        String str = "";
        for (byte b = 0; b < 4; b++) {
            char c = arrayOfChar[(int)(Math.random() * 18.0D)];
            if (str.contains(String.valueOf(c))) {
                b--;
            } else {
                str = str + c;
            }
        }
        return str;
    }

    public static String getRandomCode() {
        char[] arrayOfChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String str = "";
        for (byte b = 0; b < 6; b++) {
            char c = arrayOfChar[(int)(Math.random() * 36.0D)];
            if (str.contains(String.valueOf(c))) {
                b--;
            } else {
                str = str + c;
            }
        }
        return str;
    }

    public static String getRandomMac() {
        char[] arrayOfChar = "0123456789abcd".toCharArray();
        String str = "";
        for (byte b = 0; b < 2; b++) {
            char c = arrayOfChar[(int)(Math.random() * 14.0D)];
            if (str.contains(String.valueOf(c))) {
                b--;
            } else {
                str = str + c;
            }
        }
        return str;
    }

    public static String getRandom(int length){
        char[] arrayOfChar = "0123456789abcd".toCharArray();
        String str = "";
        for (byte b = 0; b < length; b++) {
            char c = arrayOfChar[(int)(Math.random() * 14.0D)];
            if (str.contains(String.valueOf(c))) {
                b--;
            } else {
                str = str + c;
            }
        }
        return str;
    }

    public static String randNum() {
        String str = "";
        for (byte b = 0; b < 20; b++)
            str = str + String.valueOf((int)(Math.random() * 10.0D));
        return str;
    }

    /***
     * 生成指定范围的1位随机数
     * @param num
     * @return
     */
    public static int randNum(int num){
        Random random = new Random();
        int randomNumber = random.nextInt(num);
        return randomNumber;
    }

    /***
     * 生成指定1位数
     * @param lenght
     * @return
     */
    public static String randNumLenght(int lenght){
        String str = "";
        for (byte b = 0; b < lenght; b++) {
            str = str + (int) (Math.random() * 10);
        }
        return str;
    }

    public static String randomPhonoNum(){
        int[] mobileStart = {139,138,137,136,135,134,159,158,157,150,151,152,188,130,131,132,156,155,133,153,189,180,177,176};
        Random r = new Random();
        ArrayList<Integer> mobileList = new ArrayList<>();
        for(int i = 0;i<mobileStart.length;i++){
            mobileList.add(mobileStart[i]);
        }

        Random r1 = new Random();
        String temp = "";
        for(int i=0;i<8;i++){
            if(i==4){
                temp += "";
            }
            temp += r1.nextInt(10);
        }
        return mobileList.get(r.nextInt(mobileList.size()))+temp;
    }
}
