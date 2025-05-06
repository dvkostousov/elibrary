package elib.mapper;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import elib.dto.NewWriterDto;
import elib.dto.WriterDto;
import elib.jdbc.entity.Book;
import elib.jdbc.entity.Writer;

public class JdbcWriterMapper {
	public static WriterDto toWriterDto(Writer writer, Iterable<Book> books) {
		if (books == null) {
	        books = new ArrayList<>();
	    }
		return new WriterDto(writer.getId(), writer.getFullName(), writer.getBio(), StreamSupport.stream(books.spliterator(), false)
				.map(b -> JdbcBookMapper.toWritersBookDto(b)).toList());
	}
	
	public static Writer toWriter(NewWriterDto writer) {
		return new Writer(null, writer.getFullName(), writer.getBio());
	}
}
