package com.play;

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;

import javafx.util.Pair;


public class PlayWriter {

    private Romeo myRomeo = null;
    private InetAddress RomeoAddress = null;
    private int RomeoPort = 0;
    private Socket RomeoMailbox = null;

    private Juliet myJuliet = null;
    private InetAddress JulietAddress = null;
    private int JulietPort = 0;
    private Socket JulietMailbox = null;

    double[][] theNovel;
    int novelLength;

    public PlayWriter() {
        novelLength = 500; //Number of verses
        theNovel = new double[novelLength][2];
        theNovel[0][0] = 0;
        theNovel[0][1] = 1;
    }

    //Create the lovers
    public void createCharacters() {
        //Create the lovers
        System.out.println("PlayWriter: Romeo enters the stage.");

        myRomeo = new Romeo(0);
        myRomeo.start();

        System.out.println("PlayWriter: Juliet enters the stage.");

        myJuliet = new Juliet(1);
        myJuliet.start();
    }

    //Meet the lovers and start letter communication
    public void charactersMakeAcquaintances() {

        Pair<InetAddress, Integer> romeoAcquaintance = myRomeo.getAcquaintance();
        RomeoPort = romeoAcquaintance.getValue();
        RomeoAddress = romeoAcquaintance.getKey();

        System.out.println("PlayWriter: I've made acquaintance with Romeo");

        Pair<InetAddress, Integer> julietAcquaintance = myJuliet.getAcquaintance();
        JulietPort = julietAcquaintance.getValue();
        JulietAddress = julietAcquaintance.getKey();

        System.out.println("PlayWriter: I've made acquaintance with Juliet");
    }

    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromRomeo(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Romeo. -> (" + theNovel[verse - 1][1] + ")");

        try {
            RomeoMailbox = new Socket(RomeoAddress, RomeoPort);

            String message = theNovel[verse - 1][1] + "J";
            PrintWriter writer = new PrintWriter(RomeoMailbox.getOutputStream(), true);
            writer.println(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromJuliet(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Juliet. -> (" + theNovel[verse - 1][0] + ")");

        try {
            JulietMailbox = new Socket(JulietAddress, JulietPort);

            String message = theNovel[verse - 1][0] + "R";
            PrintWriter writer = new PrintWriter(JulietMailbox.getOutputStream(), true);
            writer.println(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Receive letter from Romeo with renovated love for current verse
    public void receiveLetterFromRomeo(int verse) {
        //System.out.println("PlayWriter: Receiving letter from Romeo for verse " + verse + ".");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(RomeoMailbox.getInputStream()));
            String message = reader.readLine();

            double val = Double.parseDouble(message.substring(0, message.length() - 1));

            theNovel[verse][0] = val;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("PlayWriter: Romeo's verse " + verse + " -> " + theNovel[verse][0]);
    }

    //Receive letter from Juliet with renovated love from current verse
    public void receiveLetterFromJuliet(int verse) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(JulietMailbox.getInputStream()));
            String message = reader.readLine();

            double val = Double.parseDouble(message.substring(0, message.length() - 1));

            theNovel[verse][1] = val;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("PlayWriter: Juliet's verse " + verse + " -> " + theNovel[verse][1]);
    }

    //Let the story unfold
    public void storyClimax() {
        for (int verse = 1; verse < novelLength; verse++) {
            //Write verse
            System.out.println("PlayWriter: Writing verse " + verse + ".");

            requestVerseFromRomeo(verse);
            requestVerseFromJuliet(verse);

            receiveLetterFromRomeo(verse);
            receiveLetterFromJuliet(verse);

            System.out.println("PlayWriter: Verse " + verse + " finished.");
        }
    }

    //Character's death
    public void charactersDeath() {
        try {
            RomeoMailbox.close();
            myRomeo.interrupt();

            JulietMailbox.close();
            myJuliet.interrupt();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //A novel consists of introduction, conflict, climax and dénouement
    public void writeNovel() {
        System.out.println("PlayWriter: The Most Excellent and Lamentable Tragedy of Romeo and Juliet.");
        System.out.println("PlayWriter: A play in IV acts.");
        //Introduction,
        System.out.println("PlayWriter: Act I. Introduction.");
        this.createCharacters();
        //Conflict
        System.out.println("PlayWriter: Act II. Conflict.");
        this.charactersMakeAcquaintances();
        //Climax
        System.out.println("PlayWriter: Act III. Climax.");
        this.storyClimax();
        //Denouement
        System.out.println("PlayWriter: Act IV. Denouement.");
        this.charactersDeath();

    }

    //Dump novel to file
    public void dumpNovel() {

        FileWriter Fw = null;

        try {
            Fw = new FileWriter("RomeoAndJuliet.csv");

        } catch (IOException e) {
            System.out.println("PlayWriter: Unable to open novel file. " + e);
        }

        System.out.println("PlayWriter: Dumping novel. ");
        StringBuilder sb = new StringBuilder();

        for (int act = 0; act < novelLength; act++) {
            String tmp = theNovel[act][0] + ", " + theNovel[act][1] + "\n";
            sb.append(tmp);
            //System.out.print("PlayWriter [" + act + "]: " + tmp);
        }

        try {
            assert Fw != null;
            BufferedWriter br = new BufferedWriter(Fw);
            br.write(sb.toString());
            br.close();

        } catch (Exception e) {
            System.out.println("PlayWriter: Unable to dump novel. " + e);
        }
    }

    public static void main(String[] args) {
        PlayWriter Shakespeare = new PlayWriter();

        Shakespeare.writeNovel();
        Shakespeare.dumpNovel();

        System.exit(0);
    }
}
