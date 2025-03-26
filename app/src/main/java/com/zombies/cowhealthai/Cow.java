package com.zombies.cowhealthai;

public class Cow {
    private String name, breed, weight, age, milk, imageUri;

    public Cow(String name, String breed, String weight, String age, String milk, String imageUri) {
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.age = age;
        this.milk = milk;
        this.imageUri = imageUri;
    }

    // Getters
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public String getWeight() { return weight; }
    public String getAge() { return age; }
    public String getMilk() { return milk; }
    public String getImageUri() { return imageUri; }

    // Setters (for updating values)
    public void setName(String name) { this.name = name; }
    public void setBreed(String breed) { this.breed = breed; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setAge(String age) { this.age = age; }
    public void setMilk(String milk) { this.milk = milk; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
