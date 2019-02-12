package com.lambda.dogs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepo extends JpaRepository<Dog, Long>
{
    /*
    List<Dog> findByApt(boolean apt);
    List<Dog> findAllByBreed();
    List<Dog> findAllByAvgWeight();
    Dog findByBreedIgnoreCase(String breed);

     */
    List<Dog> findByBreed(String breed);
    List<Dog> findAllByOrderByBreedAsc();
    List<Dog> findAllByOrderByAvgWeightAsc();
    List<Dog> findByAptEquals(Boolean bool);
    Dog deleteByBreed(String breed);
}
