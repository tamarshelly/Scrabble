package test;
import java.util.ArrayList;

public class Board {
    private static Board _instance = null;
    private static  Tile[][] tiles;
    private static int[][] boardBonus;
    private ArrayList<Word> newWords;
    private boolean boardisEmpty = true;
    private Board(){
        this.tiles=new Tile[15][15];
        for(int i=0;i<=14;i++){
            for(int j=0;j<=14;j++)
                this.tiles[i][j]=null;
        }
        this.boardBonus= new int[][]{{33,1,1,2,1,1,1,33,1,1,1,2,1,1,33},
                                    {1,22,1,1,1,3,1,1,1,3,1,1,1,22,1},
                                    {1,1,22,1,1,1,2,1,2,1,1,1,22,1,1},
                                    {2,1,1,22,1,1,1,2,1,1,1,22,1,1,2},
                                    {1,1,1,1,22,1,1,1,1,1,22,1,1,1,1},
                                    {1,3,1,1,1,3,1,1,1,3,1,1,1,3,1},
                                    {1,1,2,1,1,1,2,1,2,1,1,1,2,1,1},
                                    {33,1,1,2,1,1,1,22,1,1,1,2,1,1,33},
                                    {1,1,2,1,1,1,2,1,2,1,1,1,2,1,1},
                                    {1,3,1,1,1,3,1,1,1,3,1,1,1,3,1},
                                    {1,1,1,1,22,1,1,1,1,1,22,1,1,1,1},
                                    {2,1,1,22,1,1,1,2,1,1,1,22,1,1,2},
                                    {1,1,22,1,1,1,2,1,2,1,1,1,22,1,1},
                                    {1,22,1,1,1,3,1,1,1,3,1,1,1,22,1},
                                    {33,1,1,2,1,1,1,33,1,1,1,2,1,1,33}};
    }
    public static Board getBoard() {
        if (_instance == null){
            _instance = new Board();
        }
        return _instance;
    }
    public Tile[][] getTiles() {
        return this.tiles.clone();
    }

    public int[][] getBoardBonus() {
        return this.boardBonus.clone();
    }

