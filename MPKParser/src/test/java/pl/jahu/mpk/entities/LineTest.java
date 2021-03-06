package pl.jahu.mpk.entities;

import org.junit.Test;
import pl.jahu.mpk.TestUtils;
import pl.jahu.mpk.enums.Areas;
import pl.jahu.mpk.enums.LineTypes;
import pl.jahu.mpk.enums.Vehicles;

import static org.junit.Assert.*;

/**
 * MPK Timetable Parser
 * Created by jahudzik on 2014-11-17.
 */
public class LineTest {

    @Test
    public void equals_test1() {
        Line line = new Line(1, TestUtils.EXAMPLE_LINE_TYPE);
        assertTrue(line.equals(line));
    }

    @Test
    public void equals_test2() {
        Line line1 = new Line(1, TestUtils.EXAMPLE_LINE_TYPE);
        Line line2 = new Line(1, TestUtils.EXAMPLE_LINE_TYPE);
        assertTrue(line1.equals(line2));
        assertTrue(line2.equals(line1));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void equals_test3() {
        Line line = new Line(1, TestUtils.EXAMPLE_LINE_TYPE);
        LineNumber lineNumber = new LineNumber(1);
        assertFalse(line.equals(lineNumber));
    }

    @Test
    public void equals_test4() {
        Line line = new Line(1, TestUtils.EXAMPLE_LINE_TYPE);
        assertFalse(line.equals(null));
    }

    @Test
    public void equals_test5() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertTrue(line1.equals(line2));
        assertTrue(line2.equals(line1));
    }

    @Test
    public void equals_test6() {
        Line line1 = new Line(1, null);
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertFalse(line1.equals(line2));
        assertFalse(line2.equals(line1));
    }

    @Test
    public void equals_test7() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.AGGLOMERATION));
        assertFalse(line1.equals(line2));
        assertFalse(line2.equals(line1));
    }

    @Test
    public void equals_test8() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        Line line2 = new Line(2, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertFalse(line1.equals(line2));
        assertFalse(line2.equals(line1));
    }

    @Test
    public void hashCode_test1() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertEquals(line1.hashCode(), line2.hashCode());
    }

    @Test
    public void hashCode_test2() {
        Line line1 = new Line(1, null);
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertNotEquals(line1.hashCode(), line2.hashCode());
    }

    @Test
    public void hashCode_test3() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.AGGLOMERATION));
        Line line2 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertNotEquals(line1.hashCode(), line2.hashCode());
    }

    @Test
    public void hashCode_test4() {
        Line line1 = new Line(1, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        Line line2 = new Line(2, new LineInfo(Vehicles.BUS, LineTypes.STANDARD, Areas.CITY));
        assertNotEquals(line1.hashCode(), line2.hashCode());
    }

}
