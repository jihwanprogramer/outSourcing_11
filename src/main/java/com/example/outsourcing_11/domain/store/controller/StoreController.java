package com.example.outsourcing_11.domain.store.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.domain.store.dto.SalesDto;
import com.example.outsourcing_11.domain.store.dto.StoreDetailDto;
import com.example.outsourcing_11.domain.store.dto.StoreListDto;
import com.example.outsourcing_11.domain.store.dto.StoreRequestDto;
import com.example.outsourcing_11.domain.store.dto.StoreResponseDto;
import com.example.outsourcing_11.domain.store.service.StoreService;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<StoreResponseDto> createStore(
		@RequestHeader("Authorization") String token,
		@RequestBody StoreRequestDto requestDto) {
		StoreResponseDto response = storeService.createStore(token, requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<StoreListDto>> getStores(
		@RequestParam(value = "keyword", required = false) String keyword) {
		List<StoreListDto> stores = storeService.getStores(keyword);
		return ResponseEntity.ok(stores);
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<StoreDetailDto> getStoreDetail(@PathVariable Long storeId) {
		StoreDetailDto detail = storeService.getStoreDetail(storeId);
		return ResponseEntity.ok(detail);
	}

	@GetMapping("/my")
	public ResponseEntity<List<StoreResponseDto>> getMyStores(
		@RequestHeader("Authorization") String token) {
		List<StoreResponseDto> stores = storeService.getMyStores(token);
		return ResponseEntity.ok(stores);
	}

	@PutMapping("/{storeId}")
	public ResponseEntity<Void> updateStore(
		@PathVariable Long storeId,
		@RequestBody StoreRequestDto requestDto,
		@RequestHeader("Authorization") String token) {
		storeService.updateStore(storeId, requestDto, token);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{storeId}")
	public ResponseEntity<Void> deleteStore(
		@PathVariable Long storeId,
		@RequestHeader("Authorization") String token) {
		storeService.deleteStore(storeId, token);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{storeId}/favorite")
	public ResponseEntity<Void> addFavorite(
		@PathVariable Long storeId,
		@RequestHeader("Authorization") String token) {
		storeService.addFavorite(storeId, token);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{storeId}/favorite")
	public ResponseEntity<Void> removeFavorite(
		@PathVariable Long storeId,
		@RequestHeader("Authorization") String token) {
		storeService.removeFavorite(storeId, token);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{storeId}/notice")
	public ResponseEntity<Void> createNotice(
		@PathVariable Long storeId,
		@RequestBody String content,
		@RequestHeader("Authorization") String token) {
		storeService.createNotice(storeId, content, token);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/notice/{noticeId}")
	public ResponseEntity<Void> updateNotice(
		@PathVariable Long noticeId,
		@RequestBody String content,
		@RequestHeader("Authorization") String token) {
		storeService.updateNotice(noticeId, content, token);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/notice/{noticeId}")
	public ResponseEntity<Void> deleteNotice(
		@PathVariable Long noticeId,
		@RequestHeader("Authorization") String token) {
		storeService.deleteNotice(noticeId, token);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{storeId}/sales")
	public ResponseEntity<SalesDto> getSales(
		@PathVariable Long storeId,
		@RequestParam("type") String type,
		@RequestHeader("Authorization") String token) {
		SalesDto sales = storeService.getSales(storeId, token, type);
		return ResponseEntity.ok(sales);
	}

}
