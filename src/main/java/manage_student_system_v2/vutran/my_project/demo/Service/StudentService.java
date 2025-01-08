package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.*;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentUpdateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.*;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.CertificateMapper;
import manage_student_system_v2.vutran.my_project.demo.Mapper.DiplomaMapper;
import manage_student_system_v2.vutran.my_project.demo.Mapper.StudentMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    StudentRepository studentRepository;
    RoleRepository roleRepository;
    StudentMapper studentMapper;
    PasswordEncoder passwordEncoder;
    DiplomaRepository diplomaRepository;
    CertificateRepository certificateRepository;
    CertificateMapper certificateMapper;
    DiplomaMapper diplomaMapper;

    public StudentUpdateResponse createStudent(StudentCreateRequest createRequest){

        log.info("Student: {}", createRequest);
        if(studentRepository.existsByStudentId(createRequest.getStudentId())){
            throw new AppException(ErrorCode.STUDENT_EXISTED);
        }

        Student student = studentMapper.toStudent(createRequest);
        student.setStudentId(createRequest.getStudentId());
        student.setCreatedAt(LocalDate.now());
        student.setGraduationStatus(createRequest.getGraduationStatus());

        // set Diploma
        AtomicBoolean checkDiploma = new AtomicBoolean(false);
        Set<Diploma> diplomaSet = new HashSet<>();
        createRequest.getDiplomaList().forEach(
                diplomaRequest -> {
                    // check diploma existed
                    if(!diplomaRepository.existsByDegreeTypeAndMajorAndStudent_StudentId(diplomaRequest.getDegreeType(), diplomaRequest.getMajor(), createRequest.getStudentId())){
                        Diploma diploma = diplomaMapper.toDiplomaFromStudentRequest(diplomaRequest);
                        // save diploma
                        diploma.setStudent(student);
                        //save
                        diplomaRepository.save(diploma);
                        //add set
                        diplomaSet.add(diploma);
                        checkDiploma.set(true);
                    }
                }
        );
        // nếu có diploma mới thì mới cập nhật
        if(checkDiploma.get()){
            student.setDiplomaSet(diplomaSet);
        }

        // set Certificate
        List<CertificateCreateInStudentRequest> certificates = createRequest.getCertificateList();
        Set<Certificate>  certificateSet = certificates
                .stream()
                .map(request -> {
                    Certificate certificate = certificateMapper.toCertificateFromStudentRequest(request);
                    certificate.setStudent(student);
                    return certificateRepository.save(certificate); // luu tung chung chi
                }).collect(Collectors.toSet());
        student.setCertificateSet(certificateSet);
        student.setUpdateAt(LocalDate.now());
        try{
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return studentMapper.toStudentResponse(student);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentUpdateResponse> getListStudent(){
        return studentRepository.findAll().stream().map(studentMapper::toStudentUpdateResponse).toList();
    }

    public StudentUpdateResponse updateStudent(String id, StudentUpdateRequest studentUpdateRequest){
        System.out.println("dang trong ham update student");
        // check id
        Student student = studentRepository.findByStudentId(id).orElseThrow(()->new AppException(ErrorCode.STUDENT_NOT_FOUND));
        studentMapper.updateStudent(student, studentUpdateRequest);
        Student finalStudent = student;
        // set Diploma
        AtomicBoolean checkDiploma = new AtomicBoolean(false);
        Set<Diploma> diplomaSet = new HashSet<>();
        studentUpdateRequest.getDiplomaList().forEach(
            diplomaRequest -> {
                // check diploma existed
                if(!diplomaRepository.existsByDegreeTypeAndMajorAndStudent_StudentId(diplomaRequest.getDegreeType(), diplomaRequest.getMajor(), id)){
                    Diploma diploma = diplomaMapper.toDiplomaFromStudentRequest(diplomaRequest);
                    // save diploma
                    diploma.setStudent(finalStudent);
                    //save
                    diplomaRepository.save(diploma);
                    //add set
                    diplomaSet.add(diploma);
                    checkDiploma.set(true);
                }
            }
        );
        // nếu có diploma mới thì mới cập nhật
        if(checkDiploma.get()){
            student.setDiplomaSet(diplomaSet);
        }

        // set Certificate
        List<CertificateCreateInStudentRequest> certificates = studentUpdateRequest.getCertificateList();
        Set<Certificate>  certificateSet = certificates
                .stream()
                .map(request -> {
                    Certificate certificate = certificateMapper.toCertificateFromStudentRequest(request);
                    certificate.setStudent(finalStudent);
                    return certificateRepository.save(certificate); // luu tung chung chi
                }).collect(Collectors.toSet());
        student.setCertificateSet(certificateSet);
        student.setUpdateAt(LocalDate.now());
        //save
        student = studentRepository.save(student);

        return studentMapper.toStudentUpdateResponse(student);
    }

    public StudentUpdateResponse getStudent(String id){
        StudentUpdateResponse studentUpdateResponse = new StudentUpdateResponse();
        studentUpdateResponse = studentMapper.toStudentUpdateResponse(studentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND)));
        for(CertificateResponse certificateResponse : studentUpdateResponse.getCertificates()){
            certificateResponse.setStudentId(id);
        }
        for(DiplomaResponse diplomaResponse : studentUpdateResponse.getDiplomas()){
            diplomaResponse.setStudentId(id);
        }
        return  studentUpdateResponse;
    }

    public String deleteStudent(StudentDeleteRequest deleteRequest){
        // check id
        Student student = studentRepository.findByStudentId(deleteRequest.getStudentId()).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        studentRepository.delete(student);
        return "Deleted Student Id: " + deleteRequest.getStudentId();
    }

}
