package ziwg.czy_dojade_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedule_stop_time")
public class ScheduleStopTime {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @ManyToOne
    @JoinColumn(name = "stop_id")
    private Stop stop;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trip_id")
    private Trip trip;

}
