package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Constant.PredefinedRole;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentDeleteRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentUpdateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.*;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.DiplomaMapper;
import manage_student_system_v2.vutran.my_project.demo.Mapper.StudentMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    StudentRepository studentRepository;
    RoleRepository roleRepository;
    StudentMapper studentMapper;
    PasswordEncoder passwordEncoder;
    DiplomaRepository diplomaRepository;
    CertificateRepository certificateRepository;
    DiplomaMapper diplomaMapper;

    public StudentResponse createStudent(StudentCreateRequest createRequest){

        if(studentRepository.existsByUsername(createRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Student student = studentMapper.toStudent(createRequest);

        // set role
        Set<Role> roleSet= new HashSet<>();
        roleRepository.findById(PredefinedRole.STUDENT_ROLE).ifPresent(roleSet::add);
        student.setRoles(roleSet);

        // set Create at
        student.setCreatedAt(LocalDate.now());
        // set graduationStatus
        student.setGraduationStatus(createRequest.getGraduationStatus());

        //set password
        student.setPassword(passwordEncoder.encode(createRequest.getPassword()));

        try{
            studentRepository.save(student);
        } catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return studentMapper.toStudentResponse(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentResponse> getListStudent(){
        return studentRepository.findAll().stream().map(studentMapper::toStudentResponse).toList();
    }

    public StudentResponse getInfoStudent(){
        // sau khi user login success sẽ được lưu thông tin đăng nhập tại security context holder
        SecurityContext context = SecurityContextHolder.getContext();
        String name =context.getAuthentication().getName();
        Student student = studentRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXIST));
        return studentMapper.toStudentResponse(student);
    }

//    @PreAuthorize("#studentUpdateRequest.studentId == principal.username || hasRole('ADMIN')") // được phép khi là chủ tài khoản hoặc admin
    public StudentUpdateResponse updateStudent(StudentUpdateRequest studentUpdateRequest){
        // check id
        Student student = studentRepository.findByStudentId(studentUpdateRequest.getStudentId()).orElseThrow(()->new AppException(ErrorCode.STUDENT_NOT_FOUND));
        studentMapper.updateStudent(student, studentUpdateRequest);
        // set Diploma
        AtomicBoolean checkDiploma = new AtomicBoolean(false);
        Set<Diploma> diplomaSet = new HashSet<>();
        studentUpdateRequest.getDiplomaCreate().forEach(
            diplomaRequest -> {
                // check diploma existed
                if(!diplomaRepository.existsByDegreeTypeAndMajorAndStudent_StudentId(diplomaRequest.getDegreeType(), diplomaRequest.getMajor(), diplomaRequest.getStudentId())){
                    Student studentCheck = studentRepository.findByStudentId(diplomaRequest.getStudentId()).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
                    Diploma diploma = diplomaMapper.toDiploma(diplomaRequest);
                    // save diploma
                    diploma.setStudent(studentCheck);
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
        List<String> certificates = studentUpdateRequest.getCertificateName();
        Set<Certificate>  certificateSet = certificates
                .stream()
                .map(certificateName -> certificateRepository.findByNameCertificate(certificateName).orElseThrow(() -> new AppException(ErrorCode.CERTIFICATE_NOT_FOUND))).collect(Collectors.toSet());
        student.setCertificateSet(certificateSet);
        student.setUpdateAt(LocalDate.now());
        //save
        student = studentRepository.save(student);

        return studentMapper.toStudentUpdateResponse(student);
    }

    public String deleteStudent(StudentDeleteRequest deleteRequest){
        // check id
        Student student = studentRepository.findByStudentId(deleteRequest.getStudentId()).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        studentRepository.delete(student);
        return "Deleted Student Id: " + deleteRequest.getStudentId();
    }

}
