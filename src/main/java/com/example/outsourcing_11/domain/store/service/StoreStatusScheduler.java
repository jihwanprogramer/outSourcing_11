package com.example.outsourcing_11.domain.store.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.outsourcing_11.domain.store.entity.Store;
import com.example.outsourcing_11.domain.store.repository.StoreRepository;

@Component
public class StoreStatusScheduler {

	private final StoreRepository storeRepository;

	public StoreStatusScheduler(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	@Scheduled(cron = "0 0 * * * *")
	public void updateStoreStatuses() {
		List<Store> stores = storeRepository.findAll();
		LocalDateTime now = LocalDateTime.now();

		for (Store store : stores) {
			LocalTime openTime = store.getOpenTime();
			LocalTime closeTime = store.getCloseTime();

			store.updateStatus();
		}
		storeRepository.saveAll(stores);
	}
}
