package de.prttstft.materialmensa.extras;

import android.content.Context;

import de.prttstft.materialmensa.R;

public class AllergensAdditives {
    private static Context context;

    public static String A1 = context.getResources().getString(R.string.gluten);
    public static String A2 = context.getResources().getString(R.string.crab);
    public static String A3 = context.getResources().getString(R.string.egg);
    public static String A4 = context.getResources().getString(R.string.fish);
    public static String A5 = context.getResources().getString(R.string.peanuts);
    public static String A6 = context.getResources().getString(R.string.soy);
    public static String A7 = context.getResources().getString(R.string.milk);
    public static String A8 = context.getResources().getString(R.string.nuts);
    public static String A9 = context.getResources().getString(R.string.celery);
    public static String A10 = context.getResources().getString(R.string.mustard);
    public static String A11 = context.getResources().getString(R.string.sesame);
    public static String A12 = context.getResources().getString(R.string.sulfites);
    public static String A13 = context.getResources().getString(R.string.lupins);
    public static String A14 = context.getResources().getString(R.string.mulluscs);

    public static String Z1 = context.getResources().getString(R.string.dyes);
    public static String Z2 = context.getResources().getString(R.string.preservatives);
    public static String Z3 = context.getResources().getString(R.string.antioxidant);
    public static String Z4 = context.getResources().getString(R.string.flavorenhancers);
    public static String Z5 = context.getResources().getString(R.string.phosphate);
    public static String Z6 = context.getResources().getString(R.string.sulphuretted);
    public static String Z7 = context.getResources().getString(R.string.waxed);
    public static String Z8 = context.getResources().getString(R.string.blacked);
    public static String Z9 = context.getResources().getString(R.string.sweeteners);
    public static String Z10 = context.getResources().getString(R.string.phenylalanine);
    public static String Z11 = context.getResources().getString(R.string.taurine);
    public static String Z12 = context.getResources().getString(R.string.nitritesalting);
    public static String Z13 = context.getResources().getString(R.string.caffeine);
    public static String Z14 = context.getResources().getString(R.string.quinine);
    public static String Z15 = context.getResources().getString(R.string.milkprotein);

    public String getAllergene (int allergene){
        if (allergene == 1) {
            return A1;
        } else if (allergene == 2) {
            return A2;
        } else if (allergene == 3) {
            return A3;
        } else if (allergene == 4) {
            return A4;
        } else if (allergene == 5) {
            return A5;
        } else if (allergene == 6) {
            return A6;
        } else if (allergene == 7) {
            return A7;
        } else if (allergene == 8) {
            return A8;
        } else if (allergene == 9) {
            return A9;
        } else if (allergene == 10) {
            return A10;
        } else if (allergene == 11) {
            return A11;
        } else if (allergene == 12) {
            return A12;
        } else if (allergene == 13) {
            return A13;
        } else if (allergene == 14) {
            return A14;
        }
        else {
            return null;
        }
    }

    public String getAdditive (int additive){
        if (additive == 1) {
            return Z1;
        } else if (additive == 2) {
            return Z2;
        } else if (additive == 3) {
            return Z3;
        } else if (additive == 4) {
            return Z4;
        } else if (additive == 5) {
            return Z5;
        } else if (additive == 6) {
            return Z6;
        } else if (additive == 7) {
            return Z7;
        } else if (additive == 8) {
            return Z8;
        } else if (additive == 9) {
            return Z9;
        } else if (additive == 10) {
            return Z10;
        } else if (additive == 11) {
            return Z11;
        } else if (additive == 12) {
            return Z12;
        } else if (additive == 13) {
            return Z13;
        } else if (additive == 14) {
            return Z14;
        }
        else if (additive == 14) {
            return Z15;
        }
        else {
            return "0";
        }
    }
}