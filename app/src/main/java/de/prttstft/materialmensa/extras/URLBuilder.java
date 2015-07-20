package de.prttstft.materialmensa.extras;

import de.prttstft.materialmensa.BuildConfig;
import de.prttstft.materialmensa.activities.ActivityMain;

public class URLBuilder {
    private static DateHelper dateHelper = new DateHelper();

    public static final String URL_CHAR_AMEPERSAND = "&";
    public static final String URL_API = BuildConfig.API_URL;
    public static final String URL_PARAM_RESTAURANT = "restaurant=";
    public static final String URL_PARAM_DATE = "date=";
    public static final String URL_RESTAURANT_ACADEMICA = "mensa-academica-paderborn";
    public static final String URL_RESTAURANT_FORUM = "mensa-forum-paderborn";
    public static final String URL_RESTAURANT_CAFETE = "cafete";
    public static final String URL_RESTAURANT_MENSULA = "mensula";
    public static final String URL_RESTAURANT_ONEWAYSNACK = "one-way-snack";
    public static final String URL_RESTAURANT_GRILLCAFE = "grill-cafe";
    public static final String URL_RESTAURANT_CAMPUSDOENER = "campus-doener";
    public static final String URL_RESTAURANT_HAMM = "mensa-hamm";
    public static final String URL_RESTAURANT_LIPPSTADT = "mensa-lippstadt";
    public static final String URL_RESTAURANT_BISTROHOTSPOT = "bistro-hotspot";

    public static String getRequestUrl() {
        return URL_API;
    }

    public static String getRequestUrl(int day) {
        return URL_API +
                URL_CHAR_AMEPERSAND +
                URL_PARAM_DATE +
                dateHelper.getDate(day);
    }

    public static String getRequestUrl(int day, int restaurant) {
        return URL_API +
                URL_CHAR_AMEPERSAND +
                URL_PARAM_DATE +
                dateHelper.getDate(day) +
                URL_CHAR_AMEPERSAND +
                URL_PARAM_RESTAURANT +
                convertIntToRestaurant(restaurant);
    }

    private static String convertIntToRestaurant(int restaurant) {
        switch (restaurant) {
            case 0:
                return URL_RESTAURANT_ACADEMICA;
            case 1:
                return URL_RESTAURANT_FORUM;
            case 2:
                return URL_RESTAURANT_CAFETE;
            case 3:
                return URL_RESTAURANT_GRILLCAFE;
            case 4:
                return URL_RESTAURANT_CAMPUSDOENER;
            case 5:
                return URL_RESTAURANT_ONEWAYSNACK;
            case 6:
                return URL_RESTAURANT_MENSULA;
            case 7:
                return URL_RESTAURANT_HAMM;
            case 8:
                return URL_RESTAURANT_LIPPSTADT;
            case 9:
                return URL_RESTAURANT_BISTROHOTSPOT;
            default:
                return "";
        }
    }
}