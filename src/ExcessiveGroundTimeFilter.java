import java.time.LocalDateTime;
import java.util.List;

class ExcessiveGroundTimeFilter implements FlightFilter {
    private static final long MAX_GROUND_TIME_HOURS = 2;

    @Override
    public boolean shouldExclude(com.gridnine.testing.Flight flight) {
        if (flight == null) {
            return false;
        }

        List<com.gridnine.testing.Segment> segments = flight.getSegments();
        if (segments == null || segments.size() <= 1) {
            return false;
        }

        long totalGroundTimeMinutes = 0;

        for (int i = 0; i < segments.size() - 1; i++) {
            com.gridnine.testing.Segment currentSegment = segments.get(i);
            com.gridnine.testing.Segment nextSegment = segments.get(i + 1);

            LocalDateTime currentArrival = currentSegment.getArrivalDate();
            LocalDateTime nextDeparture = nextSegment.getDepartureDate();

            if (nextDeparture.isAfter(currentArrival)) {
                totalGroundTimeMinutes += java.time.Duration.between(currentArrival, nextDeparture).toMinutes();
            }
        }

        return totalGroundTimeMinutes > MAX_GROUND_TIME_HOURS * 60;
    }

    @Override
    public String getDescription() {
        return "Исключить перелёты, где общее время, проведённое на земле, превышает два часа";
    }
}