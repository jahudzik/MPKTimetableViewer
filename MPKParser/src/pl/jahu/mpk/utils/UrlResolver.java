package pl.jahu.mpk.utils;

import pl.jahu.mpk.entities.LineNumber;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-08-30.
 */
public class UrlResolver {

    private final static String DIRECTION_TOKEN = "@[seg]";
    private final static String LINE_NUMBER_TOKEN = "@[line]";
    private final static String PAGE_TOKEN = "@[page]";

    public static final String LINES_LIST_URL = "http://rozklady.mpk.krakow.pl/linie.aspx";
    public static final String TIMETABLE_MENU_URL = "http://rozklady.mpk.krakow.pl/menu.aspx";
    public static final String STATIONS_LIST_URL = "http://rozklady.mpk.krakow.pl/aktualne/przystan.htm";

    private static final String LINE_ROUTE_URL_PATTERN = "http://rozklady.mpk.krakow.pl/aktualne/"+LINE_NUMBER_TOKEN+"/"+LINE_NUMBER_TOKEN+"w00"+DIRECTION_TOKEN+".htm";
    private static final String TIMETABLE_URL_PATTERN = "http://rozklady.mpk.krakow.pl/aktualne/" + LINE_NUMBER_TOKEN + "/" + PAGE_TOKEN;

    public static String getLineRouteUrl(LineNumber lineNo, Integer direction) {
        return LINE_ROUTE_URL_PATTERN.replace(LINE_NUMBER_TOKEN, LineNumbersResolver.getLineString(lineNo)).replace(DIRECTION_TOKEN, direction.toString());
    }

    public static String getStationTimetableUrl(LineNumber lineNo, String page) {
        return TIMETABLE_URL_PATTERN.replace(LINE_NUMBER_TOKEN, LineNumbersResolver.getLineString(lineNo)).replace(PAGE_TOKEN, page);
    }

}
