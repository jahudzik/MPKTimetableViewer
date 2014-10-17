package pl.jahu.mpk.samples;

import org.apache.commons.io.FileUtils;
import pl.jahu.mpk.entities.LineNumber;
import pl.jahu.mpk.parsers.LineRouteParser;
import pl.jahu.mpk.parsers.LinesListParser;
import pl.jahu.mpk.parsers.exceptions.LineRouteParseException;
import pl.jahu.mpk.parsers.exceptions.TimetableNotFoundException;
import pl.jahu.mpk.utils.LineNumbersResolver;
import pl.jahu.mpk.utils.UrlResolver;
import pl.jahu.mpk.validators.exceptions.NoDataProvidedException;
import pl.jahu.mpk.validators.exceptions.UnsupportedLineNumberException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-09-05.
 */
public class TimetablesDownloader {

    public static final String TIMETABLES_LOCATION = "offline/timetables/";
    public static final String LINES_PAGE_NAME = "_lines.html";
    public static final String MENU_PAGE_NAME = "_menu.html";
    public static final String STATIONS_PAGE_NAME = "_stations.html";

    /**
     * Downloads:
     * - lines list page, showing which lines timetables has changed
     * - menu page, containing next timetable changes dates and links to new timetables
     */
    public static void downloadInfo() {
        downloadUrl(UrlResolver.LINES_LIST_URL, LINES_PAGE_NAME);
        downloadUrl(UrlResolver.TIMETABLE_MENU_URL, MENU_PAGE_NAME);
        downloadUrl(UrlResolver.STATIONS_LIST_URL, STATIONS_PAGE_NAME);
    }

    public static void downloadTimetables(int firstLine, int lastLine) throws NoDataProvidedException, UnsupportedLineNumberException {
        downloadTimetables(new LineNumber(firstLine), new LineNumber(lastLine));
    }


    /**
     * Downloads timetables for lines from specified range [firstLine, lastLine] and saves them locally
     */
    public static void downloadTimetables(LineNumber firstLine, LineNumber lastLine) {

        try {
            LinesListParser linesListParser = new LinesListParser();
            List<LineNumber> lines = linesListParser.parse();

            int[] linesRange = LineNumbersResolver.getLinesFromRange(lines, firstLine, lastLine);
            for (int i = linesRange[0]; i <= linesRange[1]; i++) {
                LineNumber line = lines.get(i);
                for (int j = 1; j < 10; j++) {
                    try {
                        LineRouteParser routeParser = new LineRouteParser(line, j);
                        List<String[]> route = routeParser.parse();
                        for (String[] station : route) {
                            String url = UrlResolver.getStationTimetableUrl(line, station[1]);
                            downloadUrl(url, getPageName(url));
                        }
                    } catch (TimetableNotFoundException e) {
                        break;
                    }
                }
            }
        } catch (TimetableNotFoundException e) {
            e.printStackTrace();
        } catch (LineRouteParseException e) {
            e.printStackTrace();
        } catch (NoDataProvidedException e) {
            e.printStackTrace();
        } catch (UnsupportedLineNumberException e) {
            e.printStackTrace();
        }
    }


    private static void downloadUrl(String url, String destFileName) {
        try {
            System.out.println(url);
            FileUtils.copyURLToFile(new URL(url), new File(TIMETABLES_LOCATION + destFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getPageName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

}
