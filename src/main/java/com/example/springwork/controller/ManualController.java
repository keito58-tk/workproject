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

import com.example.springwork.entity.Manual;
import com.example.springwork.form.ManualEditForm;
import com.example.springwork.form.ManualRegisterForm;
import com.example.springwork.service.ManualService;

@Controller
@RequestMapping("/manual")
public class ManualController {
	private final ManualService manualService;
	
	public ManualController(ManualService manualService) {
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
		Page<Manual> manualPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			manualPage = manualService.findManualByNameLike(keyword, pageable);
		} else {
			manualPage = manualService.findAllManuals(pageable);
		}
		
		model.addAttribute("manualPage", manualPage);
		model.addAttribute("keyword",keyword);
		
		return "manual/index";
	}
	
	
	// 詳細ページの表示
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
			) {
		Optional<Manual> optionalManual = manualService.findManualById(id);
		
		if (optionalManual.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "マニュアルが存在しません");
			
			return "redirect:/manual";
		}
		
		Manual manual = optionalManual.get();
		model.addAttribute("manual",manual);
		
		return "manual/show";
	}
	
	// 登録ページ
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("manualRegisterForm", new ManualRegisterForm());
		
		return "manual/register";
	}
	
	// 作成ページ
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated ManualRegisterForm manualRegisterForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			model.addAttribute("manualRegisterForm",manualRegisterForm);
			
			return "/manual/register";
		}
		
		manualService.createManual(manualRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "マニュアルを登録しました。");
		
		return "redirect:/manual";
	}
	
	// 編集ページ
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id,
					   RedirectAttributes redirectAttributes,
					   Model model
					   ) 
	{
		Optional<Manual> optionalManual =manualService.findManualById(id);
		
		if (optionalManual.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "マニュアルが存在しません。");
			
			return "redirect:/manual";
		}
		
		Manual manual = optionalManual.get();
		ManualEditForm manualEditForm = new ManualEditForm(manual.getName(), null);
		
		model.addAttribute("manual", manual);
		model.addAttribute("manualEditForm", manualEditForm);
		
		return "manual/edit";
	}
	
	// 更新
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated ManualEditForm manualEditForm,
						 BindingResult bindingResult,
						 @PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes,
						 Model model) {
		Optional<Manual> optionalManual = manualService.findManualById(id);
		
		if (optionalManual.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "マニュアルが存在しません。");
			
			return "redirect;/manual";
		}
		
		Manual manual = optionalManual.get();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("manual", manual);
			model.addAttribute("manualEditForm", manualEditForm);
			
			return "manual/edit";
		}
		
		manualService.updateManual(manualEditForm, manual);
		redirectAttributes.addFlashAttribute("successMessage", "マニュアル情報を編集しました。");
		
		return "redirect:/manual";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes
						 ) {
		Optional<Manual> optionalManual = manualService.findManualById(id);
		
		if (optionalManual.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "マニュアルが存在しません");
			
			return "redirect:/manual";
		}
		
		Manual manual = optionalManual.get();
		manualService.deleteManual(manual);
		redirectAttributes.addFlashAttribute("successMessage", "マニュアルを削除しました。");
		
		return "redirect:/manual";
	}
	
	
	
	
	
}
