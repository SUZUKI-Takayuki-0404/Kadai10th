package exercise.kadai10th.response;

import exercise.kadai10th.entity.PrefectureEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PrefectureResponse {
    private String message;
    private PrefectureEntity prefectureEntity;
}
