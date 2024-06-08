package ziwg.czy_dojade_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "trip_headsign")
    private String tripHeadsign;

    @Column(name = "direction_id")
    private int directionId;

    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Route route;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Accident accident;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StopTime> stopTimes;

}
