package com.choreTracker.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.choreTracker.models.Job;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

	// this method retrieves all the books from the database
	// Mondatory
	List<Job> findAll();

	// Optional
	// this method finds books with descriptions containing the search string
	List<Job> findByTitleContaining(String search);
}