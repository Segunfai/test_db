package org.example;

import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class App {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        // Запуск миграций
        Flyway flyway = Flyway.configure().dataSource(url, username, password).load();
        flyway.migrate();

        Connection conn = DriverManager.getConnection(url, username, password);

        try {
            conn.setAutoCommit(false);

            // Добавление данных в таблицы
            PreparedStatement ps1 = conn.prepareStatement("INSERT INTO product (description, price, quantity, category, horse_power, body_type) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setString(1, "Honda Civic");
            ps1.setBigDecimal(2, new java.math.BigDecimal("1300000"));
            ps1.setInt(3, 4);
            ps1.setString(4, "Хэтчбек");
            ps1.setInt(5, 140);
            ps1.setString(6, "Хэтчбек");
            ps1.executeUpdate();
            System.out.println("Товар добавлен");

            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)");
            ps2.setString(1, "Александр");
            ps2.setString(2, "Попов");
            ps2.setString(3, "+79991234568");
            ps2.setString(4, "alex.popov@example.com");
            ps2.executeUpdate();
            System.out.println("Клиент добавлен");

            // 2. Создание заказов
            PreparedStatement psOrder = conn.prepareStatement(
                    "INSERT INTO \"order\" (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)"
            );

            int[] productIds = {1, 2, 1, 3, 1, 4, 2, 5, 1, 2, 3, 1, 4, 5, 2, 3, 1, 5, 4, 2, 11}; // 21 шт., 1 часто повторяется
            int[] customerIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; // 21 шт.
            int[] statuses = {2, 1, 3, 1, 4, 2, 3, 1, 2, 3, 1, 4, 2, 3, 1, 2, 4, 2, 1, 1, 2}; //2 - Доставляется, 3 - Выполнен, 4 - Отменен

            for (int i = 0; i < 21; i++) {
                psOrder.setInt(1, productIds[i]);
                psOrder.setInt(2, customerIds[i]);
                psOrder.setInt(3, 1); // количество = 1
                psOrder.setInt(4, statuses[i]);
                psOrder.executeUpdate();
            }
            System.out.println("Добавлены заказы");

            // 3. Чтение последних 5 заказов
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT o.id, c.first_name, c.last_name, p.description FROM \"order\" o JOIN customer c ON o.customer_id = c.id JOIN product p ON o.product_id = p.id ORDER BY o.id DESC LIMIT 5");
            System.out.println("Последние 5 заказов:");
            while (rs.next()) {
                System.out.printf("Заказ ID: %d, Клиент: %s %s, Товар: %s%n", rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("description"));
            }

            // 4. Обновление цены товара (добавление информации)
            Random random1 = new Random();
            int productIdToUpdate = random1.nextInt(10) + 1;
            BigDecimal newPrice = new java.math.BigDecimal("1350000");

            // Сначала получим текущие данные товара
            PreparedStatement selectProduct = conn.prepareStatement("SELECT description, price FROM product WHERE id = ?");
            selectProduct.setInt(1, productIdToUpdate);
            ResultSet rsProduct = selectProduct.executeQuery();

            if (rsProduct.next()) {
                String description = rsProduct.getString("description");
                BigDecimal oldPrice = rsProduct.getBigDecimal("price");

                // Теперь обновляем
                PreparedStatement ps4 = conn.prepareStatement("UPDATE product SET price = ? WHERE id = ?");
                ps4.setBigDecimal(1, newPrice);
                ps4.setInt(2, productIdToUpdate);
                ps4.executeUpdate();

                System.out.printf("Цена обновлена у товара: %s (ID=%d)\n", description, productIdToUpdate);
                System.out.printf("   Старая цена: %.2f → Новая цена: %.2f%n", oldPrice, newPrice);
            } else {
                System.out.println("Товар с ID=" + productIdToUpdate + " не найден для обновления цены.");
            }

            // 5. Удаление тестового заказа (добавлена информация о заказе)
            Random random = new Random();
            int orderIdToDelete = random.nextInt(21)+ 1;

            // Получим данные заказа перед удалением
            PreparedStatement selectOrder = conn.prepareStatement(
                    "SELECT o.id, c.first_name, c.last_name, p.description " +
                            "FROM \"order\" o " +
                            "JOIN customer c ON o.customer_id = c.id " +
                            "JOIN product p ON o.product_id = p.id " +
                            "WHERE o.id = ?");
            selectOrder.setInt(1, orderIdToDelete);
            ResultSet rsOrder = selectOrder.executeQuery();

            if (rsOrder.next()) {
                int id = rsOrder.getInt("id");
                String customerName = rsOrder.getString("first_name") + " " + rsOrder.getString("last_name");
                String productDesc = rsOrder.getString("description");

                // Теперь удаляем
                PreparedStatement ps5 = conn.prepareStatement("DELETE FROM \"order\" WHERE id = ?");
                ps5.setInt(1, orderIdToDelete);
                ps5.executeUpdate();

                System.out.printf("Заказ ID=%d удалён\n", id);
                System.out.printf("   Клиент: %s, Товар: %s%n", customerName, productDesc);
            } else {
                System.out.println("Заказ с ID=" + orderIdToDelete + " не найден для удаления.");
            }

            conn.commit();
            System.out.println("Транзакция успешно выполнена");
        } catch (Exception e) {
            conn.rollback();
            System.out.println("Ошибка, транзакция откатилась");
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}