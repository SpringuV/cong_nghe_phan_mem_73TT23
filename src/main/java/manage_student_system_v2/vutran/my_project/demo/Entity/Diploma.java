package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "diploma")
public class Diploma {

    @Id
    @Column(name = "diploma_id")
    String id;
    @Column(name = "major")
    String major; // nganh hoc
    @Column(name = "degree_type")
    String degreeType; // loai bang: cu nhan, thac si, ki su, tien si,...
    @Column(name = "issue_date")
    LocalDate issueDate; // ngay cap bang

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    @JoinTable(name = "diplomas_students", joinColumns = @JoinColumn(name = "diploma_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    Set<Student> studentSet;
}