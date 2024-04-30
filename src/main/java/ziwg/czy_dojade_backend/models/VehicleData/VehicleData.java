package ziwg.czy_dojade_backend.models.VehicleData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleData {
    @JsonProperty("0/0")
    private VehicleDataDetails details;

    // Getter and setter
    public VehicleDataDetails getDetails() {
        return details;
    }

    public void setDetails(VehicleDataDetails details) {
        this.details = details;
    }
}

