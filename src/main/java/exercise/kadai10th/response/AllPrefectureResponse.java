package exercise.kadai10th.response;

import exercise.kadai10th.entity.PrefectureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
//@Setter
@Getter
public class AllPrefectureResponse {
    private String message;
    private List<PrefectureEntity> prefectureEntityList;
}
