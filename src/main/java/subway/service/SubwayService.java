package subway.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.SectionDto;
import subway.dto.ResultDto;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayService {

    private static final WeightedMultigraph<Station, DefaultWeightedEdge> distanceGraph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> timeGraph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    static {
        initializeGraphs();
    }

    private static void initializeGraphs() {
        List<Line> lines = LineRepository.lines();
        for (Line line : lines) {
            line.setGraphByDistance(distanceGraph);
            line.setGraphByTime(timeGraph);
        }
    }

    public static ResultDto getPathByDistance(String startStationName, String endStationName) {
        Station startStation = StationRepository.getStation(startStationName);
        Station endStation = StationRepository.getStation(endStationName);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(distanceGraph);
        List<Station> path = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();

        return pathToResultDto(path);
    }

    public static ResultDto getPathByTime(String startStationName, String endStationName) {
        Station startStation = StationRepository.getStation(startStationName);
        Station endStation = StationRepository.getStation(endStationName);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(timeGraph);
        List<Station> path = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();

        return pathToResultDto(path);
    }

    private static ResultDto pathToResultDto(List<Station> path) {
        int totalDistance = 0;
        int totalTime = 0;
        for (int index = 0; index < path.size() - 1; index++) {
            SectionDto section = LineRepository.getSectionDto(path.get(index), path.get(index+1));
            totalDistance += section.getDistance();
            totalTime += section.getTime();
        }
        List<String> names = path.stream().map(Station::getName).collect(Collectors.toList());
        return new ResultDto(names, totalDistance, totalTime);
    }


}