import java.time.LocalDateTime;

class DepartureBeforeNowFilter implements FlightFilter {
    @Override
    public boolean shouldExclude(com.gridnine.testing.Flight flight) {
        if (flight == null || flight.getSegments() == null || flight.getSegments().isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return flight.getSegments().stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(now));
    }

    @Override
    public String getDescription() {
        return "Исключить перелёты с вылетом до текущего момента времени";
    }
}
