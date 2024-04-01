INSERT INTO app_user (username, hash_password, email, first_name, last_name, subscriber)
VALUES
    ('user1', '$2a$10$wMOV3vrXwQmwmNQx0qEOxuICbS/6HpTa.lruJU2w5qjAkYV2P1ABK', 'user1@example.com', 'John', 'Doe', true),        -- password1
    ('user2', '$2a$10$YsYRGVPbpTKsFYNqM6y//ef4cdu2aPGnKMkFz0kIFpTw8.D/boi3i', 'user2@example.com', 'Jane', 'Smith', false),     -- password2
    ('user3', '$2a$10$Cf19F9hlJVRbU9DdFCfgROGGkmc3CDUEl1/9xI3uXcKkYr8iDRHMy', 'user3@example.com', 'Alice', 'Johnson', true),   -- password3
    ('user4', '$2a$10$ayVB2ZBv/ZR9AFeCy73gvOrhOCVejbPsg50Ufi9dIgBTTRjxVAca6', 'user4@example.com', 'Bob', 'Brown', false),      -- password4
    ('user5', '$2a$10$sMQqUBfqtF0baPzehx9AVuQNPUni4t45/pWztuixqNupYlXf0oz6W', 'user5@example.com', 'Emma', 'White', true);      -- password5

INSERT INTO vehicle (curr_latitude, curr_longitude)
VALUES
    (52.2297, 21.0122),
    (40.7128, -74.0060),
    (51.5074, -0.1278),
    (34.0522, -118.2437),
    (35.6895, 139.6917);

INSERT INTO route_type (type)
VALUES
    ('Autobus dzienny'),
    ('Autobus nocny'),
    ('Tramwaj');

INSERT INTO route (short_name, long_name, description, route_type_id)
VALUES
    ('149', 'Long_Name_149', 'Description 149', (SELECT id FROM route_type WHERE type = 'Autobus dzienny')),
    ('255', 'Long_Name_255', 'Description 255', (SELECT id FROM route_type WHERE type = 'Autobus nocny')),
    ('16', 'Long_Name_16', 'Description 16', (SELECT id FROM route_type WHERE type = 'Tramwaj')),
    ('12', 'Long_Name_12', 'Description 12', (SELECT id FROM route_type WHERE type = 'Tramwaj')),
    ('3', 'Long_Name_3', 'Description 3', (SELECT id FROM route_type WHERE type = 'Tramwaj'));

INSERT INTO route_user (route_id, user_id)
VALUES
    ((SELECT id FROM route WHERE short_name = '149'), (SELECT id FROM app_user WHERE username = 'user1')),
    ((SELECT id FROM route WHERE short_name = '149'), (SELECT id FROM app_user WHERE username = 'user2')),
    ((SELECT id FROM route WHERE short_name = '255'), (SELECT id FROM app_user WHERE username = 'user3')),
    ((SELECT id FROM route WHERE short_name = '16'), (SELECT id FROM app_user WHERE username = 'user1')),
    ((SELECT id FROM route WHERE short_name = '16'), (SELECT id FROM app_user WHERE username = 'user4'));

INSERT INTO stop (code, name, latitude, longitude)
VALUES
    ('Code1', 'Plac Wróblewskiego', 52.2297, 21.0122),
    ('Code2', 'Galeria Dominikańska', 40.7128, -74.0060),
    ('Code3', 'Kliniki (Politechnika Wrocławska)', 51.5074, -0.1278),
    ('Code4', 'Plac Grunwaldzki', 34.0522, -118.2437),
    ('Code5', 'Most Grunwaldzki', 35.6895, 139.6917);

INSERT INTO trip (start_time, end_time, trip_headsign, direction_id, route_id, vehicle_id)
VALUES
    (TIMESTAMP '2023-11-15 13:00:00', NOW(), '16 - Tarnogaj', 1, (SELECT id FROM route WHERE short_name = '16'), (SELECT id FROM vehicle WHERE curr_latitude = 52.2297)),
    (TIMESTAMP '2023-12-15 14:00:00', NOW(), '16 - Zoo', 0, (SELECT id FROM route WHERE short_name = '16'), (SELECT id FROM vehicle WHERE curr_latitude = 51.5074)),
    (TIMESTAMP '2023-09-15 15:00:00', NOW(), '3 - Hubska', 0, (SELECT id FROM route WHERE short_name = '3'), (SELECT id FROM vehicle WHERE curr_latitude = 52.2297)),
    (TIMESTAMP '2023-08-15 16:00:00', NOW(), '149 - Plac Grunwaldzki', 0, (SELECT id FROM route WHERE short_name = '149'), (SELECT id FROM vehicle WHERE curr_latitude = 51.5074)),
    (TIMESTAMP '2023-07-15 17:00:00', NOW(), '255 - Lotnisko', 1, (SELECT id FROM route WHERE short_name = '255'), (SELECT id FROM vehicle WHERE curr_latitude = 52.2297));

