package org.nervos.ckb.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class FileUtils {

    public static String readFile(String path) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            StringBuilder strBuilder = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                strBuilder.append(str).append("\n");
            }
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
