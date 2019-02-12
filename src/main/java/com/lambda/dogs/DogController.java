package com.lambda.dogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DogController
{
    private final DogRepo dogrepo;
    private final DogResourceAssembler assembler;

    public DogController(DogRepo dogrepo, DogResourceAssembler assembler)
    {
        this.dogrepo = dogrepo;
        this.assembler = assembler;
    }
    // Get("/dogs")
    @GetMapping("/dogs/breeds/{breed}")
    // ("/breeds/{breed}") - just that breed
     public Resources<Resource<Dog>>  findBreed(@PathVariable String breed)
    {
        List<Resource<Dog>> dogs = dogrepo.findByBreed(breed).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).findBreed(breed)).withSelfRel());
    }

    //("/apartment") - all dogs suitable for apt
    @GetMapping("/apartment")
    public Resources<Resource<Dog>> findApt()
    {
        List<Resource<Dog>> dogs = dogrepo.findByAptEquals(true).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).findApt()).withSelfRel());
    }


    //("/breeds") - list of all dogs alphabetically by breed
    @GetMapping("/dogs/breeds")
    public Resources<Resource<Dog>> allByBreed()
    {
        List<Resource<Dog>> dogs = dogrepo.findAllByOrderByBreedAsc().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allByBreed()).withSelfRel());
    }

    //("/weight") - list of all dogs ordered by avg weight
    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> allByWeight()
    {
        List<Resource<Dog>> dogs = dogrepo.findAllByOrderByAvgWeightAsc().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allByWeight()).withSelfRel());
    }

    // Put("/dogs")
    @PutMapping("/dogs/{id}")
    //(/{id{}) - add or update if already present the dog with id value id
    public ResponseEntity<?> replaceDog(@RequestBody Dog newDog, @PathVariable Long id)
        throws URISyntaxException
    {
        Dog updatedDog = dogrepo.findById(id)
                .map(dog ->
                {
                    dog.setBreed(newDog.getBreed());
                    dog.setApt(newDog.isApt());
                    dog.setAvgWeight(newDog.getAvgWeight());
                    return dogrepo.save(dog);
                })
                .orElseGet(() ->
                {
                    newDog.setId(id);
                    return dogrepo.save(newDog);
                });

        Resource<Dog> resource = assembler.toResource(updatedDog);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // Post("/dogs")
    @PostMapping
    // (/dogs) - adds the dog
    public void addDog(@RequestBody Dog newDog, @PathVariable Long id)
        throws URISyntaxException
    {
        newDog.setId(id);
        // Do I need to save this???
    }

    //Delete("/dogs")
    //(/{id}) - delete dog with that id
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id)
    {
        dogrepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //(/{breed}) - delete that breed
    @DeleteMapping("/dogs/{breed}")
    public ResponseEntity<?> deleteBreed(@PathVariable String breed)
    {
        dogrepo.deleteByBreed(breed);
        return ResponseEntity.noContent().build();
    }
}
