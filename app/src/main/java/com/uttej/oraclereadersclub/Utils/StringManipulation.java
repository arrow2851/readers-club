package com.uttej.oraclereadersclub.Utils;

/**
 * Created by Clean on 27-03-2018.
 */

public class StringManipulation {

    public static String getGenres(String string){
        if(string.indexOf("#") > 0){
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();
            boolean foundWord = false;
            for(char c: charArray){
                if(c == '#'){
                    foundWord = true;
                    sb.append(c);
                }else{
                    if(foundWord)
                        sb.append(c);
                }
                if(c == ' '){
                    foundWord = false;
                }
            }
            String s = sb.toString().replace(" ","").replace("#", ",#");
            return s.substring(1, s.length());
        }
        return string;
    }

    public static String getBookTitle(String string){
        if(string.indexOf("#") > 0){
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();
            for(char c: charArray){
                if(c == '#'){
                    return sb.toString().toLowerCase().trim();
                }
                else{
                    sb.append(c);
                }
            }
        }
        return string.toLowerCase();
    }
}
