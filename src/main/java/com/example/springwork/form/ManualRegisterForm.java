package com.example.springwork.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManualRegisterForm {
	@NotBlank(message = "マニュアル名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
}
