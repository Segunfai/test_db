-- Добавление тестовых запросов к созданной БД
-- 1. Все заказы за последние 7 дней с именем клиента и описанием товара
SELECT o.id, c.first_name, c.last_name, p.description, o.order_date
FROM "order" o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 days';

-- 2. Топ-3 самых популярных товаров
SELECT p.description, COUNT(o.id) AS order_count
FROM product p
JOIN "order" o ON p.id = o.product_id
GROUP BY p.id, p.description
ORDER BY order_count DESC
LIMIT 3;

-- 3. Заказы с указанием статуса
SELECT o.id, c.first_name, p.description, os.name AS status
FROM "order" o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status os ON o.status_id = os.id;

-- 4. Средняя цена автомобиля по типу кузова
SELECT body_type, AVG(price) AS avg_price
FROM product
GROUP BY body_type;

-- 5. Список клиентов, у которых есть заказы
SELECT DISTINCT c.first_name, c.last_name
FROM customer c
JOIN "order" o ON c.id = o.customer_id;

-- 6. Обновление цены товара
UPDATE product SET price = 1600000.00 WHERE description = 'Toyota Camry';

-- 7. Изменение статуса заказа
UPDATE "order" SET status_id = 2 WHERE id = 1;
UPDATE "order" SET status_id = 3 WHERE id = 3;

-- 8. Изменение количества на складе
UPDATE product SET quantity = quantity - 1 WHERE id = 1;

-- 9. Удаление клиентов без заказов
DELETE FROM customer WHERE id NOT IN (SELECT DISTINCT customer_id FROM "order");

-- 10. Удаление заказа
DELETE FROM "order" WHERE id = 1;