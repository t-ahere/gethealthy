package com.gethealthy.gethealthy.domain;

import lombok.*;

import javax.persistence.*;

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

    @Lob
    private String description;


    private String numberOf;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String productImage;
}