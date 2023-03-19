package kadai10th.mapper;

import kadai10th.entity.PrefectureEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PrefectureMapper {

    @Select("SELECT * FROM prefectures WHERE prefCode = #{prefCode}")
    Optional<PrefectureEntity> findByCodeFromPrefs(String prefCode);

    @Select("SELECT * FROM prefectures WHERE prefName = #{prefName}")
    Optional<PrefectureEntity> findByNameFromPrefs(String prefName);

    @Select("SELECT * FROM prefectures")
    List<PrefectureEntity> findAllFromPrefs();

    @Insert("INSERT INTO prefectures (prefCode, prefName) VALUES (#{prefCode}, #{prefName})")
    void insertPref(String prefCode, String prefName);

    @Update("UPDATE prefectures SET prefName = #{prefName} WHERE prefCode = #{prefCode}")
    boolean updatePref(String prefCode, String prefName);

    @Delete("DELETE FROM prefectures WHERE prefCode = #{prefCode}")
    boolean deletePref(String prefCode);

}
