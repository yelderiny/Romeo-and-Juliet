package com.play;

import javafx.util.Pair;

import java.net.InetAddress;
import java.net.ServerSocket;

public class Juliet extends Actor {

    public Juliet(double initialLove) {
        current = initialLove;
        constant = 0.01;

        try {
            ownServerSocket = new ServerSocket(7779, 0, InetAddress.getByName("localhost"));

            System.out.println("Juliet: Good pilgrim, you do wrong your hand too much, ...");

        } catch (Exception e) {
            System.out.println("Juliet: Failed to create own socket " + e);
        }
    }

    //Get acquaintance with lover;
    // Receives lover's socket information and share's own socket
    public Pair<InetAddress, Integer> getAcquaintance() {
        System.out.println("""
                       Juliet: My bounty is as boundless as the sea,
                       My love as deep; the more I give to thee,
                       The more I have, for both are infinite.""");

        return getAcquaintanceHelper();
    }


    //Retrieves the lover's love
    protected double receiveLoveLetter() {
        double letter = receiveLoveLetterHelper();
        System.out.println("Juliet: Romeo, Romeo! Wherefore art thou Romeo? (<-" + letter + ")");
        return letter;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    protected void renovateLove(double partnerLove) {
        System.out.println(""" 
                       Juliet: Come, gentle night, come, loving black-browed night,
                       Give me my Romeo, and when I shall die,
                       Take him and cut him out in little stars.""");
        current = current + (-constant * partnerLove);
    }


    //Communicate love back to playwriter
    protected void declareLove() {
        System.out.println("Good night, good night! Parting is such sweet sorrow,\nThat I shall say good night till it be morrow.");
        declareLoveHelper();
    }

    @Override
    public void run() {
        super.run();

        if (this.isInterrupted()) {
            System.out.println("""
                    Juliet: I will kiss thy lips.
                    Haply some poison yet doth hang on them
                    To make me die with a restorative.""");
        }
    }
}
