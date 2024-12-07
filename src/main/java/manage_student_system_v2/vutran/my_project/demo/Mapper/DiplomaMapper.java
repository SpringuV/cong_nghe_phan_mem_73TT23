package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DiplomaCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Diploma;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiplomaMapper {

    @Mapping(target = "studentSet", ignore = true)
    Diploma toDiploma(DiplomaCreationRequest diplomaCreationRequest);

    DiplomaResponse toDiplomaResponse(Diploma diploma);

}
