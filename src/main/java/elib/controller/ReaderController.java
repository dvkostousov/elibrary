package elib.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elib.jpa.repository.BookOrderRepository;
import elib.jpa.repository.BookRepository;
import elib.jpa.repository.ReaderRepository;
import elib.mapper.JpaReaderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import elib.dto.ErrorResponseDto;
import elib.dto.NewReaderDto;
import elib.dto.ReaderInListDto;
import elib.dto.UpdateReaderDto;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;

import jakarta.validation.Valid;

@Tag(name = "Читатели", description = "Управление читателями.")
@RestController
@RequestMapping(path = "/readers", produces = "application/json")
public class ReaderController {
	private ReaderRepository readerRepo;
	private BookOrderRepository bookOrderRepo;
	private BookRepository bookRepo;
	
	public ReaderController(ReaderRepository readerRepo, BookOrderRepository bookOrderRepo, BookRepository bookRepo) {
		this.readerRepo = readerRepo;
		this.bookOrderRepo = bookOrderRepo;
		this.bookRepo = bookRepo;
	}
	
	@Operation(summary = "Получить всех читателей")
	@GetMapping
	public ResponseEntity<List<ReaderInListDto>> getReaders() {
		return ResponseEntity.ok(readerRepo.findAll().stream().map(r -> JpaReaderMapper.toReaderInListDto(r)).toList());
	}
	
	@Operation(summary = "Получить конкретного читателя по его id")
	@GetMapping("/{id}")
	public ResponseEntity<?> getReaderById(@PathVariable("id") Integer id) {
		Optional<Reader> reader = readerRepo.findById(id);
		if (reader.isPresent()) {
			return ResponseEntity.ok(JpaReaderMapper.toReaderDto(reader.get()));
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Reader not found with id = " + id));
	}
	
	@Operation(summary = "Добавить нового читателя")
	@PostMapping(consumes="application/json")
	public ResponseEntity<?> postReader(@Valid @RequestBody NewReaderDto newReader) {
		Reader reader = JpaReaderMapper.toReader(newReader);
		return ResponseEntity.status(HttpStatus.CREATED).body(JpaReaderMapper.toReaderDto(readerRepo.save(reader)));
	}
	
	@Operation(summary = "Обновить данные для конкретного читателя по его id")
	@PatchMapping(path="/{id}", consumes="application/json")
	public ResponseEntity<?> patchReader(@Valid @RequestBody UpdateReaderDto patch, @PathVariable("id") Integer id){
		Optional<Reader> optOldReader = readerRepo.findById(id);
		if (optOldReader.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Reader not found with id = " + id));
		}
		Reader reader = optOldReader.get();
		
		if (patch.getFullName() != null) {
			reader.setFullName(patch.getFullName());
		}
		
		if (patch.getAddress() != null) {
			reader.setAddress(patch.getAddress());
		}
		
		if (patch.getContact() != null) {
			reader.setContact(patch.getContact());
		}
		
		
		return ResponseEntity.ok(JpaReaderMapper.toReaderDto(readerRepo.save(reader)));
	}
	
	@Operation(summary = "Удалить конкретного читателя по его id. Удаление невозможно при наличии активных (с неистекшим сроком сдачи) заказов")
	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> deleteReader(@PathVariable("id") Integer id) {
		Optional<Reader> optReader = readerRepo.findById(id);
		if (optReader.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Reader not found with id = " + id));
		}
		if (bookOrderRepo.existsByReaderIdAndEndDateBefore(id, LocalDate.now())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(new ErrorResponseDto("Reader with id " + id + " cannot be deleted because he has active orders."));
		}
		
		Reader reader = optReader.get();
		for (BookOrder o: reader.getBookOrders()) {
			bookRepo.substractBorrowCopies(o.getBook().getId(), 1);
		}
		readerRepo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
