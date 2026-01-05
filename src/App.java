import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import processing.core.*;

public class App extends PApplet {
    ArrayList<bubble> bubbles;
    int scene;
    double timer;
    double highScore;
    double gameStart;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    public void setup() {
        readHighScore();
        bubbles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            bubbleMaker();
        }
        scene = 0;
        gameStart = millis();
    }

    public void settings() {
        size(1200, 800);
    }

    public void draw() {
        background(0);
        if (scene == 0) {
            for (bubble b : bubbles) {
                b.display();
                b.update();
            }
            fill(255);
            textSize(50);
            timer = millis() - gameStart;
            timer = ((int) timer / 100) / 10.0;
            text("" + timer, width - 100, 50);
            if (bubbles.size() == 0) {
                scene = 1;
                if (highScore < timer) {
                    highScore = timer;
                    saveHighScore();
                }
            }
        } else {
            text("Score: " + timer, 400, 400);
            text("High score: " + highScore, 400, 500);
        }
    }

    public void bubbleMaker() {
        for (int i = 0; i < 1; i++) {
            int x = (int) random(1000) + 125;
            int y = (int) random(800);
            bubble bubble = new bubble(x, y, this);
            bubbles.add(bubble);
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            if (scene == 0) {
                bubbles.clear();
            } else {
                setup();
            }
            // for (int i = 0; i< 1; i++){
            // int x = (int)random(1000) + 125;
            // int y = (int)random(800);
            // bubble bubble = new bubble(x, y, this);
            // bubbles.add(bubble);
            // }
        }
    }

    public void mousePressed() {
        for (int i = 0; i < bubbles.size(); i++) {
            bubble b = bubbles.get(i);
            if (b.checkTouch(mouseX, mouseY) == false) {
                bubbles.remove(b);
            }
        }
    }

    public void saveHighScore() {
        try (PrintWriter writer = new PrintWriter("highscore.txt")) {
            writer.println(highScore); // Writes the integer to the file
            writer.close(); // Closes the writer and saves the file
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void readHighScore() {
        try (Scanner scanner = new Scanner(Paths.get("highscore.txt"))) {

            // we read the file until all lines have been read
            while (scanner.hasNextLine()) {
                // we read one line
                String row = scanner.nextLine();
                highScore = Double.valueOf(row);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
