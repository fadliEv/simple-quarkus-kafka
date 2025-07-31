package com.igflife.controller;

import com.igflife.model.dto.common.ApiResponse;
import com.igflife.model.dto.common.PagedResponse;
import com.igflife.model.dto.request.OrderCreateRequest;
import com.igflife.model.dto.response.OrderResponse;
import com.igflife.service.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService orderService;

    @POST
    public Response createOrder(@Valid OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return Response
                .status(Response.Status.CREATED)
                .entity(ApiResponse.success(response))
                .build();
    }

    @GET
    public Response getOrders(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        PagedResponse<OrderResponse> response = orderService.getOrders(page, size);
        return Response.ok(response).build();
    }
}