package de.prttstft.materialmensa.extras;

import de.prttstft.materialmensa.activities.ActivityMain;

public class URLBuilder {
    public static String getRequestUrl() {
        if (ActivityMain.mensaID == 1) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_FORUM
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;

        } else if (ActivityMain.mensaID == 2) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_CAFETE
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 3) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_GRILLCAFE
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 4) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_CAMPUSDOENER
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 5) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_ONEWAYSNACK
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 6) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_MENSULA
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_ACADEMICA
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        }
    }
}