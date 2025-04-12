package manage_student_system_v2.vutran.my_project.demo.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentUpdateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentUpdateResponse toStudentResponse(Student student);

    @Mapping(target = "certificateSet", source = "certificateList")
    @Mapping(target = "diplomaSet", source = "diplomaList")
    Student toStudent(StudentCreateRequest studentCreateRequest);

    @Mapping(target = "certificateSet", ignore = true)
    @Mapping(target = "diplomaSet", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequest studentUpdateRequest);

    @Mapping(target = "certificates", source = "certificateSet")
    @Mapping(target = "diplomas", source = "diplomaSet")
    StudentUpdateResponse toStudentUpdateResponse(Student student);
}
