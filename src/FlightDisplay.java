import java.util.List;

class FlightDisplay {
    public static void displayFlights(String title, List<com.gridnine.testing.Flight> flights) {
        System.out.println("=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60));

        if (flights == null || flights.isEmpty()) {
            System.out.println("Нет перелётов для отображения.");
        } else {
            for (int i = 0; i < flights.size(); i++) {
                com.gridnine.testing.Flight flight = flights.get(i);
                System.out.printf("%d. %s%n", i + 1, flight.toString());
            }
        }
        System.out.printf("Всего перелётов: %d%n", flights != null ? flights.size() : 0);
        System.out.println();
    }

    public static void displayFilterInfo(FlightFilter filter) {
        if (filter != null) {
            System.out.println(">>> " + filter.getDescription());
        }
    }
}