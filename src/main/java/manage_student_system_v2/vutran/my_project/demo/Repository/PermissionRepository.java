package manage_student_system_v2.vutran.my_project.demo.Repository;

import manage_student_system_v2.vutran.my_project.demo.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
