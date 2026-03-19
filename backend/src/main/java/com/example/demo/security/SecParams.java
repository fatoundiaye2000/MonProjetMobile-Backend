// src/main/java/com/example/demo/security/SecParams.java
package com.example.demo.security;

public interface SecParams {
    public static final long EXP_TIME = 10 * 24 * 60 * 60 * 1000;
    public static final String SECRET = "exampleb@yahoo.com";
    
    // ⭐⭐⭐ AJOUTER CETTE CONSTANTE ⭐⭐⭐
    public static final String PREFIX = "Bearer ";
}