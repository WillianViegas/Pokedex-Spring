package com.pokedex.reactiveweb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pokedex.reactiveweb.model.Pokemon;

public interface PokemonRepository extends ReactiveMongoRepository<Pokemon, String>{

}
