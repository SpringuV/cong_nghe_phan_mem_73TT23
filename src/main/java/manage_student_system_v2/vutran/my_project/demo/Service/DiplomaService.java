package manage_student_system_v2.vutran.my_project.demo.Service;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class DiplomaService {

    private static final Logger log = LoggerFactory.getLogger(DiplomaService.class);
    DiplomaRepository diplomaRepository;
    StudentRepository studentRepository;
    DiplomaMapper diplomaMapper;
    StudentMapper studentMapper;

    public DiplomaResponse createDiploma(DiplomaCreationRequest request) {
        // check diploma exist
        boolean existDiploma = diplomaRepository.existsByDegreeTypeAndMajorAndStudent_StudentId(
                request.getDegreeType(), request.getMajor(), request.getStudentId());
        if (existDiploma) {
            throw new AppException(ErrorCode.DIPLOMA_EXISTED);
        }

        Diploma diploma = diplomaMapper.toDiploma(request);
        Student student = studentRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        diploma.setStudent(student);
        // save diploma
        try {
            diplomaRepository.save(diploma);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return diplomaMapper.toDiplomaResponse(diploma);
    }

    public Set<DiplomaResponse> getListDiploma() {
        return diplomaRepository.findAll().stream()
                .map(diplomaMapper::toDiplomaResponse)
                .collect(Collectors.toSet());
    }

    public DiplomaResponse getDiplomaByID(String idDiploma) {
        Diploma diploma =
                diplomaRepository.findById(idDiploma).orElseThrow(() -> new AppException(ErrorCode.DIPLOMA_NOT_FOUND));
        return diplomaMapper.toDiplomaResponse(diploma);
    }

    public Set<DiplomaResponse> getListDiplomaByStudentId(String idStudent) {
        return diplomaRepository.findByStudent_StudentId(idStudent).stream()
                .map(diplomaMapper::toDiplomaResponse)
                .collect(Collectors.toSet());
    }

    public Set<DiplomaResponse> getListDiplomaByMajor(String majorName) {

        if (majorName.isEmpty() || majorName == null) {
            throw new IllegalArgumentException("Major name must not be null or empty !");
        }

        return diplomaRepository.findByMajor(majorName).stream()
                .map(diplomaMapper::toDiplomaResponse)
                .collect(Collectors.toSet());
    }

    public String deleteDiploma(String id) {
        // check id
        Diploma diploma =
                diplomaRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DIPLOMA_NOT_FOUND));

        diplomaRepository.deleteById(diploma.getDiplomaId());
        return "Deleted Diploma id: " + id;
    }

    public DiplomaResponse updateDiploma(String id, DiplomaCreationRequest creationRequest) {
        // check diploma
        Diploma diploma =
                diplomaRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DIPLOMA_NOT_FOUND));
        diplomaMapper.updateDiploma(diploma, creationRequest);
        // set student
        diploma.setStudent(studentRepository
                .findByStudentId(creationRequest.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND)));
        // save
        diplomaRepository.save(diploma);
        return diplomaMapper.toDiplomaResponse(diploma);
    }
}
