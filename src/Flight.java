import java.util.List;
import java.util.stream.Collectors;

class Flight {
    private final List<com.gridnine.testing.Segment> segments;

    Flight(final List<com.gridnine.testing.Segment> segs) {
        segments = segs;
    }

    List<com.gridnine.testing.Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}