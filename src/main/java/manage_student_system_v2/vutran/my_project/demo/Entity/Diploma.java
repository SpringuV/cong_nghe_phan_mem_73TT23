package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "diploma_id", unique = true, nullable = false)
    String diplomaId;

    @Column(name = "major", nullable = false)
    String major; // nganh hoc

    @Column(name = "degree_type", nullable = false)
    String degreeType; // loai bang: cu nhan, thac si, ki su, tien si,...

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate; // ngay cap bang

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    @JoinColumn(name = "student_id")
    Student student;
}
