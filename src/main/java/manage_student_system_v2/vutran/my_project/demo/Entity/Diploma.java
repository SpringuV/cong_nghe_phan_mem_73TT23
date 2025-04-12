package manage_student_system_v2.vutran.my_project.demo.Entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") // muốn giữ lại cấu trúc tuần tự hóa nhưng tránh vòng lặp bằng cách sử dụng định danh
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
    String issueDate; // nam cap bang

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    @JoinColumn(name = "student_id")
    Student student;
}
