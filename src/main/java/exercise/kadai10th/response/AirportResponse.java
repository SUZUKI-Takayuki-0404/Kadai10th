package exercise.kadai10th.response;

import exercise.kadai10th.entity.AirportEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AirportResponse {
    private String message;
    private AirportEntity airportEntity;
}
