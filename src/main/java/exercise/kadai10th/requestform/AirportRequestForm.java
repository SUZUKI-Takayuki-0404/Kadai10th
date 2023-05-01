package exercise.kadai10th.requestform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
//@Getter
@Setter
public class AirportRequestForm {

    @Size(min = 3, max = 3, message = "Number of letters has to be 3")
    @NotBlank(message = "Airport Code is required field")
    private String airportCode;

    @NotBlank(message = "Airport Name is required field")
    private String airportName;

    @Size(min = 2, max = 2, message = "Number of letters has to be 2")
    @NotBlank(message = "Prefecture Code is required field")
    private String prefCode;

}
