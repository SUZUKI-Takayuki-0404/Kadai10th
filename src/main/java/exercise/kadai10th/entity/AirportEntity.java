package exercise.kadai10th.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AirportEntity {

    private final String airportCode;
    private String airportName;
    private String prefCode;
    private String prefName;

}
