package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {
		
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
		return new MovieDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> findAll(Pageable pageable) {
		Page<Movie> page = repository.findAll(pageable);
		return page.map(x -> new MovieDTO(x));
	}
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> findByGenre(Long genreId, Pageable pageable) {
		Optional<Genre> obj = genreRepository.findById(genreId);
		if(obj.isEmpty() && genreId != 0) {
			throw new ResourceNotFoundException("Entity not found");
		}
		Genre entity = obj.orElse(null);
		Page<Movie> page = repository.findByGenre(entity, pageable);
		return page.map(x -> new MovieDTO(x));
	}

	public List<ReviewDTO> reviews(Long id, Pageable pageable) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
		List<Review> list = reviewRepository.reviews(entity, pageable);
		return list.stream().map(x -> new ReviewDTO(x)).collect(Collectors.toList());
	}
}
