package ziwg.czy_dojade_backend.services.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
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
            importRouteTypes(extractPath);
            importStops(extractPath);
            importRoutes(extractPath);
            importTrips(extractPath);
            importStopTimes(extractPath);

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

    private void importStops(String directoryPath){
        String filePath = directoryPath + File.separator + "stops.txt";
        File file = new File(filePath);

        List<Stop> stopList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Stop stop = new Stop(Long.parseLong(values[0]), values[1],
                        values[2], Double.parseDouble(values[3]), Double.parseDouble(values[4]),
                        stopTimeRepository.findByStopId(Long.parseLong(values[0])));

                        stopList.add(stop);
            }
            stopRepository.saveAll(stopList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importRouteTypes(String directoryPath){
        String filePath = directoryPath + File.separator + "route_types.txt";
        File file = new File(filePath);

        List<RouteType> routeTypesList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                RouteType routeType = new RouteType(Long.parseLong(values[0]), values[1],
                        routeRepository.findByRouteTypeId(Long.parseLong(values[0])));

                routeTypesList.add(routeType);
            }
            routeTypeRepository.saveAll(routeTypesList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importRoutes(String directoryPath){
        String filePath = directoryPath + File.separator + "routes.txt";
        File file = new File(filePath);

        List<Route> routeList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Optional<RouteType> optionalRouteType = routeTypeRepository.findById(Long.valueOf(values[6]));
                if (optionalRouteType.isPresent()) {
                    Route route = new Route(Long.parseLong(values[0]),
                            values[2], values[4],
                            tripRepository.findByRouteId(Long.parseLong(values[0])),
                            optionalRouteType.get());

                    routeList.add(route);
                }
            }
            routeRepository.saveAll(routeList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importTrips(String directoryPath){
        String filePath = directoryPath + File.separator + "trips.txt";
        File file = new File(filePath);

        List<Trip> tripList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Optional<Route> optionalRoute = routeRepository.findById(Long.valueOf(values[0]));
                Optional<Vehicle> optionalVehicle = vehicleRepository.findById(Long.valueOf(values[7]));
                if (optionalVehicle.isPresent() && optionalRoute.isPresent()) {
                    Trip trip = new Trip(Long.parseLong(values[2]), values[3], Integer.parseInt(values[4]),
                            optionalRoute.get(), optionalVehicle.get(),
                            accidentRepository.findByTripId(Long.parseLong(values[2])),
                            stopTimeRepository.findByTripId(Long.parseLong(values[2])));

                    tripList.add(trip);
                }
            }
            tripRepository.saveAll(tripList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importStopTimes(String directoryPath){
        String filePath = directoryPath + File.separator + "stop_times.txt";
        File file = new File(filePath);

        List<StopTime> stopTimeList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Optional<Stop> optionalStop = stopRepository.findById(Long.valueOf(values[3]));
                Optional<Trip> optionalTrip = tripRepository.findById(Long.valueOf(values[0]));
                if (optionalStop.isPresent() && optionalTrip.isPresent()) {
                    StopTime stopTime = new StopTime();
                    stopTime.setArrivalTime(LocalDateTime.parse(values[1]));
                    stopTime.setDepartureTime(LocalDateTime.parse(values[2]));
                    stopTime.setStop(optionalStop.get());
                    stopTime.setTrip(optionalTrip.get());

                    stopTimeList.add(stopTime);
                }
            }
            stopTimeRepository.saveAll(stopTimeList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
