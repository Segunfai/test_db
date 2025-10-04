-- Создаем таблицу заказов
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Создаем таблицу товаров (автомобили)
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    category VARCHAR(100),
    horse_power INT CHECK (horse_power > 0 AND horse_power <= 160),
    body_type VARCHAR(50)
);

-- Таблица клиентов
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    customer_id INT NOT NULL,
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    quantity INT NOT NULL CHECK (quantity > 0),
    status_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (status_id) REFERENCES order_status(id)
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_order_product_id ON "order"(product_id);
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON "order"(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_date ON "order"(order_date);

COMMENT ON TABLE product IS 'Таблица автомобилей';
COMMENT ON COLUMN product.horse_power IS 'Мощность в лошадиных силах';
COMMENT ON TABLE customer IS 'Таблица клиентов';
COMMENT ON TABLE "order" IS 'Таблица заказов';