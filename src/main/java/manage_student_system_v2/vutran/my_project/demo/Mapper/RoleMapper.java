package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.RoleCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissionResponseSet", source = "permissionSet")
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissionSet", ignore = true)
    Role toRole(RoleCreationRequest request);

}
