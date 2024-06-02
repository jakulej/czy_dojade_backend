package ziwg.czy_dojade_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stop_time")
public class StopTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "arrival_delay")
    private LocalTime arrivalDelay;

    @Column(name = "departure_delay")
    private LocalTime departureDelay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_stoptime_id")
    @JsonIgnore
    private ScheduleStopTime scheduleStopTime;

}








