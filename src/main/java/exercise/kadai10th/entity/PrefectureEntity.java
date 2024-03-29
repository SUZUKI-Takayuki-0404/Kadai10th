package exercise.kadai10th.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PrefectureEntity {

    private final String prefCode;
    private String prefName;

}
