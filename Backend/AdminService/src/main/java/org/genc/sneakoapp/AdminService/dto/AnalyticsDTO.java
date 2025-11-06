package org.genc.sneakoapp.AdminService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsDTO {


    Long totalRevenue;

    Long totalOrders;

    Long totalCustomers;

    Long totalProducts;
}

