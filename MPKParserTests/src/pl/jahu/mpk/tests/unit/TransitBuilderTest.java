package pl.jahu.mpk.tests.unit;

import org.junit.Test;
import pl.jahu.mpk.TransitBuilder;
import pl.jahu.mpk.entities.Departure;
import pl.jahu.mpk.entities.LineNumber;
import pl.jahu.mpk.entities.Timetable;
import pl.jahu.mpk.entities.Transit;
import pl.jahu.mpk.enums.DayTypes;
import pl.jahu.mpk.parser.utils.TimeUtils;
import pl.jahu.mpk.validators.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-09-01.
 */
public class TransitBuilderTest {

    public static final String[] STATIONS = {"Station 1", "Station 2", "Station 3", "Station 4", "Station 5"};
    public static final String LAST_STATION = "Last Station";


    @Test
    public void testNullInput() throws TransitValidationException {
        Map<DayTypes, List<Transit>> transits = TransitBuilder.buildFromTimetables(null);
        assertNotNull(transits);
        assertEquals(0, transits.size());
    }


    @Test
      public void testRegularTimetables() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY, DayTypes.SUNDAY}, new int[][]{{12, 10, 12, 20, 12, 30}, {12,  0}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY, DayTypes.SUNDAY}, new int[][]{{12, 12, 12, 22, 12, 32}, {12,  2}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY, DayTypes.SUNDAY}, new int[][]{{12, 13, 12, 23, 12, 33}, {12,  3}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY, DayTypes.SUNDAY}, new int[][]{{12, 16, 12, 26, 12, 36}, {12,  6}}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY, DayTypes.SUNDAY}, new int[][]{{12, 18, 12, 28, 12, 38}, {12,  8}}, STATIONS[4], 123, LAST_STATION));
        Map<DayTypes, List<Transit>> transitsMap = TransitBuilder.buildFromTimetables(timetables);

        assertEquals(2, transitsMap.keySet().size());

        List<Transit> weekTransits = transitsMap.get(DayTypes.WEEKDAY);
        assertNotNull(weekTransits);
        assertEquals(3, weekTransits.size());
        validateTransit(weekTransits.get(0), 5, 8, LAST_STATION, STATIONS, new int[]{12, 10, 12, 12, 12, 13, 12, 16, 12, 18});
        validateTransit(weekTransits.get(1), 5, 8, LAST_STATION, STATIONS, new int[]{12, 20, 12, 22, 12, 23, 12, 26, 12, 28});
        validateTransit(weekTransits.get(2), 5, 8, LAST_STATION, STATIONS, new int[]{12, 30, 12, 32, 12, 33, 12, 36, 12, 38});

        List<Transit> sundayTransits = transitsMap.get(DayTypes.SUNDAY);
        assertNotNull(sundayTransits);
        assertEquals(1, sundayTransits.size());
        validateTransit(sundayTransits.get(0), 5, 8, LAST_STATION, STATIONS, new int[]{12, 00, 12, 02, 12, 03, 12, 06, 12, 8});
    }


    @Test
    public void testTimetablesWithDifferentBeginningAndEnd() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10        , 12, 30}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 12        , 12, 32}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 13, 12, 23, 12, 33}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 16, 12, 26,       }}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 18, 12, 28,       }}, STATIONS[4], 123, LAST_STATION));
        Map<DayTypes, List<Transit>> transitsMap = TransitBuilder.buildFromTimetables(timetables);

        assertEquals(1, transitsMap.keySet().size());

        List<Transit> weekTransits = transitsMap.get(DayTypes.WEEKDAY);
        assertNotNull(weekTransits);
        assertEquals(3, weekTransits.size());
        // first transit makes full route - from 'Station 1' to 'Last Station'
        validateTransit(weekTransits.get(0), 5, 8, LAST_STATION, STATIONS, new int[]{12, 10, 12, 12, 12, 13, 12, 16, 12, 18});
        // second transit starts later, from 'Station 3' and makes it to the end 'Last Station'
        validateTransit(weekTransits.get(1), 3, 5, LAST_STATION, new String[] {STATIONS[2], STATIONS[3], STATIONS[4]}, new int[]{12, 23, 12, 26, 12, 28});
        // third transit starts from 'Station 1', but finishes earlier on 'Station 4'
        validateTransit(weekTransits.get(2), 3, 3, STATIONS[3], new String[] {STATIONS[0], STATIONS[1], STATIONS[2]}, new int[]{12, 30, 12, 32, 12, 33});
    }

    @Test(expected = UnhandledTimetableDepartureException.class)
    public void testUnhandledTimetableDepartureException() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10, 12, 20, 12, 30}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 12, 12, 22, 12, 32}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 13, 12, 23, 12, 33}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 16, 12, 26, 12, 36, 12, 46}}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 18, 12, 28, 12, 38}}, STATIONS[4], 123, LAST_STATION));
        // there's an unexpected departure (12:46) in 4th timetable - UnhandledTimetableDepartureException should be thrown
        TransitBuilder.buildFromTimetables(timetables);
    }


    @Test(expected = IncorrectTransitDurationException.class)
    public void testIncorrectTransitDurationException() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10, 12, 20, 12, 30}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 12, 12, 22, 12, 34}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 13, 12, 23, 12, 37}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 16, 12, 26, 12, 41}}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 18, 12, 28, 12, 46}}, STATIONS[4], 123, LAST_STATION));
        // third tranist is much longer than the others - IncorrectTransitDurationException should be thrown
        TransitBuilder.buildFromTimetables(timetables);
    }


    @Test(expected = IncorrectTimeDifferenceBetweenStopsException.class)
    public void testIncorrectTimeDifferenceBetweenStopsException1() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10, 12, 20, 12, 30}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 12, 12, 22, 12, 32}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 13, 12, 23, 12, 37}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 16, 12, 26, 12, 38}}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 18, 12, 28, 12, 39}}, STATIONS[4], 123, LAST_STATION));
        // in the last transit there's much bigger difference between 2 and 3 stops (5 mins) than in previous transits (1 min) - IncorrectTimeDifferenceBetweenStopsException should be thrown
        TransitBuilder.buildFromTimetables(timetables);
    }

    @Test(expected = IncorrectTimeDifferenceBetweenStopsException.class)
    public void testIncorrectTimeDifferenceBetweenStopsException2() throws TransitValidationException {
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10, 12, 20, 12, 30}}, STATIONS[0], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 10, 12, 22, 12, 32}}, STATIONS[1], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 13, 12, 23, 12, 33}}, STATIONS[2], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 16, 12, 26, 12, 36}}, STATIONS[3], 123, LAST_STATION));
        timetables.add(buildTimetable(new DayTypes[]{DayTypes.WEEKDAY}, new int[][]{{12, 18, 12, 28, 12, 38}}, STATIONS[4], 123, LAST_STATION));
        // no time difference between first two stops in the first transit - IncorrectTimeDifferenceBetweenStopsException should be thrown
        TransitBuilder.buildFromTimetables(timetables);
    }




    private void validateTransit(Transit transit, int stopsCount, int duration, String destStation, String[] stations, int[] times) {
        assertEquals(stopsCount, transit.getStops().size());
        assertEquals(duration, transit.getDuration());
        assertEquals(destStation, transit.getDestStation());
        for (int i = 0; i < stations.length; i++) {
            assertEquals(stations[i], transit.getStops().get(i).getStation());
            assertEquals(TimeUtils.timeValue(times[2*i], times[2*i+1]), transit.getStops().get(i).getTime().getTimeValue());
        }
    }

    /**
     * Builds timetable based on passed int values representing times (grouped by day type)
     */
    private Timetable buildTimetable(DayTypes[] dayTypes, int[][] timesMap, String station, int lineNo, String destStation) {
        try {
            Map<DayTypes, List<Departure>> map = new HashMap<DayTypes, List<Departure>>();
            for (int i = 0; i < dayTypes.length; i++) {
                int[] times = timesMap[i];
                List<Departure> departures = new ArrayList<Departure>();
                for (int j = 0; j < times.length; j+=2) {
                    departures.add(new Departure(times[j], times[j + 1]));
                }
                map.put(dayTypes[i], departures);
            }
            return new Timetable(station, new LineNumber(lineNo), destStation, map);
        } catch (UnsupportedLineNumberException e) {
           fail(e.getMessage());
            return null;
        }
    }

}
