package manage_student_system_v2.vutran.my_project.demo.Repository;

import jakarta.validation.constraints.Pattern;
import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {

    //@Query("SELECT c FROM Certificate c WHERE c.student.studentId = :studentId")
    Certificate findByStudent_StudentId(String studentId);
    boolean existsByNameCertificate(String nameCertificate);
    Optional<Certificate> findByNameCertificate(String nameCertificate);

}
