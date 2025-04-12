package manage_student_system_v2.vutran.my_project.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentId(String idStudent);

    boolean existsByStudentId(String id);
}
