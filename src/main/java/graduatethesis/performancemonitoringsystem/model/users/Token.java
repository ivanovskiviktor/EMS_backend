package graduatethesis.performancemonitoringsystem.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;



import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="u_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column
    @CreationTimestamp
    private OffsetDateTime date_created;

    @ManyToOne
    @JoinColumn(name = "u_user_id")
    private User user;

    @Column
    private OffsetDateTime expiration_date;

    @JsonIgnore
    public Token(User user) {
        this.user = user;
        token = UUID.randomUUID().toString();
        expiration_date =  OffsetDateTime.now().plusMinutes(30);
    }


}
