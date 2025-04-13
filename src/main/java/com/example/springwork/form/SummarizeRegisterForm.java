package com.example.springwork.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SummarizeRegisterForm {
	@NotBlank(message = "まとめ名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
	
	private MultipartFile productImageFile;
    
    private MultipartFile manualImageFile;
}
