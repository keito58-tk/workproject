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
	
	public SummarizeService(SummarizeRepository summarizeRepository) {
		this.summarizeRepository = summarizeRepository;
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
		
		summarizeRepository.save(summarize);
		
	}
	
	// まとめ編集
	@Transactional
	public void updateSummarize(SummarizeEditForm summarizeEditForm,
							  Summarize summarize)
    {
		
		
		summarize.setName(summarizeEditForm.getName());
		
		summarizeRepository.save(summarize);
	}
	
	// まとめ削除
	@Transactional
	public void deleteSummarize(Summarize summarize) {
		summarizeRepository.delete(summarize);
	}
}
