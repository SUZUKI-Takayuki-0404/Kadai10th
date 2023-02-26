package kadai10th.mapper;

import kadai10th.entity.PrefectureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrefectureMapper {

    @Select("SELECT * FROM prefectures")
    List<PrefectureEntity> findAllFromPrefs();

}
