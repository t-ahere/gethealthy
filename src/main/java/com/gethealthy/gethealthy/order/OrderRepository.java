package com.gethealthy.gethealthy.order;

import com.gethealthy.gethealthy.account.Account;
import com.gethealthy.gethealthy.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Orders,Long> {
}
