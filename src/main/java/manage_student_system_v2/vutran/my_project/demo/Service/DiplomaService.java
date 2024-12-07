package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DiplomaCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Diploma;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.DiplomaMapper;
import manage_student_system_v2.vutran.my_project.demo.Mapper.StudentMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.DiplomaRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.StudentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiplomaService {

    DiplomaRepository diplomaRepository;
    StudentRepository studentRepository;
    DiplomaMapper diplomaMapper;
    StudentMapper studentMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public DiplomaResponse createDiploma(DiplomaCreationRequest request){
        Set<Student> studentList = request.getStudents().stream().map(
                                        studentID -> studentRepository.findByStudentId(studentID)
                                                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND))
                                ).collect(Collectors.toSet());
        Diploma diploma = diplomaMapper.toDiploma(request);
        diploma.setStudentSet(studentList);

        //save diploma
        try {
            diplomaRepository.save(diploma);
        } catch (Exception e){
            e.printStackTrace();
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return DiplomaResponse.builder()
                .diplomaId(diploma.getDiplomaId())
                .major(diploma.getMajor())
                .degreeType(diploma.getDegreeType())
                .issueDate(diploma.getIssueDate())
                .students(diploma.getStudentSet().stream().map(studentMapper::toStudentResponse).collect(Collectors.toSet()))
                .build();
    }

    public DiplomaResponse getDiplomaByID(String idDiploma){
        Diploma diploma = diplomaRepository.findById(idDiploma).orElseThrow(() -> new AppException(ErrorCode.DIPLOMA_NOT_FOUND));
        return diplomaMapper.toDiplomaResponse(diploma);
    }

    public Set<DiplomaResponse> getListDiplomaByMajor(String majorName){

        if(majorName.isEmpty() || majorName == null){
            throw new IllegalArgumentException("Major name must not be null or empty !");
        }

        return diplomaRepository.findByMajor(majorName).stream().map(diplomaMapper::toDiplomaResponse).collect(Collectors.toSet());
    }

    public String deleteDiploma(String id){
        // check id
        Diploma diploma = diplomaRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DIPLOMA_NOT_FOUND));

        diplomaRepository.deleteById(diploma.getDiplomaId());
        return "Deleted Diploma id: " + id;
    }
}
