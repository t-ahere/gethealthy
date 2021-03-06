package com.gethealthy.gethealthy.products;

import com.gethealthy.gethealthy.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct//빈이 초기화 되고 실행되는 애너테이션
    public void initZoneData() throws IOException {
        if(productRepository.count()==0){
            Resource resource = new ClassPathResource("product_kr.csv");
            List<Product> zoneList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream().map(line -> {
                String[] split = line.split(",");
                return Product.builder().name(split[0]).price(split[1]).numberOf(Long.parseLong(split[2])).liked(Long.parseLong(split[3])).build();
            }).collect(Collectors.toList());
            productRepository.saveAll(zoneList);
        }
    }

    public void increaseLiked(Product product) {
        product.setLiked(product.getLiked()+1);
        productRepository.save(product);
    }

    public void decreaseLiked(Product product) {
        product.setLiked(product.getLiked()-1);
        productRepository.save(product);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProduct(Product byName) {
        byName.setDisplayed(false);
        productRepository.save(byName);
    }

    public void createProduct(Account account, Product product) {
        product.setSeller(account);
        product.setDisplayed(true);
        product.setLiked(0L);
        productRepository.save(product);
    }
}
