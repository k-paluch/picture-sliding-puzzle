package sk.tuke.gamestudio.pictureslidingpuzzle.core;

class NullPuzzle {
    private int row;
    private int column;
    NullPuzzle(int row, int column){
        this.row = row;
        this.column = column;
    }
    int getRow(){
        return this.row;
    }

    int getColumn(){
        return this.column;
    }

    void setRow(int row){
        this.row = row;
    }

    void setColumn(int column){
        this.column = column;
    }
}
