package com.lambda.dogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Dog
{
    private @Id @GeneratedValue Long id;
    private String breed;
    private int avgWeight;
    private boolean apt;

    public Dog()
    {
        // default constructor
    }

    public Dog(String breed, int avgWeight, boolean apt)
    {
        this.breed = breed;
        this.avgWeight = avgWeight;
        this.apt = apt;
    }
}
