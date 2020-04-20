package com.gethealthy.gethealthy.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gethealthy.gethealthy.domain.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/main")
    public String products(Model model) throws JsonProcessingException {
        List<Product> productList = productRepository.findAll();
        model.addAttribute(productList);
        return "products/main";
    }
    @GetMapping("/details/{name}")
    public String productDetails(@PathVariable String name, Model model){
        Product product = productRepository.findByName(name);
        if(product==null){
            return "redirect:/products/main";
        }
        model.addAttribute(modelMapper.map(product, ProductForm.class));
        return "products/details";
    }
}