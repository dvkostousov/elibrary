package elib.controller;

import java.util.Optional;

import java.util.stream.StreamSupport;

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

import elib.dto.ErrorResponseDto;
import elib.dto.NewWriterDto;
import elib.dto.UpdateWriterDto;
import elib.jdbc.entity.Writer;
import elib.jdbc.repository.BookRepository;
import elib.jdbc.repository.WriterRepository;
import elib.mapper.JdbcWriterMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Писатели", description = "Управление писателями.")
@RestController
@RequestMapping(path = "/writers", produces = "application/json")
public class WriterController {
	private WriterRepository writerRepo;
	private BookRepository bookRepo;

	public WriterController(WriterRepository writerRepo, BookRepository bookRepo) {
		this.writerRepo = writerRepo;
		this.bookRepo = bookRepo;
	}
	
	@Operation(summary = "Получить всех писателей")
	@GetMapping
	public ResponseEntity<Iterable<Writer>> getWriters() {
		return ResponseEntity.ok(writerRepo.findAll());
	}
	
	@Operation(summary = "Получить конкретного писателя по его id")
	@GetMapping("/{id}")
	public ResponseEntity<?> getWriterById(@PathVariable("id") Long id) {
		Optional<Writer> writer = writerRepo.findById(id);
		if (writer.isPresent()) {
			return ResponseEntity.ok(JdbcWriterMapper.toWriterDto(writer.get(), bookRepo.findByWriterId(writer.get().getId())));
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Writer not found with id = " + id));
	}
	
	@Operation(summary = "Добавить нового писателя")
	@PostMapping(consumes="application/json")
	public ResponseEntity<Writer> postWriter(@Valid @RequestBody NewWriterDto newWriter) {
		Writer writer = JdbcWriterMapper.toWriter(newWriter);
		Writer savedWriter = writerRepo.save(writer);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedWriter);
	}
	
	@Operation(summary = "Обновить данные для конкретного писателя по его id")
	@PatchMapping(path="/{id}", consumes="application/json")
	public ResponseEntity<?> patchWriter(@Valid @RequestBody UpdateWriterDto patch, @PathVariable("id") Long id){
		Optional<Writer> optOldWriter = writerRepo.findById(id);
		if (!optOldWriter.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Writer not found with id = " + id));
		}
		Writer writer = optOldWriter.get();
		
		if (patch.getFullName() != null) {
			writer.setFullName(patch.getFullName());
		}
		
		if (patch.getBio() != null) {
			writer.setBio(patch.getBio());
		}
		
		
		return ResponseEntity.ok(writerRepo.update(writer));
	}
	
	@Operation(summary = "Удалить конкретного писателя по его id. Удаление невозможно при наличии книг у писателя")
	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> deleteWriter(@PathVariable("id") Long id) {
		if (writerRepo.findById(id).isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Writer not found with id = " + id));
		}
		if (StreamSupport.stream(bookRepo.findByWriterId(id).spliterator(), false).findAny().isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(new ErrorResponseDto("Writer with id " + id + " cannot be deleted because he has books."));
		}
		writerRepo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
