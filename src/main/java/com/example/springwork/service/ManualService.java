package com.example.springwork.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.springwork.entity.Manual;
import com.example.springwork.form.ManualEditForm;
import com.example.springwork.form.ManualRegisterForm;
import com.example.springwork.repository.ManualRepository;


@Service
public class ManualService {
	private final ManualRepository manualRepository;
	
	public ManualService(ManualRepository manualRepository) {
		this.manualRepository = manualRepository;
	}
	
	// すべてのマニュアルをページングされた状態で取得する
	public Page<Manual> findAllManuals(Pageable pageable) {
		return manualRepository.findAll(pageable);
	}
	
	// 指定したidを取得する
	public Optional<Manual> findManualById(Integer id) {
		return manualRepository.findById(id);
	}
	
	// 指定したキーワードがマニュアル名に含むマニュアルをページングされた状態で取得する
	public Page<Manual> findManualByNameLike(String keyword, Pageable pageable) {
		return manualRepository.findByNameLike("%" +keyword + "%", pageable);
	}
	
	// マニュアル登録
	@Transactional
	public void createManual(ManualRegisterForm manualRegisterForm) {
		Manual manual = new Manual();
		MultipartFile imageFile = manualRegisterForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			
			manual.setImageName(hashedImageName);
		}
		
		manual.setName(manualRegisterForm.getName());
		
		manualRepository.save(manual);
		
	}
	
	// マニュアル編集
	@Transactional
	public void updateManual(ManualEditForm manualEditForm,
							  Manual manual)
    {
		MultipartFile imageFile = manualEditForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			manual.setImageName(hashedImageName);
		}
		
		manual.setName(manualEditForm.getName());
		
		manualRepository.save(manual);
	}
	
	// マニュアル削除
	@Transactional
	public void deleteManual(Manual manual) {
		manualRepository.delete(manual);
	}
	
	// UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");

        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();
        }

        String hashedFileName = String.join(".", fileNames);

        return hashedFileName;
    }
	
	// 画像ファイルを指定したファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
