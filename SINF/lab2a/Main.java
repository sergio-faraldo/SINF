package com.sergio;

public class Main {

    public static void main(String[] args) {
        Recipient alice = new Recipient();
        Committer bob = new Committer();
        bob.setLiar(false);//choose if Bob is a liar or not

        System.out.println("Alice - Generating r...");
        int r=alice.genR();//Alice generates a random r value
        System.out.println("Bob - Commiting bit...");
        int c=bob.commit(r);//Bob is sent the value and commits a bit b (represented by a boolean)
        System.out.println("Bob - Commitment made");

        alice.setC(c);//Alice stores Bob's commitment value
        System.out.println("Alice - Commitment received");

        //Bob sends alice the bit b and the value s, so she can check his commitment
        alice.setB(bob.getB());
        alice.setS(bob.getS());

        //Alice checks if Bob commited that value
        System.out.println("Alice - Checking commitment...");
        if(alice.checkCommit()) {
            System.out.print("Alice - Bob commited the value ");
            if (bob.getB()) System.out.println("1");
            else System.out.println("0");
        } else
            System.out.println("Alice - Bob lied!");
    }
}
