package com.example.omega.geobangla2;

import java.util.ArrayList;

public class StoredResources {
    private static ArrayList<String> storedImageUrls = new ArrayList<>();
    private static ArrayList<String> storedNames = new ArrayList<>();
    private static int pos;
    private static String clickedDivision;
    private static int resortPosition;

    public static int getResortPosition() {
        return resortPosition;
    }

    public static void setResortPosition(int resortPosition) {
        StoredResources.resortPosition = resortPosition;
    }

    public void setStoredResources(ArrayList<String> imageUrls, ArrayList<String> imageNames){
        storedImageUrls = imageUrls;
        storedNames = imageNames;

    }
    public String getStoredImageUrls(int i){
        return storedImageUrls.get(i);
    }
    public String getStoredNames(int i){
        return storedNames.get(i);
    }

    public static void setPos(int pos) {
        StoredResources.pos = pos;
    }

    public static int getPos() {
        return pos;
    }
    public static void setClickedDivision(String clickedDivision){
        StoredResources.clickedDivision = clickedDivision;
    }
    public static String getClickedDivision(){
        return StoredResources.clickedDivision;
    }
}
