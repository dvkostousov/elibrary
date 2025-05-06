package elib.jdbc.repository;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import elib.jdbc.entity.Book;

@Repository
public class JdbcBookRepository implements BookRepository{
	private JdbcTemplate jdbcTemplate;

	public JdbcBookRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Iterable<Book> findAll() {
		return jdbcTemplate.query("select id, writer_id, name, description, total_copies, borrow_copies, genre from Book", this::mapRowToBook);
	}

	@Override
	public Optional<Book> findById(Long id) {
		List<Book> results = jdbcTemplate.query("select id, writer_id, name, description, total_copies, borrow_copies, genre from Book where id=?",
				this::mapRowToBook, id);
		return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
	}
	
	@Override
	public Iterable<Book> findByWriterId(Long writerId) {
		return jdbcTemplate.query("select id, writer_id, name, description, total_copies, borrow_copies, genre from Book where writer_id=?", this::mapRowToBook, writerId);
	}

	@Override
	public Book save(Book book) {
		String sql = "insert into Book (writer_id, name, description, total_copies, borrow_copies, genre) VALUES (?, ?, ?, ?, ?, ?)";

	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, book.getWriterId());
	        ps.setString(2, book.getName());
	        ps.setString(3, book.getDescription());
	        ps.setInt(4, book.getTotalCopies());
	        ps.setInt(5, book.getBorrowCopies());
	        ps.setString(6, book.getGenre().name());
	        return ps;
	    }, keyHolder);

	    Long generatedId = keyHolder.getKey().longValue();
	    book.setId(generatedId);

	    return book;
	}
	
	@Override
	public Book update(Book book) {
		jdbcTemplate.update("UPDATE Book SET name = ?, description = ?, total_copies = ?, genre = ? WHERE id = ?", book.getName(), book.getDescription(), book.getTotalCopies(),
				book.getGenre().name(), book.getId());
	    return book;
	}
	
	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM Book WHERE id = ?", id);
	}

	private Book mapRowToBook(ResultSet row, int rowNum) throws SQLException {
		return new Book(row.getLong("id"), row.getLong("writer_id"), row.getString("name"), row.getString("description"), row.getInt("total_copies"), row.getInt("borrow_copies"),
				Book.Genre.valueOf(row.getString("genre")));
	}
}
