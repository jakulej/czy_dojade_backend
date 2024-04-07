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
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "curr_latitude")
    private double currLatitude;

    @Column(name = "curr_longitude")
    private double currLongitude;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Trip> trip;

}
