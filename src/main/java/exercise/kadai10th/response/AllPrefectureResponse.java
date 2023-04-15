package exercise.kadai10th.response;

import exercise.kadai10th.entity.PrefectureEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AllPrefectureResponse {
    private String message;
    private List<PrefectureEntity> prefectureEntityList;
}
