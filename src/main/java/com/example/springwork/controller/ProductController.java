package com.example.springwork.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springwork.entity.Product;
import com.example.springwork.form.ProductEditForm;
import com.example.springwork.form.ProductRegisterForm;
import com.example.springwork.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	// 一覧ページの表示
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@PageableDefault(page = 0,
										 size = 10,
										 sort = "id",
										 direction = Direction.ASC) 
						Pageable pageable, 
						Model model) {
		Page<Product> productPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			productPage = productService.findProductByNameLike(keyword, pageable);
		} else {
			productPage = productService.findAllProducts(pageable);
		}
		
		model.addAttribute("productPage", productPage);
		model.addAttribute("keyword",keyword);
		
		return "product/index";
	}
	
	
	// 詳細ページの表示
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
			) {
		Optional<Product> optionalProduct = productService.findProductById(id);
		
		if (optionalProduct.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "製品が存在しません");
			
			return "redirect:/product";
		}
		
		Product product = optionalProduct.get();
		model.addAttribute("product",product);
		
		return "product/show";
	}
	
	// 登録ページ
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("productRegisterForm", new ProductRegisterForm());
		
		return "product/register";
	}
	
	// 作成ページ
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated ProductRegisterForm productRegisterForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			model.addAttribute("productRegisterForm",productRegisterForm);
			
			return "/product/register";
		}
		
		productService.createProduct(productRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "製品を登録しました。");
		
		return "redirect:/product";
	}
	
	// 編集ページ
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
					   ) 
	{
		Optional<Product> optionalProduct =productService.findProductById(id);
		
		if (optionalProduct.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "製品が存在しません。");
			
			return "redirect:/product";
		}
		
		Product product = optionalProduct.get();
		ProductEditForm productEditForm = new ProductEditForm(product.getName(), null);
		
		model.addAttribute("product", product);
		model.addAttribute("productEditForm", productEditForm);
		
		return "product/edit";
	}
	
	// 更新
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated ProductEditForm productEditForm,
						 BindingResult bindingResult,
						 @PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes,
						 Model model) {
		Optional<Product> optionalProduct = productService.findProductById(id);
		
		if (optionalProduct.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "製品が存在しません。");
			
			return "redirect;/product";
		}
		
		Product product = optionalProduct.get();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("product", product);
			model.addAttribute("productEditForm", productEditForm);
			
			return "product/edit";
		}
		
		productService.updateProduct(productEditForm, product);
		redirectAttributes.addFlashAttribute("successMessage", "製品情報を編集しました。");
		
		return "redirect:/product";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes
						 ) {
		Optional<Product> optionalProduct = productService.findProductById(id);
		
		if (optionalProduct.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "製品が存在しません");
			
			return "redirect:/product";
		}
		
		Product product = optionalProduct.get();
		productService.deleteProduct(product);
		redirectAttributes.addFlashAttribute("successMessage", "製品を削除しました。");
		
		return "redirect:/product";
	}
	
	
	
	
	
}
