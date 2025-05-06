package elib.controller;

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
import elib.mapper.JpaBookOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import elib.dto.BookOrderDto;
import elib.dto.ErrorResponseDto;
import elib.dto.NewBookOrderDto;
import elib.dto.UpdateBookOrderDto;
import elib.jpa.entity.Book;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;

@Tag(name = "Заказы книг", description = "Управление заказами. Каждый заказ принадлежит одному читателю и содержит ровну одну книгу")
@RestController
@RequestMapping(path = "/orders", produces = "application/json")
public class BookOrderController {
	private BookOrderRepository bookOrderRepo;
	private BookRepository bookRepo;
	private ReaderRepository readerRepo;
	
	public BookOrderController(BookOrderRepository bookOrderRepo, BookRepository bookRepo, ReaderRepository readerRepo) {
		this.bookOrderRepo = bookOrderRepo;
		this.bookRepo = bookRepo;
		this.readerRepo= readerRepo;
		
	}
	
	@Operation(summary = "Получить все заказы")
	@GetMapping
	public ResponseEntity<List<BookOrderDto>> getBookOrders() {
		return ResponseEntity.ok(bookOrderRepo.findAll().stream().map(b -> JpaBookOrderMapper.toBookOrderDto(b)).toList());
	}
	
	@Operation(summary = "Получить конкретный заказ по его id")
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable("id") Integer id) {
		Optional<BookOrder> bookOrder = bookOrderRepo.findById(id);
		if (bookOrder.isPresent()) {
			return ResponseEntity.ok(JpaBookOrderMapper.toBookOrderDto(bookOrder.get()));
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Order not found with id = " + id));
	}
	
	@Operation(summary = "Добавить новый заказ. Добавление невозможно при отсуствии книги из заказа в наличии")
	@PostMapping(consumes="application/json")
	public ResponseEntity<?> postReader(@Valid @RequestBody NewBookOrderDto newBookOrder) {
		Optional<Book> optBook = bookRepo.findById(newBookOrder.getBookId()); 
		if (optBook.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Book not found with id = " + newBookOrder.getBookId()));
		}
		Optional<Reader> optReader = readerRepo.findById(newBookOrder.getReaderId());
		if (optReader.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Reader not found with id = " + newBookOrder.getReaderId()));
		}
		
		Book book = optBook.get();
		
		if (!bookRepo.checkAvailable(book.getId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(new ErrorResponseDto("There is no any available books with id " + newBookOrder.getBookId() + "."));
		}
		
		bookRepo.addBorrowCopies(book.getId(), 1);
		BookOrder bookOrder = JpaBookOrderMapper.toBookOrder(newBookOrder, book, optReader.get());
		return ResponseEntity.status(HttpStatus.CREATED).body(JpaBookOrderMapper.toBookOrderDto(bookOrderRepo.save(bookOrder)));
	}
	
	@Operation(summary = "Обновить данные для конкретного заказа по его id")
	@PatchMapping(path="/{id}", consumes="application/json")
	public ResponseEntity<?> patchBookOrder(@Valid @RequestBody UpdateBookOrderDto patch, @PathVariable("id") Integer id){
		Optional<BookOrder> optOldBookOrder = bookOrderRepo.findById(id);
		if (optOldBookOrder.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Order not found with id = " + id));
		}
		BookOrder bookOrder = optOldBookOrder.get();
		
		if (patch.getEndDate() != null) {
			bookOrder.setEndDate(patch.getEndDate());
		}
		
		return ResponseEntity.ok(bookOrderRepo.save(bookOrder));
	}
	
	@Operation(summary = "Удалить конкретный заказ по его id")
	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> deleteBookOrder(@PathVariable("id") Integer id) {
		Optional<BookOrder> optBookOrder = bookOrderRepo.findById(id);
		if (optBookOrder.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Order not found with id = " + id));
		}
		
		BookOrder bookOrder = optBookOrder.get();
		bookRepo.substractBorrowCopies(bookOrder.getBook().getId(), 1);
		
		bookOrderRepo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