INSERT INTO accident (acc_latitude, acc_longitude, is_verified, time_of_accident, trip_id)
VALUES
    (52.2297, 21.0122, true, '2024-03-30T08:00:00', (SELECT id FROM trip WHERE trip_headsign = '16 - Tarnogaj')),
    (40.7128, -74.0060, false, '2024-03-30T10:00:00', (SELECT id FROM trip WHERE trip_headsign = '16 - Tarnogaj')),
    (51.5074, -0.1278, true, '2024-03-30T12:00:00', (SELECT id FROM trip WHERE trip_headsign = '16 - Zoo')),
    (34.0522, -118.2437, false, '2024-03-30T14:00:00', (SELECT id FROM trip WHERE trip_headsign = '3 - Hubska')),
    (35.6895, 139.6917, true, '2024-03-30T16:00:00', (SELECT id FROM trip WHERE trip_headsign = '149 - Plac Grunwaldzki'));

INSERT INTO stop_time (arrival_time, departure_time, stop_id, trip_id)
VALUES
    ('2024-03-30T08:45:00', '2024-03-30T09:00:00', (SELECT id FROM stop WHERE name = 'Plac Wróblewskiego'), (SELECT id FROM trip WHERE trip_headsign = '3 - Hubska')),
    ('2024-03-30T10:45:00', '2024-03-30T11:00:00', (SELECT id FROM stop WHERE name = 'Plac Wróblewskiego'), (SELECT id FROM trip WHERE trip_headsign = '16 - Tarnogaj')),
    ('2024-03-30T12:45:00', '2024-03-30T13:00:00', (SELECT id FROM stop WHERE name = 'Kliniki (Politechnika Wrocławska)'), (SELECT id FROM trip WHERE trip_headsign = '16 - Zoo')),
    ('2024-03-30T14:45:00', '2024-03-30T15:00:00', (SELECT id FROM stop WHERE name = 'Plac Grunwaldzki'), (SELECT id FROM trip WHERE trip_headsign = '16 - Zoo')),
    ('2024-03-30T14:45:00', '2024-03-30T15:00:00', (SELECT id FROM stop WHERE name = 'Galeria Dominikańska'), (SELECT id FROM trip WHERE trip_headsign = '255 - Lotnisko')),
    ('2024-03-30T16:45:00', '2024-03-30T17:00:00', (SELECT id FROM stop WHERE name = 'Most Grunwaldzki'), (SELECT id FROM trip WHERE trip_headsign = '149 - Plac Grunwaldzki'));

INSERT INTO report (description, time_of_report, user_id, accident_id)
VALUES
    ('Description 1', '2024-03-30T08:30:00', (SELECT id FROM app_user WHERE username = 'user1' LIMIT 1), (SELECT id FROM accident WHERE trip_id = (SELECT id FROM trip WHERE trip_headsign = '16 - Tarnogaj') LIMIT 1)),
    ('Description 1', '2024-03-30T08:30:00', (SELECT id FROM app_user WHERE username = 'user1' LIMIT 1), (SELECT id FROM accident WHERE trip_id = (SELECT id FROM trip WHERE trip_headsign = '16 - Tarnogaj') LIMIT 1)),
    ('Description 1', '2024-03-30T08:30:00', (SELECT id FROM app_user WHERE username = 'user2' LIMIT 1), (SELECT id FROM accident WHERE trip_id = (SELECT id FROM trip WHERE trip_headsign = '16 - Zoo') LIMIT 1)),
    ('Description 1', '2024-03-30T08:30:00', (SELECT id FROM app_user WHERE username = 'user3' LIMIT 1), (SELECT id FROM accident WHERE trip_id = (SELECT id FROM trip WHERE trip_headsign = '3 - Hubska') LIMIT 1)),
    ('Description 1', '2024-03-30T08:30:00', (SELECT id FROM app_user WHERE username = 'user5' LIMIT 1), (SELECT id FROM accident WHERE trip_id = (SELECT id FROM trip WHERE trip_headsign = '149 - Plac Grunwaldzki') LIMIT 1));

