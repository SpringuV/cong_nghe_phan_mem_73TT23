package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.PermissionCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.PermissionResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Permission;
import manage_student_system_v2.vutran.my_project.demo.Mapper.PermissionMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.PermissionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse createPermission(PermissionCreationRequest request){
        Permission permission = permissionRepository.save(permissionMapper.toPermission(request));
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> getListPermission(){
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String deletePermission(String id){
        permissionRepository.deleteById(id);
        return "Deleted permission " + id;
    }

}
