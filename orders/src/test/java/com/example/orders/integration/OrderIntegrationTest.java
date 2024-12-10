package com.example.orders.integration;

import com.example.orders.dto.OrderDTO;
import com.example.orders.dto.OrderLineDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.security.AppPrincipal;
import com.example.orders.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private String token;

    @BeforeEach
    void setUp() {
        AppPrincipal principal = AppPrincipal.builder()
                .username("hank")
                .password("123")
                .authorities(List.of(new SimpleGrantedAuthority("ADMIN")))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        token = jwtUtils.generateToken(authentication);
    }


    @Test
    @DisplayName("Should create a new order when valid data is provided")
    void createOrder_ShouldCreateNewOrder() throws Exception {
        // Arrange
        OrderDTO orderDTO = createSampleOrderDTO();
        String content = objectMapper.writeValueAsString(orderDTO);

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Long orderId = objectMapper.readValue(result.getResponse().getContentAsString(), Long.class);
        assertNotNull(orderId);
    }

    @Test
    @DisplayName("Should fail to create order when invalid data is provided")
    void createOrder_ShouldFailWithInvalidData() throws Exception {
        // Arrange
        OrderDTO invalidOrderDTO = OrderDTO.builder()
                .orderId("")  // Invalid: empty
                .customerName("")  // Invalid: empty
                .orderLines(List.of())  // Invalid: empty list
                .build();
        String content = objectMapper.writeValueAsString(invalidOrderDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains(ErrorMessages.ORDER_ID_CANNOT_BE_EMPTY));
                    assertTrue(responseBody.contains(ErrorMessages.CUSTOMER_NAME_CANNOT_BE_EMPTY));
                    assertTrue(responseBody.contains(ErrorMessages.ORDER_MUST_NOT_BE_EMPTY));
                });
    }


    @Test
    @DisplayName("Should return order when valid ID is provided")
    void getOrder_ShouldReturnOrder() throws Exception {
        // Arrange
        long orderId = 1L;

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        OrderDTO orderDTO = objectMapper.readValue(result.getResponse().getContentAsString(), OrderDTO.class);
        assertEquals(orderId, orderDTO.getId());
        assertEquals("Bobby Drake", orderDTO.getCustomerName());
        assertEquals("XK1203", orderDTO.getOrderId());
        assertNotNull(orderDTO.getOrderStatus());
        assertEquals(2, orderDTO.getOrderLines().size());
    }

    @Test
    @DisplayName("Should return 404 when order is not found")
    void getOrder_ShouldReturn404WhenNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResponse()
                        .getContentAsString()
                        .contains(ErrorMessages.ORDER_NOT_FOUND)));
    }

    @Test
    @DisplayName("Should update order when valid data is provided")
    void updateOrder_ShouldUpdateOrder() throws Exception {
        // Arrange
        long orderId = 1L;
        // First get the existing order
        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andReturn();
        OrderDTO existingOrder = objectMapper.readValue(getResult.getResponse().getContentAsString(), OrderDTO.class);

        // Modify the order
        existingOrder.setOrderStatus(OrderStatus.PROCESSED);
        String content = objectMapper.writeValueAsString(existingOrder);

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        OrderDTO updatedOrder = objectMapper.readValue(result.getResponse().getContentAsString(), OrderDTO.class);
        assertEquals(OrderStatus.PROCESSED, updatedOrder.getOrderStatus());
    }

    @Test
    @DisplayName("Should delete order when valid ID is provided")
    void deleteOrder_ShouldDeleteOrder() throws Exception {
        // Arrange
        long orderId = 1L;

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify deletion
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private OrderDTO createSampleOrderDTO() {
        OrderLineDTO lineDTO = OrderLineDTO.builder()
                .productId("TEST-PROD-1")
                .quantity(5)
                .price(10.0)
                .build();

        return OrderDTO.builder()
                .orderId("TEST-ORDER-1")
                .customerName("Test Customer")
                .orderStatus(OrderStatus.UNPROCESSED)
                .orderDate(LocalDate.now())
                .orderLines(List.of(lineDTO))
                .build();
    }



}
