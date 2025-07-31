package com.igflife.utils;

public enum SQLQueries{
    INSERT_ORDER("""
        INSERT INTO orders (order_id, customer_id, order_date, total_amount, status)
        VALUES (?, ?, ?, ?, ?)
     """),

    FIND_ALL("""
        SELECT * FROM orders
        ORDER BY order_date DESC
        LIMIT ? /* size */
        OFFSET ? /* (page-1)*size */
    """),


    COUNT_ALL("SELECT COUNT(*) FROM orders");

    private final String query;

    SQLQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}