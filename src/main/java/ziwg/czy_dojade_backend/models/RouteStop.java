package ziwg.czy_dojade_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route_stop")
public class RouteStop {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stop_id")
    private Stop stop;

    @Column(name = "currentStopInRoute")
    private long currentStopInRoute;

}
