package elib.jdbc.repository;

import org.springframework.stereotype.Repository;


import elib.jdbc.entity.Writer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class JdbcWriterRepository implements WriterRepository {
	private JdbcTemplate jdbcTemplate;

	public JdbcWriterRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Iterable<Writer> findAll() {
		return jdbcTemplate.query("select id, full_name, bio from Writer", this::mapRowToWriter);
	}

	@Override
	public Optional<Writer> findById(Long id) {
		List<Writer> results = jdbcTemplate.query("select id, full_name, bio from Writer where id=?",
				this::mapRowToWriter, id);
		return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
	}

	@Override
	public Writer save(Writer writer) {
		String sql = "insert into Writer (full_name, bio) VALUES (?, ?)";

	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, writer.getFullName());
	        ps.setString(2, writer.getBio());
	        return ps;
	    }, keyHolder);

	    Long generatedId = keyHolder.getKey().longValue();
	    writer.setId(generatedId);

	    return writer;
	}
	
	@Override
	public Writer update(Writer writer) {
		jdbcTemplate.update("UPDATE Writer SET full_name = ?, bio = ? WHERE id = ?", writer.getFullName(), writer.getBio(), writer.getId());
	    return writer;
	}
	
	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM Writer WHERE id = ?", id);
	}

	private Writer mapRowToWriter(ResultSet row, int rowNum) throws SQLException {
		return new Writer(row.getLong("id"), row.getString("full_name"), row.getString("bio"));
	}
}
