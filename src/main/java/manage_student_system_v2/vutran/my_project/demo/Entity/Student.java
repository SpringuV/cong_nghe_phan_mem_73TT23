package manage_student_system_v2.vutran.my_project.demo.Entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "student")
@Getter
@Builder
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") // muốn giữ lại cấu trúc tuần tự hóa nhưng tránh vòng lặp bằng cách sử dụng định danh
public class Student {

    @Id
    @Column(name = "student_id", unique = true, nullable = false)
    String studentId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "name_department")
    String departmentName;

    @Column(name = "graduation_status")
    String graduationStatus; // trang thai tot nghiep, chua tot nghiep, dang hoc

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "year_admission") // nam nhap hoc
    String yearAdmission;

    @Column(name = "year_graduation") // nam tot nghiep
    String yearGraduation;

    @Column(name = "dob")
    LocalDate dob;

    @Column(name = "update_at")
    LocalDate updateAt;

    @Column(name = "email")
    String email;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Diploma> diplomaSet;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Certificate> certificateSet;
}
