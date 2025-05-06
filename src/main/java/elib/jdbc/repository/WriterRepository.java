package elib.jdbc.repository;

import java.util.Optional;


import elib.jdbc.entity.Writer;

public interface WriterRepository {
	Iterable<Writer> findAll();
	
	Optional<Writer> findById(Long id);
	
	Writer save(Writer writer);
	
	Writer update(Writer writer);
	
	void deleteById(Long id);
}
