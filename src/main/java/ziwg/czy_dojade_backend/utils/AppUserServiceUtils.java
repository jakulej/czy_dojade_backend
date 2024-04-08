package ziwg.czy_dojade_backend.utils;

import ziwg.czy_dojade_backend.models.Accident;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;

public class AppUserServiceUtils {

    public static Trip findNearestTrip(List<Trip> trips, double accidentLatitude, double accidentLongitude){
        Trip nearestTrip = null;
        double minDistance = Double.MAX_VALUE;

        for(Trip trip : trips){
            double distance = calculateDistance(
                    trip.getVehicle().getCurrLatitude(),
                    trip.getVehicle().getCurrLongitude(),
                    accidentLatitude,
                    accidentLongitude
            );
            if(distance < minDistance){
                minDistance = distance;
                nearestTrip = trip;
            }
        }
        return nearestTrip;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    public static Accident findNearestAccident(List<Accident> accidents, double accidentLatitude, double accidentLongitude){
        Accident nearestAccident = null;
        double minDistance = Double.MAX_VALUE;

        for(Accident accident : accidents){
            double distance = calculateDistance(
                    accident.getAccLatitude(),
                    accident.getAccLongitude(),
                    accidentLatitude,
                    accidentLongitude
            );
            if(distance < minDistance){
                minDistance = distance;
                nearestAccident = accident;
            }
        }
        return nearestAccident;
    }

}
