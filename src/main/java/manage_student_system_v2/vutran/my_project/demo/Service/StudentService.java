package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Constant.PredefinedRole;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.StudentMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StudentService {

    StudentRepository studentRepository;
    RoleRepository roleRepository;
    StudentMapper studentMapper;
    PasswordEncoder passwordEncoder;

    public StudentResponse createStudent(StudentCreateRequest createRequest){

        if(studentRepository.existsByUsername(createRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Student student = studentMapper.toStudent(createRequest);

        // set role
        Set<Role> roleSet= new HashSet<>();
        roleRepository.findById(PredefinedRole.STUDENT_ROLE).ifPresent(roleSet::add);
        student.setRoles(roleSet);

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

}
