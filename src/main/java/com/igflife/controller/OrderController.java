package com.igflife.controller;

import com.igflife.model.dto.OrderDto;
import com.igflife.model.response.ApiResponse;
import com.igflife.service.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService orderService;

    @POST
    public Response createOrder(OrderDto orderDTO) {
        OrderDto createdOrder = orderService.createOrder(orderDTO);
        return Response.ok(ApiResponse.success(createdOrder)).build();
    }

    @GET
    public Response getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return Response.ok(ApiResponse.success(orders)).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") String orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return Response.ok(ApiResponse.success(order)).build();
    }
}