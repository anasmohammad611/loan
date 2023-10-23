package com.example.loan.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The type User detail.
 */
@Entity
@Table(name = "customer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(generator = "SEQ_customer_customer_id")
    @SequenceGenerator(name = "SEQ_customer_customer_id", sequenceName = "SEQ_customer_customer_id", allocationSize = 1)
    @Column(name = "customer_id")
    private long customerId;

    private String name;

    private String pan;
}
