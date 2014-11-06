package pl.jahu.mpk.parsers;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.jahu.mpk.entities.LineNumber;
import pl.jahu.mpk.parsers.data.ParsableData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hudzj on 8/1/2014.
 */
public class LinesListParser {

    /**
     * Parses lines list page and return list of lines
     *
     */
    public List<LineNumber> parse(ParsableData parsableData) {
        List<LineNumber> lines = new ArrayList<LineNumber>();
        Elements links = parsableData.getDocument().getElementsByTag("a");
        for (Element link : links) {
            // ensure this is a link to line details - it should have "[line_number]rw" substring (ex http://rozklady.mpk.krakow.pl/aktualne/0164/0164rw01.htm)
            String href = link.attr("href");
            if (href != null && href.contains(link.text() + "rw")) {
                lines.add(new LineNumber(link.text()));
            }
        }
        return lines;
    }

}