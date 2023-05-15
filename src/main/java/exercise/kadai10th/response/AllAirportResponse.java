package exercise.kadai10th.response;

import exercise.kadai10th.entity.AirportEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class AllAirportResponse {
    private String message;
    private List<AirportEntity> airportEntityList;
}
