-- =========================
-- USERS DATA
-- =========================

INSERT INTO users(name,email,password,role) VALUES
('Admin','admin@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','ADMIN'),

('Rahul Sharma','rahul@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Priya Patil','priya@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Amit Joshi','amit@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Sneha Kulkarni','sneha@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Karan Mehta','karan@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4fCzR8L1H7K','USER'),

('Neha Verma','neha@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Rohan Singh','rohan@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Pooja Desai','pooja@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER'),

('Arjun Nair','arjun@gmail.com','$2a$10$8HnYx8T7kKQ7fJ9zW2Pz3eM7L1nQx5uVbYcT9rF6sD8gH2jK4mN9O','USER');


-- =========================
-- STUDENT DATA
-- =========================

INSERT INTO student(name,email,course,age,deleted,profile_image) VALUES
('Rahul Sharma','rahul.student@gmail.com','Java',22,false,NULL),

('Priya Patil','priya.student@gmail.com','Spring Boot',24,false,NULL),

('Amit Joshi','amit.student@gmail.com','React JS',21,false,NULL),

('Sneha Kulkarni','sneha.student@gmail.com','Python',23,false,NULL),

('Karan Mehta','karan.student@gmail.com','Data Structures',20,false,NULL),

('Neha Verma','neha.student@gmail.com','Machine Learning',25,false,NULL),

('Rohan Singh','rohan.student@gmail.com','SQL',22,false,NULL),

('Pooja Desai','pooja.student@gmail.com','Microservices',24,false,NULL),

('Arjun Nair','arjun.student@gmail.com','Docker',23,false,NULL),

('Meera Iyer','meera.student@gmail.com','AWS',26,false,NULL);