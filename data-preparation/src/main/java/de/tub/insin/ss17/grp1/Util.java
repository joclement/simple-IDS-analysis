package de.tub.insin.ss17.grp1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;

public class Util {

    public static void saveAsArff(Instances data, File arff) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(arff));
        writer.write(data.toString());
        writer.flush();
        writer.close();
    }

    public static File saveAsArff(Instances data) throws IOException {
        File arffTmp = File.createTempFile("combinedArff", ".arff");
        saveAsArff(data, arffTmp);
        return arffTmp;
    }

    public static int hex2decimal(String s) {
        s = s.substring(2);
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}
