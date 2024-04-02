package ziwg.czy_dojade_backend.services.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@AllArgsConstructor
public class DataService {

    private final RouteRepository routeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final StopRepository stopRepository;
    private final StopTimeRepository stopTimeRepository;
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final AccidentRepository accidentRepository;
    public String processZip() throws IOException {

        String zipUrl = "https://www.wroclaw.pl/open-data/87b09b32-f076-4475-8ec9-6020ed1f9ac0/OtwartyWroclaw_rozklad_jazdy_GTFS.zip";

        try {
            // Download the ZIP file
            RestTemplate restTemplate = new RestTemplate();
            byte[] zipData = restTemplate.getForObject(zipUrl, byte[].class);

            // Unpack the ZIP file/
            unzip(zipData);

            String resourcesDirectory = getClass().getResource("/").getPath();
            String extractPath = resourcesDirectory + "GTFS";

            // Read the text files
            readTextFiles(extractPath);

            return "Files processed successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing files.";
        }
    }

    private void unzip(byte[] zipData) throws IOException {
        String resourcesDirectory = getClass().getResource("/").getPath();
        String extractPath = resourcesDirectory + "GTFS";

        File extractDirectory = new File(extractPath);
        if (!extractDirectory.exists()) {
            extractDirectory.mkdirs();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String filePath = extractPath + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // Create parent directories if not exists
                    new File(filePath).getParentFile().mkdirs();
                    // Write the file
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                    }
                } else {
                    // Create directory
                    new File(filePath).mkdirs();
                }
            }
        }
    }

    private void readTextFiles(String directoryPath) {
        File directory = new File(directoryPath);
        String[] fileNames = {"stops.txt", "route_types.txt", "routes.txt", "trips.txt", "stop_times.txt"};
        //File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        File[] files = directory.listFiles((dir, name) -> {
            for (String fileName : fileNames) {
                if (name.equalsIgnoreCase(fileName)) {
                    return true;
                }
            }
            return false;
        });
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(",");
                        switch (file.getName()) {
                            case "stops.txt":
                                Stop stop = new Stop(Long.parseLong(values[0]), values[2],
                                        values[3], Double.parseDouble(values[4]), Double.parseDouble(values[5]),
                                        stopTimeRepository.findByStopId(Long.parseLong(values[0])));
                                stopRepository.save(stop);
                                break;
                            case "route_types.txt":
                                RouteType routeType = new RouteType(Long.parseLong(values[0]), values[1],
                                        routeRepository.findByRouteTypeId(Long.parseLong(values[0])));
                                routeTypeRepository.save(routeType);
                                break;
                            case "routes.txt":
                                Optional<RouteType> optionalRouteType = routeTypeRepository.findById(Long.valueOf(values[6]));
                                if(optionalRouteType.isPresent()){
                                    Route route = new Route(Long.parseLong(values[0]),
                                            values[2], values[3], values[4],
                                            tripRepository.findByRouteId(Long.parseLong(values[0])),
                                            optionalRouteType.get());
                                    routeRepository.save(route);
                                }
                                break;
                            case "trips.txt":
                                Optional<Route> optionalRoute = routeRepository.findById(Long.valueOf(values[0]));
                                Optional<Vehicle> optionalVehicle = vehicleRepository.findById(Long.valueOf(values[7]));
                                if(optionalVehicle.isPresent() && optionalRoute.isPresent()){
                                    Trip trip = new Trip(Long.parseLong(values[2]), values[3], Integer.parseInt(values[4]),
                                            optionalRoute.get(), optionalVehicle.get(),
                                            accidentRepository.findByTripId(Long.parseLong(values[2])),
                                            stopTimeRepository.findByTripId(Long.parseLong(values[2])));
                                    tripRepository.save(trip);
                                }

                                break;
                            case "stop_times.txt":
                                Optional<Stop> optionalStop = stopRepository.findById(Long.valueOf(values[3]));
                                Optional<Trip> optionalTrip = tripRepository.findById(Long.valueOf(values[0]));
                                if(optionalStop.isPresent() && optionalTrip.isPresent()){
                                    StopTime stopTime = new StopTime();
                                    stopTime.setArrivalTime(LocalDateTime.parse(values[1]));
                                    stopTime.setDepartureTime(LocalDateTime.parse(values[2]));
                                    stopTime.setStop(optionalStop.get());
                                    stopTime.setTrip(optionalTrip.get());
                                    stopTimeRepository.save(stopTime);
                                }
                                break;
                            default:
                                // Handle unknown file
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
