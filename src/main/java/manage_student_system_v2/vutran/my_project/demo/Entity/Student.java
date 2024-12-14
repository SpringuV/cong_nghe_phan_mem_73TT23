package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Student extends User{

    @Column(name = "student_id", unique = true, nullable = false)
    String studentId;

    @Column(name = "graduation_status")
    String graduationStatus; // trang thai tot nghiep, chua tot nghiep, dang hoc

    @Column(name = "class_name")
    String className;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "update_at")
    LocalDate updateAt;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    Set<Department> departmentSet;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    Set<Diploma> diplomaSet;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    Set<Certificate> certificateSet;
}
