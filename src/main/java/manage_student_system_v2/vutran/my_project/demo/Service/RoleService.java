package manage_student_system_v2.vutran.my_project.demo.Service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.RoleCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Permission;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.RoleMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.PermissionRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse createRole(RoleCreationRequest request) {
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissionSet(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getListRole() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    public RoleResponse updateRole(RoleCreationRequest creationRequest) {
        // check id
        Role role = roleRepository
                .findById(creationRequest.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        role.setDescription(creationRequest.getDescription());

        // set Permission
        List<Permission> permissionList = permissionRepository.findAllById(creationRequest.getPermissions());
        role.setPermissionSet(new HashSet<>(permissionList));
        roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }
}
