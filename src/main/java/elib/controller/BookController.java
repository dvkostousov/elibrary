package elib.controller;

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

import elib.dto.ErrorResponseDto;
import elib.dto.NewBookDto;
import elib.dto.UpdateBookDto;
import elib.jdbc.entity.Book;
import elib.jdbc.repository.BookRepository;
import elib.jdbc.repository.WriterRepository;
import elib.mapper.JdbcBookMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Книги", description = "Управление книгами.")
@RestController
@RequestMapping(path = "/books", produces = "application/json")
public class BookController {
	private BookRepository bookRepo;
	private WriterRepository writerRepo;

	public BookController(BookRepository bookRepo, WriterRepository writerRepo) {
		this.bookRepo = bookRepo;
		this.writerRepo = writerRepo;
	}
	
	@Operation(summary = "Получить все книги")
	@GetMapping
	public ResponseEntity<Iterable<Book>> getBooks() {
		return ResponseEntity.ok(bookRepo.findAll());
	}
	
	@Operation(summary = "Получить конкретную кнгиу по ее id")
	@GetMapping("/{id}")
	public ResponseEntity<?> getBookById(@PathVariable("id") Long id) {
		Optional<Book> book = bookRepo.findById(id);
		if (book.isPresent()) {
			return ResponseEntity.ok(JdbcBookMapper.toBookDto(book.get(), writerRepo.findById(book.get().getWriterId()).get().getFullName()));
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Book not found with id = " + id));
	}
	
	@Operation(summary = "Добавить новую книгу")
	@PostMapping(consumes="application/json")
	public ResponseEntity<?> postBook(@Valid @RequestBody NewBookDto newBook) {
		if (writerRepo.findById(newBook.getWriterId()).isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Book's writer not found with id = " + newBook.getWriterId()));
		}
		
		Book book = JdbcBookMapper.toBook(newBook);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookRepo.save(book));
	}
	
	@Operation(summary = "Обновить данные для конкретной книги по ее id")
	@PatchMapping(path="/{id}", consumes="application/json")
	public ResponseEntity<?> patchBook(@Valid @RequestBody UpdateBookDto patch, @PathVariable("id") Long id){
		Optional<Book> optOldBook= bookRepo.findById(id);
		if (optOldBook.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Book not found with id = " + id));
		}
		Book book = optOldBook.get();
		
		if (patch.getName() != null) {
			book.setName(patch.getName());
		}
		
		if (patch.getDescription() != null) {
			book.setDescription(patch.getDescription());
		}
		
		if (patch.getTotalCopies() != null) {
			book.setTotalCopies(patch.getTotalCopies());
		}
		
		if (patch.getGenre() != null) {
			book.setGenre(patch.getGenre());
		}
		
		
		return ResponseEntity.ok(bookRepo.update(book));
	}
	
	@Operation(summary = "Удалить конкретную книгу по ее id. Удаление невозможно при наличии заказов с данной книгой")
	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
		Optional<Book> optBook = bookRepo.findById(id);
		if (optBook.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Book not found with id = " + id));
		}
		if (optBook.get().getBorrowCopies() > 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(new ErrorResponseDto("Book with id " + id + " cannot be deleted because it is currently borrowed by readers"));
		}
		bookRepo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
