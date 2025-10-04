package org.example;

import org.flywaydb.core.Flyway;
import java.sql.*;
import java.util.Properties;

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
            System.out.println("Добавлен товар ");

            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)");
            ps2.setString(1, "Александр");
            ps2.setString(2, "Попов");
            ps2.setString(3, "+79991234568");
            ps2.setString(4, "alex.popov@example.com");
            ps2.executeUpdate();
            System.out.println("Клиент добавлен");

            // 2. Создание заказа
            PreparedStatement ps3 = conn.prepareStatement("INSERT INTO \"order\" (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)");
            ps3.setInt(1, 11); // Honda Civic
            ps3.setInt(2, 11); // Александра
            ps3.setInt(3, 1);
            ps3.setInt(4, 1); // В обработке
            ps3.executeUpdate();
            System.out.println("Заказ создан");

            // 3. Чтение последних 5 заказов
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT o.id, c.first_name, c.last_name, p.description FROM \"order\" o JOIN customer c ON o.customer_id = c.id JOIN product p ON o.product_id = p.id ORDER BY o.id DESC LIMIT 5");
            System.out.println("Последние 5 заказов:");
            while (rs.next()) {
                System.out.printf("Заказ ID: %d, Клиент: %s %s, Товар: %s%n", rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("description"));
            }

            // 4. Обновление цены товара
            PreparedStatement ps4 = conn.prepareStatement("UPDATE product SET price = ? WHERE id = ?");
            ps4.setBigDecimal(1, new java.math.BigDecimal("1350000"));
            ps4.setInt(2, 11);
            ps4.executeUpdate();
            System.out.println("Цена обновлена");

            // 5. Удаление тестового заказа
            PreparedStatement ps5 = conn.prepareStatement("DELETE FROM \"order\" WHERE id = ?");
            ps5.setInt(1, 11);
            ps5.executeUpdate();
            System.out.println("Заказ удален");

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
