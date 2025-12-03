import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FlightFilterProcessor {
    private final List<FlightFilter> filters;

    public FlightFilterProcessor() {
        this.filters = new ArrayList<>();
    }

    public FlightFilterProcessor addFilter(FlightFilter filter) {
        if (filter != null) {
            filters.add(filter);
        }
        return this;
    }

    public List<com.gridnine.testing.Flight> filter(List<com.gridnine.testing.Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return new ArrayList<>();
        }

        return flights.stream()
                .filter(flight -> filters.stream().noneMatch(filter -> filter.shouldExclude(flight)))
                .collect(Collectors.toList());
    }

    public List<com.gridnine.testing.Flight> filter(List<com.gridnine.testing.Flight> flights, FlightFilter filter) {
        if (flights == null || flights.isEmpty() || filter == null) {
            return flights != null ? new ArrayList<>(flights) : new ArrayList<>();
        }

        return flights.stream()
                .filter(flight -> !filter.shouldExclude(flight))
                .collect(Collectors.toList());
    }

    public void clearFilters() {
        filters.clear();
    }

    public int getFilterCount() {
        return filters.size();
    }
}