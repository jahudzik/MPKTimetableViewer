package pl.jahu.mpk.utils;

import pl.jahu.mpk.entities.LineNumber;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-08-30.
 */
public class UrlResolver {

    private static final String DIRECTION_TOKEN = "@[seg]";
    private static final String LINE_NUMBER_TOKEN = "@[line]";
    private static final String SEQUENCE_TOKEN = "@[seq]";

    private static final String HOST_URL = "http://rozklady.mpk.krakow.pl/";
    private static final String ACT_HOST_URL = HOST_URL + "aktualne";

    public static final String LINES_LIST_URL = HOST_URL + "linie.aspx";
    public static final String UPDATE_INFO_URL = HOST_URL + "menu.aspx";
    public static final String STATIONS_LIST_URL = ACT_HOST_URL + "/" + "przystan.htm";

    private static final String LINE_ROUTE_FILE_PATTERN = LINE_NUMBER_TOKEN + "w00" + DIRECTION_TOKEN + ".htm";
    private static final String LINE_ROUTE_URL_PATTERN = ACT_HOST_URL + "/" + LINE_NUMBER_TOKEN + "/" + LINE_ROUTE_FILE_PATTERN;

    private static final String TIMETABLE_FILE_PATTERN = LINE_NUMBER_TOKEN + "t" + SEQUENCE_TOKEN + ".htm";
    private static final String TIMETABLE_URL_PATTERN = ACT_HOST_URL + "/" + LINE_NUMBER_TOKEN + "/" + TIMETABLE_FILE_PATTERN;

    public static String getLineRouteUrl(LineNumber lineNo, Integer direction) {
        return LINE_ROUTE_URL_PATTERN.replace(LINE_NUMBER_TOKEN, getLineLiteral(lineNo)).replace(DIRECTION_TOKEN, direction.toString());
    }

    public static String getStationTimetableUrl(LineNumber lineNo, int sequenceNumber) {
        return TIMETABLE_URL_PATTERN.replace(LINE_NUMBER_TOKEN, getLineLiteral(lineNo)).replace(SEQUENCE_TOKEN, getSequenceNumberLiteral(sequenceNumber));
    }

    public static String getLineLiteral(LineNumber lineNumber)  {
        return getLineNumberLiteral(lineNumber);
    }

    private static String getLineNumberLiteral(LineNumber lineNumber) {
        switch (lineNumber.getValue().length()) {
            case 1:
                return "000" + lineNumber.toString();
            case 2:
                return "00" + lineNumber.toString();
            case 3:
                return "0" + lineNumber.toString();
            case 4:
                return lineNumber.toString();
            default:
                return null;
        }
    }

    public static String getSequenceNumberLiteral(Integer sequenceNumber) {
        return (sequenceNumber < 10) ? "00" + sequenceNumber.toString() : (sequenceNumber < 100) ? "0" + sequenceNumber.toString() : sequenceNumber.toString();
    }

    public static String getLineRouteFileName(LineNumber lineNo, Integer direction) {
        return LINE_ROUTE_FILE_PATTERN.replace(LINE_NUMBER_TOKEN, getLineLiteral(lineNo)).replace(DIRECTION_TOKEN, direction.toString());
    }

    public static String getStationTimetableFileName(LineNumber lineNumber, int sequenceNumber) {
        return TIMETABLE_FILE_PATTERN.replace(LINE_NUMBER_TOKEN, getLineLiteral(lineNumber)).replace(SEQUENCE_TOKEN, getSequenceNumberLiteral(sequenceNumber));
    }

}
