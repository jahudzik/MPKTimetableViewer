package pl.jahu.mpk.parsers;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import pl.jahu.mpk.DaggerApplication;
import pl.jahu.mpk.DefaultTestModule;
import pl.jahu.mpk.TestUtils;
import pl.jahu.mpk.entities.Line;
import pl.jahu.mpk.entities.LineInfo;
import pl.jahu.mpk.enums.Areas;
import pl.jahu.mpk.enums.LineTypes;
import pl.jahu.mpk.enums.Vehicles;
import pl.jahu.mpk.parsers.data.ParsableData;
import pl.jahu.mpk.parsers.exceptions.ParsableDataNotFoundException;
import pl.jahu.mpk.parsers.exceptions.TimetableParseException;
import pl.jahu.mpk.providers.TimetableProvider;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * MPK Timetable Parser
 * Created by hudzj on 8/1/2014.
 */
public class LinesListParserTest {

    @Inject
    TimetableProvider timetableProvider;

    private LinesListParser linesListParser;

    @Before
    public void setUp() {
        linesListParser = new LinesListParser();
    }

    /******************** TESTS ********************/

    @Test
    public void getLinesList_numbericOnly() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 169);
    }

    @Test
    public void getLinesList_literals() throws ParsableDataNotFoundException {
        initModule("2014-10-17");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 184);
    }

    @Test
    public void getChangedLinesList_test1() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseChanged(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 11);
    }

    @Test
    public void getChangedLinesList_test2() throws ParsableDataNotFoundException {
        initModule("2014-11-01");
        List<Line> lines = linesListParser.parseChanged(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 47);
    }

    @Test
    public void getChangedLinesList_oneChangeOnly() throws ParsableDataNotFoundException {
        initModule("2014-10-17");
        List<Line> lines = linesListParser.parseChanged(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 1);
    }

    @Test
    public void getChangedLinesList_noChanges() throws ParsableDataNotFoundException {
        initModule("no_changes");
        List<Line> lines = linesListParser.parseChanged(timetableProvider.getLinesListDocument());
        TestUtils.checkCollectionSize(lines, 0);
    }

    @Test
    public void getLinesList_tramStandardCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 1, Vehicles.TRAM, LineTypes.STANDARD, Areas.CITY);
        checkExpectedLine(lines, 52, Vehicles.TRAM, LineTypes.STANDARD, Areas.CITY);
    }

    @Test
    public void getLinesList_tramReplacementCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 72, Vehicles.TRAM, LineTypes.REPLACEMENT, Areas.CITY);
    }

    @Test
    public void getLinesList_tramSpecialCity1() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 0, Vehicles.TRAM, LineTypes.SPECIAL, Areas.CITY);
    }

    @Test
    public void getLinesList_tramSpecialCity2() throws ParsableDataNotFoundException {
        initModule("2014-11-01");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 86, Vehicles.TRAM, LineTypes.SPECIAL, Areas.CITY);
    }

    @Test
    public void getLinesList_tramNightlyCity1() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 62, Vehicles.TRAM, LineTypes.NIGHTLY, Areas.CITY);
        checkExpectedLine(lines, 69, Vehicles.TRAM, LineTypes.NIGHTLY, Areas.CITY);
    }

    @Test
    public void getLinesList_tramNightlyCity2() throws ParsableDataNotFoundException {
        initModule("2014-11-01");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, "64a", Vehicles.TRAM, LineTypes.NIGHTLY, Areas.CITY);
    }

    @Test
    public void getLinesList_busStandardCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 100, Vehicles.BUS, LineTypes.STANDARD, Areas.CITY);
        checkExpectedLine(lines, 159, Vehicles.BUS, LineTypes.STANDARD, Areas.CITY);
        checkExpectedLine(lines, 179, Vehicles.BUS, LineTypes.STANDARD, Areas.CITY);
    }

    @Test
    public void getLinesList_busStandardAgglomeration() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 201, Vehicles.BUS, LineTypes.STANDARD, Areas.AGGLOMERATION);
        checkExpectedLine(lines, 202, Vehicles.BUS, LineTypes.STANDARD, Areas.AGGLOMERATION);
    }

    @Test
    public void getLinesList_busRapidAgglomeration() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 301, Vehicles.BUS, LineTypes.RAPID, Areas.AGGLOMERATION);
        checkExpectedLine(lines, 352, Vehicles.BUS, LineTypes.RAPID, Areas.AGGLOMERATION);
    }

    @Test
    public void getLinesList_busAdditionalCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 405, Vehicles.BUS, LineTypes.ADDITIONAL, Areas.CITY);
        checkExpectedLine(lines, 422, Vehicles.BUS, LineTypes.ADDITIONAL, Areas.CITY);
    }

    @Test
    public void getLinesList_busRapidCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 502, Vehicles.BUS, LineTypes.RAPID, Areas.CITY);
        checkExpectedLine(lines, 572, Vehicles.BUS, LineTypes.RAPID, Areas.CITY);
    }

    @Test
    public void getLinesList_busNightlyCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 601, Vehicles.BUS, LineTypes.NIGHTLY, Areas.CITY);
        checkExpectedLine(lines, 605, Vehicles.BUS, LineTypes.NIGHTLY, Areas.CITY);
    }

    @Test
    public void getLinesList_busReplacementCity() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 704, Vehicles.BUS, LineTypes.REPLACEMENT, Areas.CITY);
        checkExpectedLine(lines, 724, Vehicles.BUS, LineTypes.REPLACEMENT, Areas.CITY);
    }

    @Test
    public void getLinesList_busNightlyAgglomeration() throws ParsableDataNotFoundException {
        initModule("default");
        List<Line> lines = linesListParser.parseAll(timetableProvider.getLinesListDocument());
        checkExpectedLine(lines, 902, Vehicles.BUS, LineTypes.NIGHTLY, Areas.AGGLOMERATION);
        checkExpectedLine(lines, 904, Vehicles.BUS, LineTypes.NIGHTLY, Areas.AGGLOMERATION);
    }

    @Test
    public void parseLastUpdateDate_noPlannedUpdateDates() throws ParsableDataNotFoundException, TimetableParseException {
        initModule("2014-10-20");
        ParsableData parsableData = timetableProvider.getUpdateInfoDocument();
        DateTime date = linesListParser.parseLastUpdateDate(parsableData);
        checkDate(date, 20, 10, 2014);
    }

    @Test
    public void parseLastUpdateDate_withTwoPlannedUpdateDates() throws ParsableDataNotFoundException, TimetableParseException {
        initModule("default");
        ParsableData parsableData = timetableProvider.getUpdateInfoDocument();
        DateTime date = linesListParser.parseLastUpdateDate(parsableData);
        checkDate(date, 5, 9, 2014);
    }

    /******************** API ********************/

    private void initModule(String filesLocation) {
        DaggerApplication.init(new DefaultTestModule(filesLocation));
        DaggerApplication.inject(this);
    }

    private void checkExpectedLine(List<Line> lines, int expectedNumber, Vehicles expectedVehicleType, LineTypes expectedReasonType, Areas expectedAreaType) {
        assertTrue("Didn't find expected line number [" + expectedNumber + "]", lines.contains(new Line(expectedNumber, new LineInfo(expectedVehicleType, expectedReasonType, expectedAreaType))));
    }

    private void checkExpectedLine(List<Line> lines, String expectedNumber, Vehicles expectedVehicleType, LineTypes expectedReasonType, Areas expectedAreaType) {
        assertTrue("Didn't find expected line number [" + expectedNumber + "]", lines.contains(new Line(expectedNumber, new LineInfo(expectedVehicleType, expectedReasonType, expectedAreaType))));
    }

    private void checkDate(DateTime date, int expectedDay, int expectedMonth, int expectedYear) {
        assertEquals(expectedDay, date.getDayOfMonth());
        assertEquals(expectedMonth, date.getMonthOfYear());
        assertEquals(expectedYear, date.getYear());
    }

}
