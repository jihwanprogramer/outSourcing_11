package com.example.outsourcing_11.domain.store.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.config.security.CustomUserDetails;
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
	@PreAuthorize("hasRole('사장님')")
	public ResponseEntity<StoreResponseDto> createStore(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody StoreRequestDto requestDto) {

		StoreResponseDto response = storeService.createStore(userDetails.getUser(), requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<StoreListDto>> getStores(
		@RequestParam(value = "keyword", required = false) String keyword) {
		List<StoreListDto> stores = storeService.getStores(keyword);
		return ResponseEntity.ok(stores);
	}

	@GetMapping("/{storeId}")
	public StoreDetailDto getStoreDetail(@PathVariable Long storeId) {
		return storeService.getStoreDetail(storeId);
	}

	@PreAuthorize("hasRole('사장님')")
	@GetMapping("/my")
	public ResponseEntity<List<StoreResponseDto>> getMyStores(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<StoreResponseDto> stores = storeService.getMyStores(userDetails.getUser());
		return ResponseEntity.ok(stores);
	}

	@PreAuthorize("hasRole('사장님')")
	@PutMapping("/{storeId}")
	public ResponseEntity<String> updateStore(
		@PathVariable Long storeId,
		@RequestBody StoreRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.updateStore(storeId, dto, userDetails.getUser());
		return ResponseEntity.ok("가게 정보를 수정하였습니다.");
	}

	@PreAuthorize("hasRole('사장님')")
	@DeleteMapping("/{storeId}")
	public ResponseEntity<String> deleteStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.deleteStore(storeId, userDetails.getUser());
		return ResponseEntity.ok("가게를 폐업하였습니다.");
	}

	@PostMapping("/{storeId}/favorite")
	public ResponseEntity<String> addFavorite(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.addFavorite(storeId, userDetails.getUser());
		return ResponseEntity.ok("즐겨찾기가 등록되었습니다.");
	}

	@DeleteMapping("/{storeId}/favorite")
	public ResponseEntity<String> removeFavorite(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.removeFavorite(storeId, userDetails.getUser());
		return ResponseEntity.ok("즐겨찾기가 해제되었습니다.");
	}

	@PreAuthorize("hasRole('사장님')")
	@PostMapping("/{storeId}/notice")
	public ResponseEntity<String> createNotice(
		@PathVariable Long storeId,
		@RequestBody String content,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.createNotice(storeId, content, userDetails.getUser());
		return ResponseEntity.ok("공지사항을 등록하였습니다.");
	}

	@PreAuthorize("hasRole('사장님')")
	@PutMapping("/{storeId}/notice/{noticeId}")
	public ResponseEntity<String> updateNotice(@PathVariable Long storeId,
		@PathVariable Long noticeId,
		@RequestBody String content,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.updateNotice(storeId, noticeId, content, userDetails.getUser());
		return ResponseEntity.ok("공지사항을 수정하였습니다.");
	}

	@PreAuthorize("hasRole('사장님')")
	@DeleteMapping("/{storeId}/notice/{noticeId}")
	public ResponseEntity<String> deleteNotice(
		@PathVariable Long storeId,
		@PathVariable Long noticeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		storeService.deleteNotice(storeId, noticeId, userDetails.getUser());
		return ResponseEntity.ok("공지사항을 삭제하였습니다.");
	}

	@PreAuthorize("hasRole('사장님')")
	@GetMapping("/{storeId}/sales")
	public ResponseEntity<SalesDto> getSales(
		@PathVariable Long storeId,
		@RequestParam("type") String type,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		SalesDto sales = storeService.getSales(storeId, userDetails.getUser(), type);
		return ResponseEntity.ok(sales);
	}

}
