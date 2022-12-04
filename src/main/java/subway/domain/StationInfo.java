package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.dto.DistanceDto;
import java.util.List;

public class StationInfo {

    private List<Station> stations;
    private List<Integer> distance;
    private List<Integer> time;

    StationInfo(List<Station> stations, List<Integer> distance, List<Integer> time) {
        this.stations = stations;
        this.distance = distance;
        this.time = time;
    }

    public void setGraphByDistance(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addVertex(graph);
        setWeightByDistance(graph);
    }

    public void setGraphByTime(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addVertex(graph);
        setWeightByTime(graph);
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setWeightByDistance(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (int index = 0; index < distance.size(); index++) {

            Station beforeStation = stations.get(index);
            Station afterStation = stations.get(index +1);
            int distance = this.distance.get(index);

            graph.setEdgeWeight(graph.addEdge(beforeStation, afterStation), distance);
        }
    }

    private void setWeightByTime(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (int index = 0; index < distance.size(); index++) {

            Station beforeStation = stations.get(index);
            Station afterStation = stations.get(index +1);
            int time = this.time.get(index);

            graph.setEdgeWeight(graph.addEdge(beforeStation, afterStation), time);
        }
    }

    public boolean isExistNextTo(Station station1, Station station2) {
        for (int index = 0; index < stations.size()-1; index++) {
            if (isMatched(station1, station2, index)) {
                return true;
            }
        }
        return false;
    }

    public DistanceDto getDistanceDto(Station station1, Station station2) {
        for (int index = 0; index < stations.size()-1; index++) {
            if (isMatched(station1, station2, index)) {
                return new DistanceDto(distance.get(index), time.get(index));
            }
        }
        return null;
    }

    private boolean isMatched(Station station1, Station station2, int index) {
        return stations.get(index).isEqual(station1)
                && stations.get(index+1).isEqual(station2)
                || stations.get(index).isEqual(station2)
                && stations.get(index+1).isEqual(station1);
    }


}