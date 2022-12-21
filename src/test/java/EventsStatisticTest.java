import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import statistic.EventsStatistic;
import statistic.EventsStatisticImpl;

import java.time.Clock;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventsStatisticTest {
    private static final int HOUR_MINUTES = 60;
    private static final int MINUTE_SECONDS = 60;
    private static final long SECOND_MILLIS = 1000L;
    private final Clock clock = mock(Clock.class);

    @Test
    public void eventTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        eventsStatistic.incEvent("test");

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        Assertions.assertEquals(1, list.get(0));

        for (int i = 1; i < HOUR_MINUTES; i++) {
            Assertions.assertEquals(0, list.get(i));
        }
    }

    @Test
    public void eventMinuteTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);
        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(MINUTE_SECONDS * SECOND_MILLIS);

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        Assertions.assertEquals(0, list.get(0));
        Assertions.assertEquals(1, list.get(1));

        for (int i = 2; i < HOUR_MINUTES; i++) {
            Assertions.assertEquals(0, list.get(i));
        }
    }

    @Test
    public void eventHourTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);
        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(HOUR_MINUTES * MINUTE_SECONDS * SECOND_MILLIS);

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        for (int i = 0; i < HOUR_MINUTES; i++) {
            Assertions.assertEquals(0, list.get(i));
        }
    }

    @Test
    public void emptyEventTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        for (int i = 0; i < HOUR_MINUTES; i++) {
            Assertions.assertEquals(0, list.get(i));
        }
    }

    @Test
    public void twoSameEventTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        eventsStatistic.incEvent("test");
        eventsStatistic.incEvent("test");

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        Assertions.assertEquals(2, list.get(0));

        for (int i = 1; i < HOUR_MINUTES; i++) {
            Assertions.assertEquals(0, list.get(i));
        }
    }

    @Test
    public void twoDiffEventTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(1L);
        eventsStatistic.incEvent("test1");

        Map<String, List<Integer>> map = eventsStatistic.getAllEventStatistic();

        Assertions.assertEquals(1, map.get("test").get(0));
        Assertions.assertEquals(1, map.get("test1").get(0));
    }

    @Test
    public void twoMinuteEventTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(2 * MINUTE_SECONDS * SECOND_MILLIS);
        eventsStatistic.incEvent("test");

        when(clock.millis()).thenReturn(3 * MINUTE_SECONDS * SECOND_MILLIS);

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        Assertions.assertEquals(1, list.get(1));
        Assertions.assertEquals(1, list.get(3));
    }

    @Test
    public void eventMillisTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(132414L);
        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(1312414L);

        List<Integer> list = eventsStatistic.getEventStatisticByName("test");

        Assertions.assertEquals(1, list.get(19));
    }

    @Test
    public void printTest() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);

        when(clock.millis()).thenReturn(0L);

        eventsStatistic.incEvent("test");
        when(clock.millis()).thenReturn(1L);
        eventsStatistic.incEvent("test1");

        eventsStatistic.printStatistic();
    }
}
