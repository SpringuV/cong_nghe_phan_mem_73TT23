package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentUpdateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toStudentResponse(Student student);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "studentId", source = "studentId")
    Student toStudent(StudentCreateRequest studentCreateRequest);


    @Mapping(target = "certificateSet", ignore = true)
    @Mapping(target = "diplomaSet", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequest studentUpdateRequest);

    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "certificates", source = "certificateSet")
    @Mapping(target = "diplomas", source = "diplomaSet")
    StudentUpdateResponse toStudentUpdateResponse(Student student);
}
