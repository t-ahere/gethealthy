package com.gethealthy.gethealthy.mypage;

import com.gethealthy.gethealthy.WithAccount;
import com.gethealthy.gethealthy.account.AccountRepository;
import com.gethealthy.gethealthy.account.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }


    @WithAccount("dongkyun")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception{
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(get(MyPageController.MYPAGE_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("dongkyun")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(post(MyPageController.MYPAGE_PROFILE_URL)
                .param("bio",bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MyPageController.MYPAGE_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));
        Account dongkyun = accountRepository.findByNickname("dongkyun");
        assertEquals(bio, dongkyun.getBio());
    }


    @WithAccount("dongkyun")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_with_error() throws Exception{
        String bio = "길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우";
        mockMvc.perform(post(MyPageController.MYPAGE_PROFILE_URL)
                .param("bio",bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(MyPageController.MYPAGE_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account dongkyun = accountRepository.findByNickname("dongkyun");
        assertNull(dongkyun.getBio());
    }

    @WithAccount("dongkyun")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception{
        mockMvc.perform(get(MyPageController.MYPAGE_PASSWORD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("dongkyun")
    @DisplayName("패스워드 수정하기 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception{
        mockMvc.perform(post(MyPageController.MYPAGE_PASSWORD_URL)
                .param("newPassword","123412341234")
                .param("newPasswordConfirm","123412341234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MyPageController.MYPAGE_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account dongkyun = accountRepository.findByNickname("dongkyun");
        assertTrue(passwordEncoder.matches("123412341234",dongkyun.getPassword()));
    }
    @WithAccount("dongkyun")
    @DisplayName("패스워드 수정하기 - 입력값 불일치")
    @Test
    void updatePassword_fail() throws Exception{
        mockMvc.perform(post(MyPageController.MYPAGE_PASSWORD_URL)
                .param("newPassword","123412341234")
                .param("newPasswordConfirm","121111111114")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(MyPageController.MYPAGE_PASSWORD_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));

    }





}