package statistic;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventsStatisticImpl implements EventsStatistic {
    private static final long MINUTE_MILLIS = 60 * 1000;
    private static final int HOUR_MINUTES = 60;
    private static final long HOUR_MILLIS = HOUR_MINUTES * MINUTE_MILLIS;

    private final Clock clock;
    private final Map<String, OneEventStatistic> nameEventStatistic = new HashMap<>();

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        nameEventStatistic.putIfAbsent(name, new OneEventStatistic(name));

        OneEventStatistic oneEventStatistic = nameEventStatistic.get(name);

        oneEventStatistic.inc(clock.millis());
    }

    @Override
    public List<Integer> getEventStatisticByName(String name) {
        OneEventStatistic oneEventStatistic = nameEventStatistic.get(name);

        if (oneEventStatistic == null) {
            List<Integer> result = new ArrayList<>();
            for (int minute = 0; minute < HOUR_MINUTES; minute++) {
                result.add(0);
            }

            return result;
        }

        return oneEventStatistic.getStatistic(clock.millis());
    }

    @Override
    public Map<String, List<Integer>> getAllEventStatistic() {
        long timestamp = clock.millis();
        Map<String, List<Integer>> result = new HashMap<>();

        for (OneEventStatistic oneEventStatistic : nameEventStatistic.values()) {
            result.put(oneEventStatistic.getName(), oneEventStatistic.getStatistic(timestamp));
        }

        return result;
    }

    @Override
    public void printStatistic() {
        long timestamp = clock.millis();
        System.out.format("%8s", "Event");
        for (int minute = 0; minute < HOUR_MINUTES; minute++) {
            System.out.format("%3s", minute);
        }
        System.out.println();
        for (OneEventStatistic oneEventStatistic : nameEventStatistic.values()) {
            System.out.format("%8s", oneEventStatistic.getName());
            for (Integer rpm : oneEventStatistic.getStatistic(timestamp)) {
                System.out.format("%3s", rpm);
            }
            System.out.println();
        }
    }

    private static class OneEventStatistic {
        private final String name;
        private final Deque<Long> timestamps = new LinkedList<>();

        public OneEventStatistic(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void inc(long timestamp) {
            timestamps.addLast(timestamp);
            updateTimestamps(timestamp);
        }

        private void updateTimestamps(long timestamp) {
            long hourAgo = timestamp - HOUR_MILLIS;
            while (hourAgo > timestamps.getFirst()) {
                timestamps.pollFirst();
            }
        }

        public List<Integer> getStatistic(long timestamp) {
            updateTimestamps(timestamp);
            List<Integer> resultList = new ArrayList<>(HOUR_MINUTES);

            int minuteAgo = 1;
            int current = 0;
            for (Iterator<Long> it = timestamps.descendingIterator(); it.hasNext(); ) {
                Long time = it.next();
                while (time <= timestamp - minuteAgo * MINUTE_MILLIS) {
                    minuteAgo++;
                    resultList.add(current);
                    current = 0;
                }

                current++;
            }
            resultList.add(current);

            for (int minute = resultList.size(); minute < HOUR_MINUTES; minute++) {
                resultList.add(0);
            }

            return resultList;
        }
    }
}
