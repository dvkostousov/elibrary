package elib.jpa.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import elib.jpa.entity.BookOrder;

public interface BookOrderRepository extends JpaRepository<BookOrder, Integer> {
	boolean existsByReaderIdAndEndDateBefore(Integer readerId, LocalDate currentDate);
}
