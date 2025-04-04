package manage_student_system_v2.vutran.my_project.demo.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
