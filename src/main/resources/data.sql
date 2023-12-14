INSERT INTO USERS (created_at, updated_at, user_name, password, phone_number, user_type, role)
VALUES
    (now(), now(), 'admin', '$2a$10$FSMXcFOoJh8wXbsUozbcw.B6Gg2tFjlVAFD6X585O53M8DWpSF9o.', '1234567890', 'ADMIN', ARRAY[2]);

INSERT INTO Admin ( name, email, center_code, user_id)
VALUES
    ('Admin 1', 'admin1@example.com', 'CenterCode1', 1);