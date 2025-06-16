package com.example.springwork.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springwork.entity.Summarize;
import com.example.springwork.form.SummarizeEditForm;
import com.example.springwork.form.SummarizeRegisterForm;
import com.example.springwork.repository.SummarizeRepository;


@Service
public class SummarizeService {
	private final SummarizeRepository summarizeRepository;
	private final ProductService productService;
	private final ManualService manualService;
	
	
	public SummarizeService(SummarizeRepository summarizeRepository,
							ProductService productService,
							ManualService manualService) {
		this.summarizeRepository = summarizeRepository;
		this.productService = productService;
		this.manualService = manualService;
	}
	
	// すべてのまとめをページングされた状態で取得する
	public Page<Summarize> findAllSummarizes(Pageable pageable) {
		return summarizeRepository.findAll(pageable);
	}
	
	// 指定したidを取得する
	public Optional<Summarize> findSummarizeById(Integer id) {
		return summarizeRepository.findById(id);
	}
	
	// 指定したキーワードがまとめ名に含むまとめをページングされた状態で取得する
	public Page<Summarize> findSummarizeByNameLike(String keyword, Pageable pageable) {
		return summarizeRepository.findByNameLike("%" +keyword + "%", pageable);
	}
	
	
	
	// まとめ登録
	@Transactional
	public void createSummarize(SummarizeRegisterForm summarizeRegisterForm) {
		Summarize summarize = new Summarize();
		
		summarize.setName(summarizeRegisterForm.getName());
		
		// フォームから送られてきた productId があれば紐付け
		if (summarizeRegisterForm.getSelectedProductId() != null) {
			productService.findProductById(summarizeRegisterForm.getSelectedProductId())
						  .ifPresent(summarize :: setProduct);
		}
		
		// フォームから送られてきた manualId があれば紐付け
		if (summarizeRegisterForm.getSelectedManualId() != null) {
			manualService.findManualById(summarizeRegisterForm.getSelectedManualId())
						 .ifPresent(summarize :: setManual);
		}
		
		// ここで JPA がまとめテーブルに product_id, manual_id を含めて INSERT します
		summarizeRepository.save(summarize);
	}
	
	// まとめ編集
	@Transactional
	public void updateSummarize(SummarizeEditForm summarizeEditForm,
							  Summarize summarize) {
		
		summarize.setName(summarizeEditForm.getName());
		
		// 製品の差し替え
        if (summarizeEditForm.getSelectedProductId() != null) {
            productService.findProductById(summarizeEditForm.getSelectedProductId())
                          .ifPresent(summarize::setProduct);
        } else {
            summarize.setProduct(null);
        }
        // マニュアルの差し替え
        if (summarizeEditForm.getSelectedManualId() != null) {
            manualService.findManualById(summarizeEditForm.getSelectedManualId())
                         .ifPresent(summarize::setManual);
        } else {
            summarize.setManual(null);
        }

        summarizeRepository.save(summarize);
	}
	
	// まとめ削除
	@Transactional
	public void deleteSummarize(Summarize summarize) {
		summarizeRepository.delete(summarize);
	}
}
