package com.Sergio;

import java.security.SecureRandom;

/*
The seeds are static arrays of bytes.

This implementation generates only pseudorandom integers.
 */

public class BlumMicaliPRG {
    private SecureRandom R;

    public BlumMicaliPRG(byte[] seed) {//initialize from seed
        R=new SecureRandom(seed);
    }

    public BlumMicaliPRG(){//initialize from random seed
        R=new SecureRandom();
    }

	//Instead of generating a longer number and splitting it in two pieces to obtain the output and the new seed, I genereate first the output and then the seed.
	//Blum-Micali should be done by getting a longer output from the PRG and splitting it into two, but I don't think that my implementation is any less secure.
    public int nextInt(){
        int ret = R.nextInt();//Return one integer
        R=new SecureRandom(R.generateSeed(20));//Generate with the current PRG one seed, and reinitialize the PRG with that seed
        return ret;
    }
}
