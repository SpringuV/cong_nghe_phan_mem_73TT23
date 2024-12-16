package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DepartmentCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DepartmentResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toDepartment(DepartmentCreationRequest departmentCreationRequest);

    DepartmentResponse toDepartmentResponse(Department department);


    void updateDepartment(@MappingTarget Department department, DepartmentCreationRequest departmentCreationRequest);
}
