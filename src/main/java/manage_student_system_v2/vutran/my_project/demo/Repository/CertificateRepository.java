package manage_student_system_v2.vutran.my_project.demo.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    boolean existsByNameCertificate(String nameCertificate);
    Optional<Certificate> findByNameCertificate(String nameCertificate);
}
