package com.igflife.repository.impl;

import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderRepositoryImpl  implements OrderRepository {

    @Inject
    DataSource dataSource;

    @Override
    public void save(Order order) {
        String query = "INSERT INTO orders (order_id, customer_id, order_date, total_amount, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getCustomerId());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            stmt.setBigDecimal(4, order.getTotalAmount());
            stmt.setString(5, order.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save order", e);
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orders.add(mapOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch orders", e);
        }

        return orders;
    }

    @Override
    public Order findById(String orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapOrderFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch order by ID", e);
        }

        return null;
    }

    private Order mapOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order(
                rs.getString("order_id"),
                rs.getString("customer_id"),
                rs.getTimestamp("order_date").toLocalDateTime(),
                rs.getBigDecimal("total_amount"),
                rs.getString("status")
        );
    }
}
