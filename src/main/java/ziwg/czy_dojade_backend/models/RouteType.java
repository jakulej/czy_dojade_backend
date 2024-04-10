package ziwg.czy_dojade_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route_type")
public class RouteType {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    @Schema(description = "Nazwa typu linii, rozróżnienie czy autobus czy tramwaj, dzienny czy nocny itp.",
            example = "Normalna autobusowa")
    private String type;

    @OneToMany(mappedBy = "routeType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes;

}
