package com.example.springwork.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummarizeEditForm {
	@NotBlank(message = "まとめ名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
	
	@NotNull(message = "製品名を選択してください")
	private Integer selectedProductId;
	
	@NotNull(message = "マニュアル名を選択してください")
	private Integer selectedManualId;
}