    public Boolean boardLegal(Word newWord){
        int length = newWord.getTiles().length;
        int col = newWord.getCol();
        int row = newWord.getRow();
        boolean vertical = newWord.isVertical();
        boolean overlapLetter = false;
        Tile[][] currentBoard = getTiles();
        Tile[] wordTiles = newWord.getTiles();
        if (wordInLines(row, col, length, vertical)){
            if (boardisEmpty){ //first word in the board
                if (vertical) { //לאורך
                    if ((row < 7 && row+length <7) || (row > 7) || (col != 7))
                        return false; //the word isn't place on the star (8,8)
                }
                else {
                    if ((col < 7 && col+length < 7) || (col >7) || row !=7)
                        return false; //the word isn't place on the star (8,8)
                }
            }
            else{
                for(int l=0; l < length; l++){ // go over all the letter in word
                    if (letterIsOverlap(l, row, col, vertical, currentBoard)){
                        if (lettersEqual(wordTiles[l].letter, l, row, col, vertical, currentBoard))
                            overlapLetter = true;
                        else
                            return false;
                    }
                }
                if (!overlapLetter) { //there isn't overlap char
                    if (!wordLean(row, col, length, vertical, currentBoard))
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean dictionaryLegal(Word newWord){
        return true;
    }

    public ArrayList<Word> getWords(Word newWord){
        ArrayList<Word> newWords = new ArrayList<Word>();
        int length = newWord.getTiles().length;
        int col = newWord.getCol();
        int row = newWord.getRow();
        int firstRow, lastRow, firstCol, lastCol;
        boolean vertical = newWord.isVertical();
        Tile[][] currentBoard = getTiles();
        Tile[] currentWord = newWord.getTiles();

        if(vertical){
            firstRow = checkBeginningRow(currentBoard, row, col);//search if our word is different because there is tiles witch continue it
            lastRow = checkEndRow(currentBoard, row+length-1, col);
            if (firstRow!= row || lastRow != row+length-1)
                createNewWordVertical(firstRow, lastRow, col, currentBoard, newWords, null);
            else
                newWords.add(newWord);

            for (int l=0; l < length; l++){
                if (!letterIsOverlap(l, row, col, true, currentBoard)){
                    firstCol = checkBeginningCol(currentBoard, row+l, col);
                    lastCol = checkEndCol(currentBoard, row+l, col);
                    if (firstCol != lastCol)
                        createNewWordNonVertical(row+l, firstCol, lastCol, currentBoard, newWords, currentWord[l]);
                }
            }
        }

        else{
            firstCol = checkBeginningCol(currentBoard, row, col); //search if our word is different because there is tiles witch continue it
            lastCol = checkEndCol(currentBoard, row, col+length-1);
            if (firstCol != col || lastCol != col+length-1 ) //the first word is different
                createNewWordNonVertical(row, firstCol, lastCol, currentBoard, newWords, null);
            else
                newWords.add(newWord);

            for (int l=0; l < length; l++){
                if (!letterIsOverlap(l, row, col, false, currentBoard)){
                    firstRow = checkBeginningRow(currentBoard, row, col+l);
                    lastRow = checkEndRow(currentBoard, row, col+l);
                    if (firstRow != lastRow) {
                        createNewWordVertical(firstRow, lastRow, col+l, currentBoard, newWords, currentWord[l]);
                    }
                }
            }
        }
        return newWords;
    }

    public int getScore(Word newWord){
        Tile[] wordTiles = newWord.getTiles();
        int[][] boardBonus = getBoardBonus();
        int bonusScore, letterScore;
        int multWord = 1;
        int totalScore = 0;
        int length = newWord.getTiles().length;
        boolean vertical = newWord.isVertical();
        int row = newWord.getRow();
        int col= newWord.getCol();
        for (int l = 0; l < length; l++) {
            letterScore = wordTiles[l].score;
            bonusScore = boardBonus[row][col];
            if (bonusScore == 22 || bonusScore == 33) {//double word or triple word
                if (row == 7 && col ==7 && !boardisEmpty)//don't pay attention to the star
                    totalScore += letterScore;
                else{
                    multWord = multWord * (boardBonus[row][col] % 10);
                    totalScore += letterScore;
                }
            }
            else //double letter ot triple letter
                totalScore += letterScore * bonusScore;
            if (vertical) row++;
            else col++;
        }
        totalScore = totalScore * multWord;
        return totalScore;
    }

    public int tryPlaceWord(Word newWord){
        ArrayList<Word> newWords;
        fillEmptyLetters(newWord);
        int scoreWords = 0;
        if (boardLegal(newWord)){
            newWords = getWords(newWord);
            for(Word word: newWords){
                if (dictionaryLegal(word))
                    scoreWords += getScore(word);
            }
        }
        if (scoreWords != 0){ //insert the word into the board
            insertWordIntoBoard(newWord);
            if(boardisEmpty)
                boardisEmpty = false;
            return scoreWords;
        }
        return 0;
    }

    private void createNewWordVertical(int firstRow, int lastRow, int col, Tile[][] currentBoard, ArrayList<Word> newWords,  Tile currentTile) {
        Tile[] newWordTiles = new Tile[lastRow-firstRow+1];
        for(int r=0; r <= lastRow-firstRow; r++) {//create the tiles
            if(currentBoard[firstRow+r][col] == null) //this is where the checked tile is located
                newWordTiles[r] = currentTile;
            else
                newWordTiles[r] = currentBoard[firstRow+r][col];
        }
        newWords.add(new Word(newWordTiles, firstRow, col, true));

    }

    private void createNewWordNonVertical(int row, int firstCol, int lastCol, Tile[][] currentBoard, ArrayList<Word> newWords, Tile currentTile) {
        Tile[] newWordTiles = new Tile[lastCol-firstCol+1];
        for(int c=0; c <= lastCol-firstCol; c++){
            if (currentBoard[row][firstCol+c] == null)//this is where the checked tile is located
                newWordTiles[c] = currentTile;
            else
                newWordTiles[c]= currentBoard[row][firstCol+c];
        }
        newWords.add(new Word(newWordTiles, row, firstCol, false));
    }

    private Word fillEmptyLetters(Word newWord) {
        Tile[] wordTiles = newWord.getTiles();
        Tile[][] currentBoard = getTiles();
        boolean vertical = newWord.isVertical();
        int row = newWord.getRow();
        int col= newWord.getCol();
        for (int l=0; l<wordTiles.length; l++){
            if (wordTiles[l] == null){
                if(vertical)
                    wordTiles[l] = currentBoard[row+l][col];
                else
                    wordTiles[l] = currentBoard[row][col+l];
            }
        }
        return newWord;
    }

    public void insertWordIntoBoard(Word newWord){
        Tile[] wordTiles = newWord.getTiles();
        Tile[][] currentBoard = getTiles();
        boolean vertical = newWord.isVertical();
        int row = newWord.getRow();
        int col= newWord.getCol();
        for (int l=0; l< wordTiles.length; l++){
            if (!letterIsOverlap(l,row, col, vertical,currentBoard))
                insertLetterIntoBoard(l,row, col, vertical,currentBoard, wordTiles);//insert letter into the board
        }
    }

    public void insertLetterIntoBoard(int l, int row, int col, boolean vertical, Tile[][] currentBoard, Tile[] wordTiles){
        if (vertical)
            currentBoard[row+l][col] = wordTiles[l];
        else
            currentBoard[row][col+l] = wordTiles[l];
    }

    private int checkBeginningCol(Tile[][] currentBoard, int row, int col) {
        int firstCol = col;
        while(currentBoard[row][firstCol-1] != null)//go up to the first letter
            firstCol--;
        return firstCol;
    }

    private int checkEndCol(Tile[][] currentBoard, int row, int col) {
        int lastCol = col;
        while(currentBoard[row][lastCol+1] != null)//go up to the last letter
            lastCol++;
        return lastCol;
    }

    public int checkBeginningRow(Tile[][] currentBoard, int row, int col){
        int firstRow = row;
        while(currentBoard[firstRow-1][col] != null)//go up to the first letter
                firstRow--;
        return firstRow;
    }
    public int checkEndRow(Tile[][] currentBoard, int row, int col){
        int lastRow = row;
        while(currentBoard[lastRow+1][col] != null)//go up to the last letter
            lastRow++;
        return lastRow;
    }


    public boolean wordInLines(int row, int col, int length, boolean vertical ){
        if (row < 0 || col < 0 )
            return false;
        if (vertical){
            if (row + length -1 > 14)
                return false;
            if (col > 14)
                return false;
        }
        else{
            if (col + length -1 > 14)
                return false;
            if (row > 14)
                return false;
        }
        return true;
    }

    public boolean letterIsOverlap(int l, int row, int col, boolean vertical, Tile[][] currentBoard){
        if (vertical){
            if (currentBoard[row+l][col] != null)
                return true;
        }
        else {
            if (currentBoard[row][col+l] != null)
                return true;
        }
        return false;
    }
    public boolean lettersEqual(char letter, int l, int row, int col,boolean vertical, Tile[][] currentBoard){
        if (vertical){
            if (currentBoard[row+l][col].letter == letter)
                return true;
        }
        else {
            if (currentBoard[row][col+l].letter == letter)
                return true;
        }
        return false;
    }
    public boolean wordLean(int row, int col,int length, boolean vertical, Tile[][] currentBoard){
        if (vertical){
            for(int l=0; l < length; l++){ //check the sides
                if (col != 0){
                    if(currentBoard[row+l][col-1] != null) //right
                        return true;
                }
                if (col+length-1 != 14){
                    if(currentBoard[row+l][col+1] != null) //left
                        return true;
                }
            }
            if (row != 0){
                if (currentBoard[row-1][col]!= null)//check fist letter up
                    return true;
            }
            if(row+length-1 != 14){
                if (currentBoard[row+length-1][col]!= null)//check last letter down
                    return true;
            }
        }
        else{
            for(int l=0; l < length; l++){ //check the sides
                if (row != 0){
                    if(currentBoard[row-1][col+l] != null) //up
                        return true;
                }
                if (row != 14){
                    if(currentBoard[row+1][col+l] != null) //down
                        return true;
                }
            }
            if (col != 0){
                if (currentBoard[row][col+length-1]!= null)//check first letter right
                    return true;
            }
            if(col+length != 14){
                if (currentBoard[row][col-1]!= null)//check last letter left
                    return true;
            }
        }
        return false;
    }

}


