package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;


import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Field {
    @Autowired
    ServletContext servletContext;
    private Puzzle[][] puzzles;
    private int rowCount;
    private int columnCount;
    private Gamestate gamestate;
    private NullPuzzle nullPuzzle;
    private long startMillis;
    public static final String GAME_NAME = "pictureslidingpuzzle";
    private Difficulty difficulty;
    private boolean alreadyShuffled;
    private BufferedImage[][] imgs;
    private int height;
    private int width;
    public Field(int rowCount, int columnCount) throws IOException, URISyntaxException {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.puzzles = new Puzzle[rowCount][columnCount];
        generateImages();
        generate();
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public boolean isSolved() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (puzzles[row][column] != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(row);
                    sb.append(column);
                    if (!puzzles[row][column].imgValue().equals(sb.toString())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Puzzle[][] getPuzzles() {
        return this.puzzles;
    }

    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }

    private void generateImages() throws IOException {
        ImageSpliter imageSpliter = new ImageSpliter();
        ImageIcon icon = null;
        BufferedImage bi;
        try {
            icon = new ImageIcon(new URL("https://www.memerewards.com/images/2018/10/24/NOT_SURE_IF_YOURE_JOKING_OR_ACTUALLY_SERIOUS_15403602678276f6f1b10bd33c.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        if (icon != null) {
            icon.paintIcon(null, bi.getGraphics(), 0, 0);
        }
        //URL url = new URL("http://iislab.kpi.fei.tuke.sk/static/images/icons/tuke.png");
        //BufferedImage bi = ImageIO.read(url/*new File("tuke.png")*/);
        Image img = bi.getScaledInstance(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        imgs = imageSpliter.getImages(bi, rowCount, columnCount);
        this.height = imageSpliter.getHeight();
        this.width = imageSpliter.getWidth();
    }

    private void generate() {
        alreadyShuffled = false;
        //char value = 'A';
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                //puzzles[row][column] = new Puzzle(value);
                //value++;
                puzzles[row][column] = new Puzzle(imgs[row][column],Integer.toString(row)+column);
                puzzles[row][column].setHigh(this.height);
                puzzles[row][column].setWidth(this.width);
            }
        }
        gamestate = Gamestate.PLAYING;

        nullPuzzle = new NullPuzzle(rowCount - 1, columnCount - 1,puzzles[rowCount-1][columnCount-1].getBufferedImage());
        puzzles[rowCount - 1][columnCount - 1] = null;
        startMillis = System.currentTimeMillis();
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public NullPuzzle getNullPuzzle() {
        return nullPuzzle;
    }

    public Gamestate getState() {
        return this.gamestate;
    }

    public void setState(Gamestate gamestate) {
        this.gamestate = gamestate;
    }


    public void shuffle(int nOfShuffles) {
        for (int curShuffle = 0; curShuffle < nOfShuffles; curShuffle++) {
            for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    if (puzzles[row][column] == null) {
                        int shuf = (int) (Math.random() * 4);
                        switch (shuf) {
                            case 0:
                                move("UP");
                                break;
                            case 1:
                                move("DOWN");
                                break;
                            case 2:
                                move("LEFT");
                                break;
                            case 3:
                                move("RIGHT");
                                break;
                        }
                    }
                }
            }
        }
        if (this.isSolved()) {
            shuffle(nOfShuffles);
        }
    }


    public void setAlreadyShuffled() {
        this.alreadyShuffled = true;
    }

    public void moveWeb(String input) {
        int row, column;
        row = Character.getNumericValue(input.charAt(0));
        column = Character.getNumericValue(input.charAt(1));
        int divRow = this.getNullPuzzle().getRow() - row;
        int divCol = this.getNullPuzzle().getColumn() - column;
        if (divCol == 0 && divRow == 0) {
            System.out.println("Wrong input");
            return;
        }
        if (divRow <= 1 && divRow >= (-1) && divCol <= 1 && divCol >= (-1)) {
            if (divCol == 1 && divRow == 0) {
                move("RIGHT");
            }
            if (divCol == (-1) && divRow == 0) {
                move("LEFT");
            }
            if (divCol == 0 && divRow == 1) {
                move("DOWN");
            }
            if (divCol == 0 && divRow == (-1)) {
                move("UP");
            }
        }
        if (divCol >= 2 && divRow == 0) {
            if (divCol == 2) {
                multipleMove("RIGHT");
            }
            if (divCol == 3) {
                tripleMove("RIGHT");
            }
            if (divCol == 4) {
                quadMove("RIGHT");
            }
        }
        if (divCol <= (-2) && divRow == 0) {
            if (divCol == -2) {
                multipleMove("LEFT");
            }
            if (divCol == -3) {
                tripleMove("LEFT");

            }
            if (divCol == -4) {
                quadMove("LEFT");
            }
        }
        if (divCol == 0 && divRow >= 2) {
            if (divRow == 2) {
                multipleMove("DOWN");
            }
            if (divRow == 3) {
                tripleMove("DOWN");
            }
            if (divRow == 4) {
                quadMove("DOWN");
            }
        }
        if (divCol == 0 && divRow <= (-2)) {
            if (divRow == -2) {
                multipleMove("UP");
            }
            if (divRow == -3) {
                tripleMove("UP");
            }
            if (divRow == -4) {
                quadMove("UP");
            }

        }
    }

    public void tripleMove(String input) {
        if(input.equalsIgnoreCase("UP")) {
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 3][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() + 3][this.getNullPuzzle().getColumn()] = null;
            nullPuzzle.setRow(this.getNullPuzzle().getRow() + 3);
            nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
        }
        if(input.equalsIgnoreCase("DOWN")) {
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 3][this.getNullPuzzle().getColumn()];
            puzzles[this.getNullPuzzle().getRow() - 3][this.getNullPuzzle().getColumn()] = null;
            nullPuzzle.setRow(this.getNullPuzzle().getRow() - 3);
            nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
        }
        if(input.equalsIgnoreCase("RIGHT")) {
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 3];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 3] = null;
            nullPuzzle.setRow(this.getNullPuzzle().getRow());
            nullPuzzle.setColumn(this.getNullPuzzle().getColumn() - 3);
        }
        if(input.equalsIgnoreCase("LEFT")) {
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 3];
            puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 3] = null;
            nullPuzzle.setRow(this.getNullPuzzle().getRow());
            nullPuzzle.setColumn(this.getNullPuzzle().getColumn() + 3);
        }
    }


    public void quadMove(String input) {
        if (input.equalsIgnoreCase("UP")) {
            if (puzzles[this.getNullPuzzle().getRow() + 4][this.getNullPuzzle().getColumn()] != null) {
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 3][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() + 3][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 4][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() + 4][this.getNullPuzzle().getColumn()] = null;
                nullPuzzle.setRow(this.getNullPuzzle().getRow() + 4);
                nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
            }
        }
        if (input.equalsIgnoreCase("DOWN")) {
            if (puzzles[this.getNullPuzzle().getRow() - 4][this.getNullPuzzle().getColumn()] != null) {
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 3][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() - 3][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 4][this.getNullPuzzle().getColumn()];
                puzzles[this.getNullPuzzle().getRow() - 4][this.getNullPuzzle().getColumn()] = null;
                nullPuzzle.setRow(this.getNullPuzzle().getRow() - 4);
                nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
            }
        }
        if (input.equalsIgnoreCase("RIGHT")) {
            if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 4] != null) {
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 3];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 3] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 4];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 4] = null;
                nullPuzzle.setRow(this.getNullPuzzle().getRow());
                nullPuzzle.setColumn(this.getNullPuzzle().getColumn() - 4);
            }
        }
        if (input.equalsIgnoreCase("LEFT")) {
            if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 4] != null) {
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 3];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 3] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 4];
                puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 4] = null;
                nullPuzzle.setRow(this.getNullPuzzle().getRow());
                nullPuzzle.setColumn(this.getNullPuzzle().getColumn() + 4);
            }
        }
    }


    public void multipleMove(String input) {
        if (input.equalsIgnoreCase("UP")) {
            if ((this.getNullPuzzle().getRow() + 2) < rowCount) {
                if (puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() + 2][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow() + 2);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("DOWN")) {
            if ((this.getNullPuzzle().getRow() - 2) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() - 2][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow() - 2);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("RIGHT")) {
            if ((this.getNullPuzzle().getColumn() - 2) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 2] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() - 2);
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("LEFT")) {
            if ((this.getNullPuzzle().getColumn() + 2) < columnCount) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 2] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() + 2);
                } else {
                    if (alreadyShuffled) {
                        System.out.println("Move is note possible.");
                    }
                }
            } else {
                System.out.println("Wrong input, try again.");
            }
        }
    }

    public void move(String input) {
        if (input.equalsIgnoreCase("UP")) {
            if ((this.getNullPuzzle().getRow() + 1) < rowCount) {
                if (puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow() + 1);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("DOWN")) {
            if ((this.getNullPuzzle().getRow() - 1) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow() - 1);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("RIGHT")) {
            if ((this.getNullPuzzle().getColumn() - 1) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() - 1);
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else if (input.equalsIgnoreCase("LEFT")) {
            if ((this.getNullPuzzle().getColumn() + 1) < columnCount) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() + 1);
                }
            } else {
                if (alreadyShuffled) {
                    System.out.println("Move is note possible.");
                }
            }
        } else {
            System.out.println("Wrong input, try again.");
        }
    }

    public int getScore() {
        if (getDifficulty().equals(Difficulty.EASY)) {
            return Math.max(0, rowCount * columnCount * 10 - getPlayingTime());
        }
        if (getDifficulty().equals(Difficulty.MEDIUM)) {
            return Math.max(0, rowCount * columnCount * 10 * 2 - getPlayingTime());
        } else {
            return Math.max(0, rowCount * columnCount * 10 * 4 - getPlayingTime());
        }
    }


    public void render() {
        System.out.println();
        for (Puzzle[] p : puzzles) {
            for (Puzzle c : p) {
                if (c != null) {
                    System.out.print(c);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}