package org.foi.nwtis.damdrempe.rest.klijenti;

/**
 * 
 * @author ddrempetic
 */
public class OWMRESTHelper {
    private static final String OWM_BASE_URI = "http://api.openweathermap.org/data/2.5/";    
    private String apiKey;


    public OWMRESTHelper(String apiKey) {
        this.apiKey = apiKey;
    }

    public static String getOWM_BASE_URI() {
        return OWM_BASE_URI;
    }

    public static String getOWM_Current_Path() {
        return "weather";
    }
        
    public static String getOWM_Forecast_Path() {
        return "forecast";
    }
        
    public static String getOWM_ForecastDaily_Path() {
        return "forecast/daily";
    }
        
    public static String getOWM_StationsNear_Path() {
        return "station/find";
    }
        
    public static String getOWM_Station_Path() {
        return "station";
    }
        
}
