package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DepartmentCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DepartmentResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Department;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.DepartmentMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.DepartmentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {

    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentResponse createDepartment(DepartmentCreationRequest request){
        return departmentMapper.toDepartmentResponse(departmentRepository.save(departmentMapper.toDepartment(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Set<DepartmentResponse> getListDepartment(){
        return departmentRepository.findAll().stream().map(departmentMapper::toDepartmentResponse).collect(Collectors.toSet());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDepartment(String idName){
        Department department = departmentRepository.findById(idName).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        departmentRepository.deleteById(idName);
        return "Deleted Department: " + department.getNameDepartment();
    }
}
