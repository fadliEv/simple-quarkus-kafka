package com.igflife.repository.impl;

import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

    @Inject
    DataSource dataSource;

    @Override
    public String create(Order order) {
        String sql = "INSERT INTO orders (order_id, customer_id, order_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        String orderId = UUID.randomUUID().toString();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ps.setString(2, order.getCustomerId());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            return orderId;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findById(String orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToOrder(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll(int page, int size) {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapToOrder(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage(), e);
        }

        return orders;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM orders";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean updateStatus(String orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, orderId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    // Helper method to map ResultSet to Order entity
    private Order mapToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getString("order_id"));
        order.setCustomerId(rs.getString("customer_id"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        return order;
    }
}