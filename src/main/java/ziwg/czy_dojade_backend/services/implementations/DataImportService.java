package ziwg.czy_dojade_backend.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ziwg.czy_dojade_backend.dtos.VehicleDto;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;
import ziwg.czy_dojade_backend.utils.DateTimeAndTimeParser;

import java.io.*;
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

    public File getFileWithCoordinates() {
        String apiUrl = "https://czynaczas.pl/api/wroclaw/single-live-vehicle/premium/2769e83f-fd5e-4669-b5fd-59ce08f56144";
        WebClient webClient = WebClient.create();
        
        Mono<byte[]> responseMono = webClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class);

        byte[] fileBytes = responseMono.block();

        if (fileBytes != null) {
            try {
                File tempFile = File.createTempFile("tempFile", ".tmp");

                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(fileBytes);
                }
                return tempFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Error: Received null byte array from the API");
            return null;
        }
    }

    @Scheduled(fixedRate = 10000)
    public String importVehicleCoordinates() {
        List<Vehicle> vehicles = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        File jsonFile = getFileWithCoordinates();
        try {
            Map<String, Map<String, Object>> dataMap = objectMapper.readValue(jsonFile, Map.class);
            // Access the nested data
            Map<String, Object> innerMap = dataMap.get("data");
            for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
                // vehicleJson - each vehicle in json with vehicles
                String vehicleJson = objectMapper.writeValueAsString(entry.getValue());
                // Parse vehicleJson to extract key-value pairs
                JsonNode jsonNode = objectMapper.readTree(vehicleJson);
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
                Vehicle vehicle = new Vehicle();
                Optional<Vehicle> existingVehicleOpt = null;
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();
                    String key = field.getKey();
                    JsonNode value = field.getValue();
                    if ("id".equals(key)) {
                        String id = value.asText();
                        String[] parts = id.split("/");
                        int idInt = Integer.parseInt(parts[1]);
                        existingVehicleOpt = vehicleRepository.findById(vehicle.getId());
                        vehicle.setId(idInt);
                    }
                    if ("lat".equals(key)) {
                        double lat = value.asDouble();
                        vehicle.setCurrLatitude(lat);
                    }
                    if ("lon".equals(key)) {
                        double lon = value.asDouble();
                        vehicle.setCurrLongitude(lon);
                    }
                    if ("trip_id".equals(key)) {
                        String tripId = value.asText();
                        vehicle.setTrip(tripRepository.findAllById(tripId));
                    }
                    if ("delay".equals(key)) {
                        long delay = value.asLong();
                        vehicle.setDelay(delay);
                    }
                    if ("type".equals(key)) {
                        long type = value.asLong();
                        vehicle.setType(type);
                    }
                }
                if (vehicle.getDelay() == null)
                    vehicle.setDelay(0L);
                if (existingVehicleOpt.isPresent()) {
                    Vehicle existingVehicle = existingVehicleOpt.get();
                    existingVehicle.setCurrLatitude(vehicle.getCurrLatitude());
                    existingVehicle.setCurrLongitude(vehicle.getCurrLongitude());
                    existingVehicle.setType(vehicle.getType());
                    existingVehicle.setTrip(vehicle.getTrip());
                    existingVehicle.setDelay(vehicle.getDelay());
                    vehicles.add(existingVehicle);
                }
                else {
                    vehicles.add(vehicle);
                }
            }
            vehicleRepository.saveAll(vehicles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Imported from czynaczas.pl";
    }

    public List<VehicleDto> getVehicles(){
        List<VehicleDto> vehicleDtos = new LinkedList<>();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        for (Vehicle vehicle:vehicles) {
            VehicleDto vehicleDto = new VehicleDto(vehicle.getCurrLatitude(), vehicle.getCurrLongitude());
            vehicleDtos.add(vehicleDto);
        }
        return vehicleDtos;
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
        Vehicle vehicle = new Vehicle(id, 50.0, 50.0,0L,null, tripRepository.findByVehicleId(id));
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
                    LocalTime arrive = DateTimeAndTimeParser.parseTime(values[1]);
                    LocalTime departure = DateTimeAndTimeParser.parseTime(values[2]);
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

}


