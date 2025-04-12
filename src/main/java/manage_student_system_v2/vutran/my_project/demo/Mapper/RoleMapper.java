package manage_student_system_v2.vutran.my_project.demo.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.RoleCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.PermissionResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Permission;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissionResponseSet", source = "permissionSet", qualifiedByName = "mapPermission")
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissionSet", ignore = true)
    Role toRole(RoleCreationRequest request);

    @Named("mapPermission")
    default Set<PermissionResponse> mapPermisison(Set<Permission> permissionSet) {
        return permissionSet.stream()
                .map(permission -> new PermissionResponse(permission.getName(), permission.getDescription()))
                .collect(Collectors.toSet());
    }
}
