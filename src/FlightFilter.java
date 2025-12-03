interface FlightFilter {
    boolean shouldExclude(com.gridnine.testing.Flight flight);
    String getDescription();
}