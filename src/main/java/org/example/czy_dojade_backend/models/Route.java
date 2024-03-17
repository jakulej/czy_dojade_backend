package org.example.czy_dojade_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private RouteType routeType;

    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trip> trips;

    // @JoinTable with route user still left

}
