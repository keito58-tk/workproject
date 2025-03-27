package com.example.springwork.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithUserDetails("山田 太郎")
	public void ログイン済みの場合は製品一覧ページが正しく表示される() throws Exception {
		mockMvc.perform(get("/product"))
						.andExpect(status().isOk())
						.andExpect(view().name("product/index"));
	}
	
	@Test
    public void 未ログインの場合は製品一覧ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/product"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
	
	
}
