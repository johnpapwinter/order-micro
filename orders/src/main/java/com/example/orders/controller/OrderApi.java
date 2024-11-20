package com.example.orders.controller;

import com.example.orders.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Orders", description = "The Orders API")
public interface OrderApi {

    @Operation(
            summary = "Create a new Order",
            description = "Will create a new order along with its associated line items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "data violation")
    })
    ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDTO dto);


    @Operation(
            summary = "Fetch a specific Order",
            description = "Will fetch an existing order based on the ID provided"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    ResponseEntity<OrderDTO> getOrder(@PathVariable Long id);


    @Operation(
            summary = "Update a specific Order",
            description = "Will update an existing Order based on the payload provided"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update successful"),
            @ApiResponse(responseCode = "400", description = "data violation"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderDTO dto);


    @Operation(
            summary = "Delete a specific Order based on the ID provided",
            description = "Will delete an existing Order based on the ID provided"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "delete successful"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    ResponseEntity<Void> deleteOrder(@PathVariable Long id);

}
