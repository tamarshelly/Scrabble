package test;


import java.util.Arrays;

public class Word {
    private Tile[] tiles = new Tile[10]; //array of the tiles that the word contain
    private int row, col; //position of the first tile of a word
    private boolean vertical; //the word written from up to down, if false from left to right

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }

}
