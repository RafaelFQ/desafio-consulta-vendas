package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

import static java.time.ZoneId.systemDefault;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public ResponseEntity<Page<SaleReportDTO>> getReport(String minDate, String maxDate, String name, Pageable pageable){

		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = (maxDate == null || maxDate.equals("")) ? today : LocalDate.parse(maxDate);
		LocalDate min = (minDate == null || minDate.equals("")) ? max.minusYears(1) : LocalDate.parse(minDate);
		name = (name == null) ? "" : name;

		Page<SaleReportDTO> result = repository.searchSales(min, max, name, pageable);

		return ResponseEntity.ok(result);
	}

	public ResponseEntity<List<SaleSummaryDTO>> getSummary(String minDate, String maxDate){

		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = (maxDate == null || maxDate.equals("")) ? today : LocalDate.parse(maxDate);
		LocalDate min = (minDate == null || minDate.equals("")) ? max.minusYears(1) : LocalDate.parse(minDate);

		List<SaleSummaryDTO> result = repository.summarySales(min, max);

		return ResponseEntity.ok(result);
	}

}
