package com.play;

import java.net.ServerSocket;
import java.net.InetAddress;

import javafx.util.Pair;

public class Romeo extends Actor {

    public Romeo(double initialLove) {
        current = initialLove;
        constant = 0.02;

        try {
            ownServerSocket = new ServerSocket(7778, 0, InetAddress.getByName("localhost"));

            System.out.println("Romeo: What lady is that, which doth enrich the hand\nOf yonder knight?");

        } catch (Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
    }

    //Get acquaintance with lover;
    public Pair<InetAddress, Integer> getAcquaintance() {
        System.out.println("Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");

        return getAcquaintanceHelper();
    }

    //Retrieves the lover's love
    protected double receiveLoveLetter() {
        double letter = receiveLoveLetterHelper();
        System.out.println("Romeo: O sweet Juliet... (<-" + letter + ")");
        return letter;
    }

    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    protected void renovateLove(double partnerLove) {
        System.out.println("Romeo: But soft, what light through yonder window breaks?\nIt is the east, and Juliet is the sun.");
        current = current + (constant * partnerLove);
    }

    //Communicate love back to playwriter
    protected void declareLove() {
        System.out.println("I would I were thy bird");
        declareLoveHelper();
    }

    //Execution
    @Override
    public void run() {
        super.run();

        if (this.isInterrupted()) {
            System.out.println("Romeo: Here's to my love. O true apothecary,\n" +
                    "Thy drugs are quick. Thus with a kiss I die.");
        }
    }
}
