package manage_student_system_v2.vutran.my_project.demo.Repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Diploma;

@Repository
public interface DiplomaRepository extends JpaRepository<Diploma, String> {

    Set<Diploma> findByMajor(String majorName);

    Set<Diploma> findByStudent_StudentId(
            String idStudent); // findByStudent_Id: Sử dụng thuộc tính id trong thực thể Student để truy vấn.

    boolean existsByDegreeTypeAndMajorAndStudent_StudentId(String degreeType, String major, String studentId);

    Optional<Diploma> findByMajorAndStudent_StudentId(String major, String studentId);
}
