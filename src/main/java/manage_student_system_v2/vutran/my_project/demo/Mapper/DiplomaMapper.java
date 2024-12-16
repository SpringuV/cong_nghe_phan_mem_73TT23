package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DiplomaCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Diploma;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DiplomaMapper {

    @Mapping(target = "student", ignore = true)
    Diploma toDiploma(DiplomaCreationRequest diplomaCreationRequest);

    @Mapping(target = "student", source = "student")
    DiplomaResponse toDiplomaResponse(Diploma diploma);

    @Mapping(target = "student", ignore = true)
    void updateDiploma(@MappingTarget Diploma diploma, DiplomaCreationRequest diplomaCreationRequest);
}
