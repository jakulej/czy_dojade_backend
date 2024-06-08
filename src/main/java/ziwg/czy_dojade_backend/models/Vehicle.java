package ziwg.czy_dojade_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"trip"})
public class Vehicle {

//    @Id
//    //@GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private long id;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "curr_latitude")
    private double currLatitude;

    @Column(name = "curr_longitude")
    private double currLongitude;

    @Column(name = "delay")
    private Long delay;

    @Column(name = "type")
    private Long type;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trip> trip;

}
