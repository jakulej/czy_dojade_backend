package ziwg.czy_dojade_backend.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.models.VehicleData.VehicleData;
import ziwg.czy_dojade_backend.models.VehicleData.VehicleDataDetails;
import ziwg.czy_dojade_backend.repositories.*;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;
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
    private final ObjectMapper objectMapper;

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
    @PostConstruct
    public String importFromCzyNaCzas() throws JsonProcessingException {
        List<Vehicle> vehicles = new LinkedList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Path path1 = Path.of("C:\\Users\\macie\\OneDrive\\Pulpit\\wroclaw-live.json");
        Path path2 = Path.of("C:\\Users\\macie\\OneDrive\\Pulpit\\wroclaw-live2.json");

        File jsonFile = new File(path1.toString());
        try {
            // Read JSON data from file into a Map<String, Map<String, Object>>
            Map<String, Map<String, Object>> dataMap = objectMapper.readValue(jsonFile, Map.class);
            // Access the nested data
            Map<String, Object> innerMap = dataMap.get("data");
            for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
                // Convert each inner map to JSON string
                String innerJson = objectMapper.writeValueAsString(entry.getValue());
                // Parse innerJson to extract key-value pairs
                JsonNode jsonNode = objectMapper.readTree(innerJson);
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
                Vehicle vehicle = new Vehicle();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
                    // Extract key-value pairs
                    String key = field.getKey();
                    JsonNode value = field.getValue();
                    // Process key-value pairs as needed
                    System.out.println("Key: " + key + ", Value: " + value);
                    if ("lat".equals(key)) {
                        // Process latitude value
                        double latitude = value.asDouble();
                        vehicle.setCurrLatitude(latitude);
                        System.out.println("Latitude: " + latitude);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Imported from czynaczas.pl";
    }

    /**
     *  Temporarely k as id of vehicle. In "name" there is info about line number/letter
     * @param key: busList[tram][] or busList[bus][]
     * @param value: number/letter of line
     * @return
     */
    public String importMpkLocalization(String key, String value) {
        // URL and form data
        String url = "https://mpk.wroc.pl/bus_position";
        List<Vehicle> vehicles = new LinkedList<>();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(key, value);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        try {
            String responseBody = response.getBody();
            if (responseBody != null) {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                if (jsonNode.isArray()) {
                    for (JsonNode node : jsonNode) {
                        Vehicle vehicle = new Vehicle(node.get("k").asLong(),
                                node.get("x").asDouble(),
                                node.get("y").asDouble(),
                                null);
                        vehicles.add(vehicle);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        vehicleRepository.saveAll(vehicles);
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


