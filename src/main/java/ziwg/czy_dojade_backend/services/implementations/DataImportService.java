package ziwg.czy_dojade_backend.services.implementations;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ziwg.czy_dojade_backend.exceptions.ErrorDetails;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;

import java.io.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@AllArgsConstructor
public class DataImportService {

    private final RouteRepository routeRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final StopRepository stopRepository;
    private final StopTimeRepository stopTimeRepository;
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final AccidentRepository accidentRepository;

    public String processZip() {

        String zipUrl = "https://www.wroclaw.pl/open-data/87b09b32-f076-4475-8ec9-6020ed1f9ac0/OtwartyWroclaw_rozklad_jazdy_GTFS.zip";

        try {
            RestTemplate restTemplate = new RestTemplate();
            byte[] zipData = restTemplate.getForObject(zipUrl, byte[].class);

            unzip(zipData);

            String resourcesDirectory = getClass().getResource("/").getPath();
            String extractPath = resourcesDirectory + "GTFS";

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

    public String importVehicles() {
        List<Vehicle> vehicles = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String url = "https://www.wroclaw.pl/open-data/datastore/dump/17308285-3977-42f7-81b7-fdd168c210a2";

        String csvContent = restTemplate.getForObject(url, String.class);

        try (CSVReader csvReader = new CSVReader(new StringReader(csvContent))) {
            List<String[]> rows = csvReader.readAll();

            // Ignore the first row
            if (!rows.isEmpty()) {
                rows.remove(0);
            }

            for (String[] row : rows) {
                Vehicle vehicle = new Vehicle(Long.parseLong(row[0]),
                        Double.parseDouble(row[5]),
                        Double.parseDouble(row[6]),
                        tripRepository.findByVehicleId(Long.parseLong(row[0])));
                vehicles.add(vehicle);
            }
            vehicleRepository.saveAll(vehicles);
        } catch (CsvException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Vehicles coordinates imported.";
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
                        values[2].replaceAll("\"", ""), Double.parseDouble(values[3]), Double.parseDouble(values[4]),
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

                RouteType routeType = new RouteType(Long.parseLong(values[0]), values[1].replaceAll("\"", ""),
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
                    Route route = new Route(values[0],
                            values[2].replaceAll("\"", ""), values[4].replaceAll("\"", ""),
                            tripRepository.findByRouteId(values[0]),
                            optionalRouteType.get());

                    routeList.add(route);
                }
            }
            routeRepository.saveAll(routeList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Temporary method to create sample vehicles in database
     * @param id
     */
    private void createSampleVehicle(long id){
        Vehicle vehicle = new Vehicle(id, 50.0, 50.0, tripRepository.findByVehicleId(id));
        vehicleRepository.save(vehicle);
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

                Optional<Route> optionalRoute = routeRepository.findById(values[0]);
                Optional<Vehicle> optionalVehicle = vehicleRepository.findById(Long.valueOf(values[7]));
                if(optionalVehicle.isEmpty()){
                    createSampleVehicle(Long.parseLong(values[7]));
                    optionalVehicle = vehicleRepository.findById(Long.valueOf(values[7]));
                }
                if (optionalVehicle.isPresent() && optionalRoute.isPresent()) {
                    Trip trip = new Trip(values[2], values[3].replaceAll("\"", ""), Integer.parseInt(values[4]),
                            optionalRoute.get(), optionalVehicle.get(),
                            accidentRepository.findByTripId(values[2]),
                            stopTimeRepository.findByTripId(values[2]));

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
                Optional<Trip> optionalTrip = tripRepository.findById(values[0]);
                if (optionalStop.isPresent() && optionalTrip.isPresent()) {
                    LocalTime arrive = parseTime(values[1]);
                    LocalTime departure = parseTime(values[2]);
                    StopTime stopTime = new StopTime();
                    stopTime.setArrivalTime(arrive);
                    stopTime.setDepartureTime(departure);
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

    public static LocalTime parseTime(String timeStr) {
        // Split the time string by ":" to extract hours, minutes, and seconds
        String[] timeParts = timeStr.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Adjust hours if it exceeds 23
        if (hours == 24 && minutes == 0 && seconds == 0) {
            // Treat "24:00:00" as equivalent to "00:00:00" of the next day
            return LocalTime.MIDNIGHT;
        } else if (hours >= 24) {
            // Subtract 24 hours to make it a valid time of the next day
            hours -= 24;
        }

        return LocalTime.of(hours, minutes, seconds);
    }
}


