package com.example.springwork.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductEditForm {
	@NotBlank(message = "製品名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
}
