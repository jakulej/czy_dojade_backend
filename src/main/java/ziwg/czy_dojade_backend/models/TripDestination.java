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
@Table(name = "trip_destination")
public class TripDestination {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "trip_headsign")
    private String tripHeadsign;


    @OneToMany(mappedBy = "tripDestination")
    private List<Trip> trip;
}
