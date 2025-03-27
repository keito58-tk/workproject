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

import com.example.springwork.entity.Product;
import com.example.springwork.form.ProductEditForm;
import com.example.springwork.form.ProductRegisterForm;
import com.example.springwork.repository.ProductRepository;


@Service
public class ProductService {
	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	// すべての製品をページングされた状態で取得する
	public Page<Product> findAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
	
	// 指定したidを取得する
	public Optional<Product> findProductById(Integer id) {
		return productRepository.findById(id);
	}
	
	// 指定したキーワードが製品名に含む製品をページングされた状態で取得する
	public Page<Product> findProductByNameLike(String keyword, Pageable pageable) {
		return productRepository.findByNameLike("%" +keyword + "%", pageable);
	}
	
	// 製品登録
	@Transactional
	public void createProduct(ProductRegisterForm productRegisterForm) {
		Product product = new Product();
		MultipartFile imageFile = productRegisterForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			
			product.setImageName(hashedImageName);
		}
		
		product.setName(productRegisterForm.getName());
		
		productRepository.save(product);
		
	}
	
	// 製品編集
	@Transactional
	public void updateProduct(ProductEditForm productEditForm,
							  Product product)
    {
		MultipartFile imageFile = productEditForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			product.setImageName(hashedImageName);
		}
		
		product.setName(productEditForm.getName());
		
		productRepository.save(product);
	}
	
	// 製品削除
	@Transactional
	public void deleteProduct(Product product) {
		productRepository.delete(product);
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
