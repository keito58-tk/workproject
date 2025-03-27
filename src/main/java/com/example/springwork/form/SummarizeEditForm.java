package com.example.springwork.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummarizeEditForm {
	@NotBlank(message = "まとめ名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
}
