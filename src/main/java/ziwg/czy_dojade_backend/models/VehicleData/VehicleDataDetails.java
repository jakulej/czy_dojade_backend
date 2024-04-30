package ziwg.czy_dojade_backend.models.VehicleData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDataDetails {
    private String id;
    private String type;
    @JsonProperty("vehicleNo")
    private String vehicleNumber;
    @JsonProperty("route_id")
    private String routeId;
    private long timestamp;
    private double lat;
    private double lon;
    private int delay;
    @JsonProperty("currentStatus")
    private String currentStatus;
    private String symbol;
    @JsonProperty("stop_id")
    private String stopId;
    @JsonProperty("stop_name")
    private String stopName;
    @JsonProperty("trip_headsign")
    private String tripHeadsign;
    @JsonProperty("trip_id")
    private String tripId;
    private String brigade;
    @JsonProperty("prevLon")
    private double prevLon;
    @JsonProperty("prevLat")
    private double prevLat;
    private int angle;

    // Getters and setters
}
