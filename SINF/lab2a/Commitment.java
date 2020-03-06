package com.sergio;

import java.security.SecureRandom;

public class Commitment {//this class holds the commitment value calculation, which is common between Committer and Recipient

    public static int commit(byte[] s, int r, boolean b){//this function calculates the commitment value
        SecureRandom rand = new SecureRandom(s);
        if(b)
            return rand.nextInt()^r;
        else
            return rand.nextInt();
    }
}
