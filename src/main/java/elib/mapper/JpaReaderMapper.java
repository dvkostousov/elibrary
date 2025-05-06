package elib.mapper;

import java.util.ArrayList;

import elib.dto.NewReaderDto;
import elib.dto.ReaderDto;
import elib.dto.ReaderInListDto;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;

public class JpaReaderMapper {
	public static ReaderDto toReaderDto(Reader reader) {
		return new ReaderDto(reader.getId(), reader.getFullName(), reader.getAddress(), reader.getContact(), reader.getBookOrders().stream().map(x -> JpaBookOrderMapper.toReadersBookOrderDto(x)).toList());
	}
	
	public static Reader toReader(NewReaderDto reader) {
		return new Reader(null, reader.getFullName(), reader.getAddress(), reader.getContact(), new ArrayList<BookOrder>());
	}
	
	public static ReaderInListDto toReaderInListDto(Reader reader) {
		return new ReaderInListDto(reader.getId(), reader.getFullName(), reader.getAddress(), reader.getContact());
	}
}
