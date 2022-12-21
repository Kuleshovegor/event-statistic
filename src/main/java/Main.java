import statistic.EventsStatistic;
import statistic.EventsStatisticImpl;

import java.time.Clock;

public class Main {
    public static void main(String[] args) {
        Clock clock = Clock.systemUTC();
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        eventsStatistic.incEvent("receive");
        eventsStatistic.incEvent("send");
        eventsStatistic.incEvent("send");
        eventsStatistic.printStatistic();
    }
}
