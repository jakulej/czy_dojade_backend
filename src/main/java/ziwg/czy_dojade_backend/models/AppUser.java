package ziwg.czy_dojade_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@JsonIgnoreProperties({"hashPassword"})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "hash_password")
    private String hashPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "subscriber")
    private boolean subscriber;

    @Column(name = "reports")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Report> reports;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "route_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id"))
    private Set<Route> favouriteRoutes;

}
