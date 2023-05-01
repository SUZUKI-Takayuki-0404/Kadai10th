package exercise.kadai10th.response;

import exercise.kadai10th.entity.PrefectureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PrefectureResponse {
    private String message;
    private PrefectureEntity prefectureEntity;
}
