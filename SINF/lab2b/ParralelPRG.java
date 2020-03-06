package com.Sergio;

import java.security.SecureRandom;
import java.util.ArrayList;

/*
This implementation of a Parallel Pseudorandom Generator uses only one instance of the basic PRG
and an array of seeds instead of an array of PRGs to save memory. The seeds are static arrays of bytes.

This implementation generates only pseudorandom integers.
 */

public class ParralelPRG {
    private ArrayList<byte[]> seeds;//Array of seeds
    private int i;//keeps track of how many times nextInt has been called

    public ParralelPRG(ArrayList<byte[]> seeds) {//initialize sending an ArrayList of seeds
        this.seeds = seeds;
        i=0;
    }

    public ParralelPRG(int n){//initialize sending the number of seeds
        SecureRandom R = new SecureRandom();
        for (int i = 0; i < n; i++) {
            seeds.add(R.generateSeed(20));
        }
        i=0;
    }

    public int nextInt(){
        if((seeds == null) || seeds.isEmpty()){//in case the PRG hasn't been initialized correctly, inform user and fall back to basic secure PRG
            SecureRandom R = new SecureRandom();
            System.out.println("PRG seed not initialized correctly");
            return R.nextInt();
        }
        SecureRandom R = new SecureRandom(seeds.get(i%seeds.size()));//Initialize the PRG with the current seed
        for (int j = 0; j < i/seeds.size(); j++) {//skip the already used pseudorandom numbers
            R.nextInt();
        }
        i++;
        return R.nextInt();//return the random int
    }
}