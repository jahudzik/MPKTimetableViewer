package pl.jahu.mpk.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.jahu.mpk.parser.exceptions.TimetableNotFoundException;
import pl.jahu.mpk.parser.exceptions.TimetableParseException;
import pl.jahu.mpk.parser.utils.UrlResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hudzj on 8/1/2014.
 */
public class LinesListParser extends AbstractParser {

    public LinesListParser() throws TimetableNotFoundException {
        super(UrlResolver.getLinesListUrl());
    }

    public LinesListParser(File file, String encoding) throws IOException, TimetableParseException {
        super(file, encoding);
    }

    public List<Integer> parse() {
        List<Integer> lines = new ArrayList<Integer>();
        Elements links = document.getElementsByTag("a");
        for (Element link : links) {
            String lineCandidate = link.text();
            try {
                int line = Integer.parseInt(lineCandidate);
                lines.add(line);
            } catch (NumberFormatException ignored) {}
        }
        return lines;
    }




}
