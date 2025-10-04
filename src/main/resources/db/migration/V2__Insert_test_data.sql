-- Добавляем статусы заказов
INSERT INTO order_status (name) VALUES ('В обработке'), ('Доставляется'), ('Выполнен'), ('Отменен');

-- Добавляем тестовых клиентов
INSERT INTO customer (first_name, last_name, phone, email) VALUES
    ('Иван', 'Иванов', '+79991234567', 'ivan@example.com'),
    ('Мария', 'Петрова', '+79987654321', 'maria@example.com'),
    ('Алексей', 'Смирнов', '+79991112233', 'alex@example.com'),
    ('Елена', 'Козлова', '+79993334455', 'elena@example.com'),
    ('Дмитрий', 'Волков', '+79994445566', 'dmitry@example.com'),
    ('Ольга', 'Морозова', '+79995556677', 'olga@example.com'),
    ('Анна', 'Соколова', '+79996667788', 'anna@example.com'),
    ('Артем', 'Лебедев', '+79997778899', 'artem@example.com'),
    ('Татьяна', 'Новикова', '+79998889900', 'tanya@example.com'),
    ('Сергей', 'Васильев', '+79999990011', 'sergey@example.com');

-- Добавляем товары
INSERT INTO product (description, price, quantity, category, horse_power, body_type) VALUES
    ('Toyota Camry', 1500000.00, 5, 'Седан', 150, 'Седан'),
    ('Lada Granta', 700000.00, 10, 'Седан', 106, 'Седан'),
    ('Kia Rio', 900000.00, 7, 'Седан', 123, 'Седан'),
    ('Hyundai Solaris', 850000.00, 8, 'Седан', 110, 'Седан'),
    ('Volkswagen Polo', 1000000.00, 6, 'Седан', 115, 'Седан'),
    ('Renault Logan', 650000.00, 12, 'Седан', 102, 'Седан'),
    ('Skoda Rapid', 950000.00, 4, 'Седан', 116, 'Седан'),
    ('Nissan Almera', 880000.00, 5, 'Седан', 110, 'Седан'),
    ('Mazda 3', 1400000.00, 3, 'Хэтчбек', 150, 'Хэтчбек'),
    ('Volkswagen Tayron', 1650000.00, 4, 'Кроссовер', 150, 'Кроссовер'),
    ('Ford Focus', 1200000.00, 6, 'Хэтчбек', 125, 'Хэтчбек');