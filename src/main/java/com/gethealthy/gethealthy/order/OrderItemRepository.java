package com.gethealthy.gethealthy.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
