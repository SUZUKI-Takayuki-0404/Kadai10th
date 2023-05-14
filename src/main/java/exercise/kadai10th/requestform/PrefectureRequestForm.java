package exercise.kadai10th.requestform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PrefectureRequestForm {

    @Size(min = 2, max = 2, message = "Number of letters has to be 2")
    @NotBlank(message = "Prefecture Code is required field")
    private String prefCode;

    @Size(max = 8, message = "Number of letters has to be 8 or less")
    @NotBlank(message = "Prefecture Name is required field")
    private String prefName;

}
