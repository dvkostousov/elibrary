package elib.mapper;

import java.time.LocalDate;

import elib.dto.BookOrderDto;
import elib.dto.NewBookOrderDto;
import elib.dto.ReadersBookOrderDto;
import elib.jpa.entity.Book;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;

public class JpaBookOrderMapper {
	public static ReadersBookOrderDto toReadersBookOrderDto(BookOrder bookOrder) {
		return new ReadersBookOrderDto(bookOrder.getId(), bookOrder.getBook().getId(), bookOrder.getStartDate(), bookOrder.getEndDate());
	}
	
	public static BookOrderDto toBookOrderDto(BookOrder bookOrder) {
		return new BookOrderDto(bookOrder.getId(), bookOrder.getReader().getId(), bookOrder.getReader().getFullName(), 
				bookOrder.getBook().getId(), bookOrder.getBook().getName(), bookOrder.getStartDate(), bookOrder.getEndDate());
	}
	
	public static BookOrder toBookOrder(NewBookOrderDto bookOrderDto, Book book, Reader reader) {
		return new BookOrder(null, reader, book, LocalDate.now(), bookOrderDto.getEndDate());
	}
}
