package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.PermissionCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.PermissionResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
