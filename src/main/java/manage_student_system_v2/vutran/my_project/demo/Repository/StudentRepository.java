package manage_student_system_v2.vutran.my_project.demo.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentId(String idStudent);

    boolean existsByUsername(String username);

    Optional<Student> findByUsername(String username);

    boolean existsByStudentId(String id);
}
