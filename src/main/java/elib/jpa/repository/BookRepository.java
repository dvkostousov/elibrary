package elib.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import elib.jpa.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	
	@Query("SELECT (b.totalCopies - b.borrowCopies) > 0 FROM Book b WHERE b.id = :bookId")
	public boolean checkAvailable(@Param("bookId") Integer bookId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Book b SET b.borrowCopies = b.borrowCopies + COALESCE(:count, 0) WHERE b.id = :bookId")
	public void addBorrowCopies(@Param("bookId") Integer bookId, @Param("count") int count);
	
	@Transactional
	@Modifying
	@Query("UPDATE Book b SET b.borrowCopies = b.borrowCopies - COALESCE(:count, 0) WHERE b.id = :bookId")
	public void substractBorrowCopies(@Param("bookId") Integer bookId, @Param("count") int count);
}
