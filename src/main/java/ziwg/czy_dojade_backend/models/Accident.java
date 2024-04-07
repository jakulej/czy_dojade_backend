package ziwg.czy_dojade_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accident")
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "acc_latitude")
    private double accLatitude;

    @Column(name = "acc_longitude")
    private double accLongitude;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "time_of_accident")
    private LocalDateTime timeOfAccident;

    @OneToMany(mappedBy = "accident", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;

}
