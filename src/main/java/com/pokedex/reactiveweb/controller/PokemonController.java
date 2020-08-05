package com.pokedex.reactiveweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokedex.reactiveweb.model.Pokemon;
import com.pokedex.reactiveweb.repository.PokemonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

	private PokemonRepository repository;

	public PokemonController(PokemonRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping
	public Flux<Pokemon> getPokemons(){
		return repository.findAll();
	}
	
	@GetMapping("{id}")
	public Mono<ResponseEntity<Pokemon>> getPokemonById(@PathVariable String id){
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<Pokemon> insertPokemon(@RequestBody Pokemon obj){
		return repository.save(obj);
	}
	
	@PutMapping("{id}")
	public Mono<ResponseEntity<Pokemon>> updatePokemon(@RequestBody Pokemon obj, @PathVariable String id){
		return repository.findById(id)
				.flatMap(existingPokemon -> {
					existingPokemon.setName(obj.getName());
					existingPokemon.setCategory(obj.getCategory());
					existingPokemon.setSkill(obj.getSkill());
					existingPokemon.setWeight(obj.getWeight());
					return repository.save(existingPokemon);
				})
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("{id}")
	public Mono<ResponseEntity<Void>> deletePokemonById(@PathVariable String id){
		return repository.findById(id)
				.flatMap(existingPokemon ->
				repository.delete(existingPokemon)
				.then(Mono.just(ResponseEntity.ok().<Void>build()))
				)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping
	public Mono<Void> deleteAllPokemons(){
		return repository.deleteAll();
	}
}
