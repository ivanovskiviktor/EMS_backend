package graduatethesis.performancemonitoringsystem.model.organization;


import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatethesis.performancemonitoringsystem.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="o_note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private OffsetDateTime startWeekDate;

    private OffsetDateTime endWeekDate;

    @ManyToOne
    @JoinColumn(name = "u_user_id")
    private User user;

}
