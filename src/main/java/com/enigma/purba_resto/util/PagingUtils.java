package com.enigma.purba_resto.util;

public class PagingUtils {
    public static Integer validatePage(Integer page) {
        return page <= 0 ? 1 : page;
    }
    public static Integer validateSize(Integer size) {
        return size <= 0 ? 10 : size;
    }

    public static String validateDirection(String direction) {
        return direction.equals("ASC") || direction.equals("DSC") ? direction : "ASC";
    }

}
