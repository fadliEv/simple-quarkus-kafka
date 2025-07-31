package com.igflife.repository.impl;

import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;
import com.igflife.utils.SQLQueries;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

    @Inject
    DataSource dataSource;

    @Override
    public String create(Order order) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction

            ps = conn.prepareStatement(SQLQueries.INSERT_ORDER.getQuery());

            String orderId = UUID.randomUUID().toString();
            ps.setString(1, orderId);
            ps.setString(2, order.getCustomerId());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Creating order failed, no rows affected.");
            }

            conn.commit(); // Commit transaction
            return orderId;

        } catch (SQLException e) {
            rollbackTransaction(conn);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, null);
        }
    }

    @Override
    public List<Order> findAll(int page, int size) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(SQLQueries.FIND_ALL.getQuery());

            int offset = (page - 1) * size;
            ps.setInt(1, size);
            ps.setInt(2, offset);

            rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(mapToOrder(rs));
            }
            return orders;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    @Override
    public int countAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(SQLQueries.COUNT_ALL.getQuery());

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count orders: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
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

    private void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                // Log this error
                System.err.println("Failed to rollback transaction: " + e.getMessage());
            }
        }
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // Log this error
            System.err.println("Failed to close JDBC resources: " + e.getMessage());
        }
    }
}