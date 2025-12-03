
class ArrivalBeforeDepartureFilter implements FlightFilter {
    @Override
    public boolean shouldExclude(com.gridnine.testing.Flight flight) {
        if (flight == null || flight.getSegments() == null) {
            return false;
        }

        return flight.getSegments().stream()
                .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
    }

    @Override
    public String getDescription() {
        return "Исключить перелёты с сегментами, где дата прилёта раньше даты вылета";
    }
}
