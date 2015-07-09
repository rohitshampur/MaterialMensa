package de.prttstft.materialmensa.extras;

import de.prttstft.materialmensa.BuildConfig;
import de.prttstft.materialmensa.activities.ActivityMain;

public class URLBuilder {

    static String today = ActivityMain.today;
    static String tomorrow = ActivityMain.tomorrow;

    public static final String URL_CHAR_AMEPERSAND = "&";
    public static final String URL_API = BuildConfig.API_URL;
    public static final String URL_PARAM_RESTAURANT = "restaurant=";
    public static final String URL_PARAM_DATE = "date=";
    public static final String URL_PARAM_TODAY = today ;
    public static final String URL_PARAM_TOMORROW = tomorrow;
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
        return URL_API +
                URL_CHAR_AMEPERSAND +
                URL_PARAM_DATE +
                URL_PARAM_TODAY
                ;
    }

    /*
    public static String getRequestUrl() {
        if (ActivityMain.mensaID == 1) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_FORUM
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;

        } else if (ActivityMain.mensaID == 2) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_CAFETE
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 3) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_GRILLCAFE
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 4) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_CAMPUSDOENER
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 5) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_ONEWAYSNACK
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 6) {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_MENSULA
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        } else {
            return URL_API
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_RESTAURANT
                    + URL_RESTAURANT_ACADEMICA
                    + URL_CHAR_AMEPERSAND
                    + URL_PARAM_DATE
                    + URL_PARAM_TODAY
                    ;
        }
    }
    */
}