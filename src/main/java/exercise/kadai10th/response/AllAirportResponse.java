package exercise.kadai10th.response;

import exercise.kadai10th.entity.AirportEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AllAirportResponse {
    private String message;
    private List<AirportEntity> airportEntityList;
}
