package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "student")
@Getter
@SuperBuilder
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // muốn giữ lại cấu trúc tuần tự hóa nhưng tránh vòng lặp bằng cách sử dụng định danh
public class Student extends User{

    @Column(name = "student_id", unique = true, nullable = false)
    String studentId;

    @Column(name = "graduation_status")
    String graduationStatus; // trang thai tot nghiep, chua tot nghiep, dang hoc

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "update_at")
    LocalDate updateAt;

    @Column(name = "certificate_type", nullable = false)
    String certificateType;

    @Column(name = "issue_date")
    LocalDate issueDateCertificate; // ngay cap

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    Set<Diploma> diplomaSet;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    Set<Certificate> certificateSet;
}
