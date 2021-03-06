package com.gethealthy.gethealthy.products;

import com.gethealthy.gethealthy.account.Account;
import com.gethealthy.gethealthy.order.OrderItem;
import com.gethealthy.gethealthy.order.Orders;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String price;

    private String origin;

    private Long amount;

    private Long liked;

    @ManyToOne
    private Account seller;

    @Column(nullable = false)
    private boolean displayed;

    @Lob
    private String description;

    private Long numberOf;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String productImage;
}
