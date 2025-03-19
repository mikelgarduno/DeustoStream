package com.example.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapi.model.Series;

public interface SerieRepository extends JpaRepository<Series, Long> {
    
}