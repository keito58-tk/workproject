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

import com.example.springwork.entity.Summarize;
import com.example.springwork.form.SummarizeEditForm;
import com.example.springwork.form.SummarizeRegisterForm;
import com.example.springwork.service.ManualService;
import com.example.springwork.service.ProductService;
import com.example.springwork.service.SummarizeService;

@Controller
@RequestMapping("/summarize")
public class SummarizeController {
	private final SummarizeService summarizeService;
	private final ProductService productService;
	private final ManualService manualService;
	
	public SummarizeController(SummarizeService summarizeService, 
							   ProductService productService,
							   ManualService manualService) {
		this.summarizeService = summarizeService;
		this.productService = productService;
		this.manualService = manualService;
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
		Page<Summarize> summarizePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			summarizePage = summarizeService.findSummarizeByNameLike(keyword, pageable);
		} else {
			summarizePage = summarizeService.findAllSummarizes(pageable);
		}
		
		model.addAttribute("summarizePage", summarizePage);
		model.addAttribute("keyword",keyword);
		
		return "summarize/index";
	}
	
	
	// 詳細ページの表示
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
			) {
		Optional<Summarize> optionalSummarize = summarizeService.findSummarizeById(id);
		
		if (optionalSummarize.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "まとめが存在しません");
			
			return "redirect:/summarize";
		}
		
		Summarize summarize = optionalSummarize.get();
		model.addAttribute("summarize",summarize);
		
		return "summarize/show";
	}
	
	// 登録ページ
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("summarizeRegisterForm", new SummarizeRegisterForm());
		model.addAttribute("productList", productService.findAllProducts(Pageable.unpaged()));
		model.addAttribute("manualList", manualService.findAllManuals(Pageable.unpaged()));
		return "summarize/register";
	}
	
	// 作成ページ
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated SummarizeRegisterForm summarizeRegisterForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			model.addAttribute("summarizeRegisterForm",summarizeRegisterForm);
			// エラー時にもリストを再設定する
			model.addAttribute("productList", productService.findAllProducts(Pageable.unpaged()));
			model.addAttribute("manualList", manualService.findAllManuals(Pageable.unpaged()));
			return "summarize/register";
		}
		
		summarizeService.createSummarize(summarizeRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "まとめを登録しました。");
		
		return "redirect:/summarize";
	}
	
	// 編集ページ
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
					   ) 
	{
		Optional<Summarize> optionalSummarize =summarizeService.findSummarizeById(id);
		
		if (optionalSummarize.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "まとめが存在しません。");
			
			return "redirect:/summarize";
		}
		
		Summarize summarize = optionalSummarize.get();
		SummarizeEditForm summarizeEditForm = new SummarizeEditForm(summarize.getName(), null, null, null);
		
		model.addAttribute("summarize", summarize);
		model.addAttribute("summarizeEditForm", summarizeEditForm);
		
		return "summarize/edit";
	}
	
	// 更新
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated SummarizeEditForm summarizeEditForm,
						 BindingResult bindingResult,
						 @PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes,
						 Model model) {
		Optional<Summarize> optionalSummarize = summarizeService.findSummarizeById(id);
		
		if (optionalSummarize.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "まとめが存在しません。");
			
			return "redirect:/summarize";
		}
		
		Summarize summarize = optionalSummarize.get();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("summarize", summarize);
			model.addAttribute("summarizeEditForm", summarizeEditForm);
			
			return "summarize/edit";
		}
		
		summarizeService.updateSummarize(summarizeEditForm, summarize);
		redirectAttributes.addFlashAttribute("successMessage", "まとめ情報を編集しました。");
		
		return "redirect:/summarize";
	}
	
	// 削除処理
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes
						 ) {
		Optional<Summarize> optionalSummarize = summarizeService.findSummarizeById(id);
		
		if (optionalSummarize.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "まとめが存在しません");
			
			return "redirect:/summarize";
		}
		
		Summarize summarize = optionalSummarize.get();
		summarizeService.deleteSummarize(summarize);
		redirectAttributes.addFlashAttribute("successMessage", "まとめを削除しました。");
		
		return "redirect:/summarize";
	}
	
	
	
	
	
}
