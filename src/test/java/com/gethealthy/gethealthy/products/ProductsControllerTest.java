package com.gethealthy.gethealthy.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gethealthy.gethealthy.WithAccount;
import com.gethealthy.gethealthy.account.Account;
import com.gethealthy.gethealthy.account.AccountRepository;
import com.gethealthy.gethealthy.community.Post;
import com.gethealthy.gethealthy.community.PostRepository;
import com.gethealthy.gethealthy.products.form.ProductForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("상품 목록 정렬 테스트")
    @Test
    public void getProducts() throws Exception {
//        Product product = new Product();
//        product.setName("홍삼즙");
//        productRepository.save(product);

        mockMvc.perform(get("/products")
        .param("page","0")
        .param("size","10")
        .param("sort","name"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAccount("dongkyun")
    @DisplayName("장바구니에 상품 추가")
    @Test
    public void addProductInCart() throws Exception {

        ProductForm productForm = new ProductForm();
        productForm.setName("홍삼즙");

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productForm))
                .with(csrf()))

                .andExpect(status().isOk());

        Product product = productRepository.findByName("홍삼즙");
        assertNotNull(product);
        Account dongkyun = accountRepository.findByNickname("dongkyun");
        assertNotNull(dongkyun);
        assertTrue(dongkyun.getCart().contains(product));
    }
    @WithAccount("dongkyun")
    @DisplayName("장바구니에 상품 제거")
    @Test
    public void removeProductInCart() throws Exception {

        ProductForm productForm = new ProductForm();
        productForm.setName("홍삼즙");

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productForm))
                .with(csrf()))
                .andExpect(status().is3xxRedirection());


        Product product = productRepository.findByName("홍삼즙");
        Account dongkyun = accountRepository.findByNickname("dongkyun");
        assertTrue(dongkyun.getCart().contains(product));


        mockMvc.perform(post("/cart/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productForm))
                .with(csrf()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(status().is3xxRedirection());


        Product product1 = productRepository.findByName("홍삼즙");
        Account dongkyun1 = accountRepository.findByNickname("dongkyun");
        assertFalse(dongkyun1.getCart().contains(product1));
    }

    @WithAccount("dongkyun")
    @DisplayName("홍삼즙 : 좋아요 토글 post")
    @Test
    public void LikedPostRequest() throws Exception {

        mockMvc.perform(post("/홍삼즙/liked")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());
        Product product = productRepository.findByName("홍삼즙");
        assertEquals(product.getName(),"홍삼즙");
        assertEquals(1,product.getLiked());

        mockMvc.perform(post("/홍삼즙/liked")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());
        Product product1 = productRepository.findByName("홍삼즙");
        assertEquals(product1.getName(),"홍삼즙");
        assertEquals(0,product1.getLiked());
    }

    @DisplayName("상품 리뷰 리스트")
    @Test
    void reviewList() throws Exception {
        ProductForm productForm = new ProductForm();
        productForm.setName("홍삼즙");
        createPost();
        createPost();
        createPost();
        createPost();
        createPost();
        createPost();
        createPost();
        createPost();
        createPost();
        mockMvc.perform(get("/products/details/홍삼즙")
//                .param("page","0")
//                .param("size","10")
//                .param("sort","created")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(productForm))
//                .with(csrf())
        )
                .andDo(print())
                .andExpect(model().attributeExists("reviews"))
                .andExpect(status().isOk());
    }


    private void createPost() {

        Product item = productRepository.findByName("홍삼즙");
        Post review = Post.builder().title("제목" ).contents("내용" )
                .created(LocalDateTime.now())
                .liked((long) 0)
//                .category((long) 3)
                .product(item)
                .created(LocalDateTime.now())
                .build();
        postRepository.save(review);
    }

}