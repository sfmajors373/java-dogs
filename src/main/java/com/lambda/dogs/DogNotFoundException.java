package com.lambda.dogs;

public class DogNotFoundException extends RuntimeException
{
    public DogNotFoundException(Long id)
    {
        super("Could not find dog" + id);
    }
    public DogNotFoundException(String breed)
    {
        super("Could not find dog" + breed);
    }
}
