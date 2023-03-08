package com.play;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Actor extends Thread {
    protected ServerSocket ownServerSocket = null;
    protected Socket serviceMailbox = null;
    protected double current = 0;
    protected double constant = 0;

    protected abstract double receiveLoveLetter();
    protected abstract void renovateLove(double val);
    protected abstract void declareLove();
    public abstract Pair<InetAddress, Integer> getAcquaintance();

    protected Pair<InetAddress, Integer> getAcquaintanceHelper() {
        return new Pair<>(ownServerSocket.getInetAddress(), ownServerSocket.getLocalPort());
    }

    protected double receiveLoveLetterHelper() {
        double val = 0.0;

        try {
            serviceMailbox = ownServerSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(serviceMailbox.getInputStream()));
            String message = reader.readLine();
            val = Double.parseDouble(message.substring(0, message.length() - 1));

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return val;
    }

    protected void declareLoveHelper() {
        try {
            PrintWriter writer = new PrintWriter(serviceMailbox.getOutputStream(), true);
            writer.println(current + "A");

            serviceMailbox.close();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (!this.isInterrupted()) {
                double RomeoLove = receiveLoveLetter();
                renovateLove(RomeoLove);
                declareLove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
