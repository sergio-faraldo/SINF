package com.sergio;

import java.security.SecureRandom;

public class Committer {
    //These are the values that the committer is going to need to know to generate the commitment and to then verify it
    private byte[] s;
    private boolean b, liar;//liar modifies the behaviour of the commiter so that it always lies

    private SecureRandom rand;//Committer's PRG

    public Committer() {
        rand =new SecureRandom();
    }

    public void setLiar(boolean liar) {//set to true if the committer is going to lie
        this.liar = liar;
    }

    public boolean isLiar() {
        return liar;
    }

    public int commit(int r){//generate the commitment value, first step the committer makes (the value r is generated by the recipient)
        s= rand.generateSeed(20);//choose s
        b= rand.nextBoolean();//choose b, the bit that is going to be committed
        return Commitment.commit(s,r,b);//return the commitment value
    }

    public byte[] getS() {
        return s;
    }//return the seed chosen at random, part of the verification process

    public boolean getB() {//return the bit commited, part of the verification process
        if(liar) return !b;//if the commiter is a liar, the opposite bit is going to be sent
        return b;
    }
}
