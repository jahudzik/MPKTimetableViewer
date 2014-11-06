package pl.jahu.mpk.providers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.jahu.mpk.DaggerApplication;
import pl.jahu.mpk.entities.LineNumber;
import pl.jahu.mpk.parsers.data.ParsableData;
import pl.jahu.mpk.parsers.exceptions.ParsableDataNotFoundException;
import pl.jahu.mpk.utils.UrlResolver;

import java.io.File;
import java.io.IOException;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-10-21.
 */
public class FileTimetableProvider extends TimetableProvider {

    public static final String LINES_LIST_FILE = "_lines.html";
    public static final String MENU_PAGE_FILE = "_menu.html";
    private static final String DIRECTION_TOKEN = "@[seg]";
    private static final String LINE_NUMBER_TOKEN = "@[line]";
    private static final String LINE_ROUTE_FILE_PATTERN = LINE_NUMBER_TOKEN + "w00" + DIRECTION_TOKEN + ".htm";
    private static final String PAGE_TOKEN = "@[page]";
    private static final String TIMETABLE_FILE_PATTERN = LINE_NUMBER_TOKEN + "t" + PAGE_TOKEN + ".htm";
    public static final String FILE_ENCODING = "iso-8859-2";

    private final String filesLocation;


    public FileTimetableProvider(String filesLocation) {
        DaggerApplication.inject(this);
        this.filesLocation = filesLocation;
    }

    @Override
    public ParsableData getLinesListDocument() throws ParsableDataNotFoundException {
        return getDocumentFromFile(LINES_LIST_FILE);
    }

    @Override
    public ParsableData getLineRouteDocument(LineNumber lineNo, int direction) throws ParsableDataNotFoundException {
        return getDocumentFromFile(getLineRouteFileName(lineNo, direction));
    }

    @Override
    public ParsableData getTimetableDocument(LineNumber lineNumber, int sequenceNumber) throws ParsableDataNotFoundException {
        return getDocumentFromFile(getStationTimetableFileName(lineNumber, sequenceNumber));
    }

    private ParsableData getDocumentFromFile(String fileName) throws ParsableDataNotFoundException {
        try {
            String fileLocation = filesLocation + "/" + fileName;
            Document document = Jsoup.parse(new File(fileLocation), FILE_ENCODING);
            return new ParsableData(document, fileLocation);
        } catch (IOException e) {
            throw new ParsableDataNotFoundException(e.getMessage());
        }
    }

    private static String getLineRouteFileName(LineNumber lineNo, Integer direction) {
        return LINE_ROUTE_FILE_PATTERN.replace(LINE_NUMBER_TOKEN, UrlResolver.getLineLiteral(lineNo)).replace(DIRECTION_TOKEN, direction.toString());
    }

    public static String getStationTimetableFileName(LineNumber lineNumber, int sequenceNumber) {
        return TIMETABLE_FILE_PATTERN.replace(LINE_NUMBER_TOKEN, UrlResolver.getLineLiteral(lineNumber)).replace(PAGE_TOKEN, UrlResolver.getSequenceNumberLiteral(sequenceNumber));
    }

}
