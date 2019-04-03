package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;

public class Field {
    private Puzzle[][] puzzles;
    private Puzzle[][] solution;
    private int rowCount;
    private int columnCount;
    private Gamestate gamestate;
    private NullPuzzle nullPuzzle;
    private long startMillis;
    public static final String GAME_NAME = "Picture Sliding Puzzle";
    private Difficulty difficulty;
    private boolean alreadyShuffled;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.puzzles = new Puzzle[rowCount][columnCount];
        this.solution = new Puzzle[rowCount][columnCount];
        generate();
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    private Difficulty getDifficulty() {
        return this.difficulty;
    }

    public boolean isSolved() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if ((puzzles[row][column] != null) && (solution[row][column] != null)) {
                    if (puzzles[row][column].getValue() != solution[row][column].getValue()) {
                        return false;
                    }
                }
            }
        }
        return puzzles[rowCount - 1][columnCount - 1] == null;
    }

    public Puzzle[][] getPuzzles() {
        return this.puzzles;
    }

    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }

    private void generate() {
        alreadyShuffled =false;
        char value = 'A';
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                puzzles[row][column] = new Puzzle(value);
                solution[row][column] = new Puzzle(value);
                value++;
            }
        }
        gamestate = Gamestate.PLAYING;
        puzzles[rowCount - 1][columnCount - 1] = null;
        solution[rowCount - 1][columnCount - 1] = null;
        nullPuzzle = new NullPuzzle(rowCount - 1, columnCount - 1);
        startMillis = System.currentTimeMillis();
    }

    private NullPuzzle getNullPuzzle() {
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

    public void move(String input) {
        if (input.equalsIgnoreCase("UP")) {
            if ((this.getNullPuzzle().getRow() + 1) < rowCount) {
                if (puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() + 1][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow()+ 1);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            }else {
                if(alreadyShuffled){
                    System.out.println("Move is note possible.");
                }
            }
        }
        else if (input.equalsIgnoreCase("DOWN")) {
            if ((this.getNullPuzzle().getRow() - 1) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()];
                    puzzles[this.getNullPuzzle().getRow() - 1][this.getNullPuzzle().getColumn()] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow() - 1);
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn());
                }
            }else {
                if(alreadyShuffled){
                    System.out.println("Move is note possible.");
                }
            }
        }
        else if (input.equalsIgnoreCase("RIGHT")) {
            if ((this.getNullPuzzle().getColumn() - 1) >= 0) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() - 1] = null;

                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() - 1);
                }
            }else {
                if(alreadyShuffled){
                    System.out.println("Move is note possible.");
                }
            }
        }
        else if (input.equalsIgnoreCase("LEFT")) {
            if ((this.getNullPuzzle().getColumn() + 1) < columnCount) {
                if (puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] != null) {
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn()] = puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1];
                    puzzles[this.getNullPuzzle().getRow()][this.getNullPuzzle().getColumn() + 1] = null;
                    nullPuzzle.setRow(this.getNullPuzzle().getRow());
                    nullPuzzle.setColumn(this.getNullPuzzle().getColumn() + 1);
                }
            }else {
                if(alreadyShuffled){
                    System.out.println("Move is note possible.");
                }
            }
        }
        else {
            System.out.println("Wrong input, try again.");
        }
    }

    public int getScore() {
        if (getDifficulty().equals(Difficulty.EASY)) {
            return Math.max(0, rowCount * columnCount * 10 - getPlayingTime());
        }
        if (getDifficulty().equals(Difficulty.MEDIUM)) {
            return Math.max(0, rowCount * columnCount * 10*2 - getPlayingTime());
        } else {
            return Math.max(0, rowCount * columnCount * 10*4 - getPlayingTime());
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