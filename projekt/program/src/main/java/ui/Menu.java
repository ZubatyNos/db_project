package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Rastislav Urbanek
 */
public abstract class Menu {

    private boolean exit;

    public void run() throws IOException {
        exit = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println();
        print();
        System.out.println();

        while (exit == false) {
            System.out.println();
            System.out.print("> ");
            String line = br.readLine();
            if (line == null) {
                return;
            }

            handle(line);

        }
    }

    public void exit() {
        exit = true;
    }

    public abstract void print();

    public abstract void handle(String option);	
}