package test;
import java.util.Objects;

public class Tile {
    public final char letter; //set data members
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public static class Bag{
        private int tileCounter = 98; //tiles amount
        private Tile[] tiles;
        private int[] tileAmount;
        private int[] maxTileAmount; //ensure we don't add more than the amount allowed
        private static Bag _instance = null;
        private Bag(){
            tiles= new Tile[]{new Tile('A', 1), new Tile('B', 3), new Tile('C', 3), new Tile('D', 2),
                    new Tile('E', 1), new Tile('F', 4), new Tile('G', 2), new Tile('H', 4),
                    new Tile('I', 1), new Tile('J', 8), new Tile('K', 5), new Tile('L', 1),
                    new Tile('M', 3), new Tile('N', 1), new Tile('O', 1), new Tile('P', 3),
                    new Tile('Q', 10), new Tile('R', 1), new Tile('S', 1), new Tile('T', 1),
                    new Tile('U', 1), new Tile('V', 4), new Tile('W', 4), new Tile('X', 8),
                    new Tile('Y', 4), new Tile('Z', 10)};
            tileAmount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            maxTileAmount = tileAmount.clone();
        }
        public static Bag getBag(){ //singleton constructor
            if (_instance == null){
                _instance = new Bag();
            }
            return _instance;
        }

        public Tile getRand(){
            int random_letter = (int)(Math.random() * (25 - 0 + 1) + 0);
            int checkingIndex = random_letter;
            while((tileAmount[checkingIndex] == 0) && (checkingIndex%26!= random_letter)) //check the numbers until one not 0 and check we didn't return to the first num
                checkingIndex++;
            if (tileAmount[checkingIndex] != 0){
                tileAmount[checkingIndex]--;
                tileCounter--;
                return tiles[checkingIndex]; //send back the tile
            }
            return null; //if all the array is 0 (empty) return null
        }

        public Tile getTile(char tile_letter){
            if ((0 <= tile_letter-'A') && (tile_letter-'A' <= 25) && (tileAmount[tile_letter-'A'] != 0)) {
                tileAmount[tile_letter-'A']--;
                tileCounter--;
                return tiles[tile_letter-'A'];
            }
            return null;
        }

        public void put(Tile returnedTile){
            if (returnedTile != null){
                if (tileAmount[returnedTile.letter - 'A']+1 <= maxTileAmount[returnedTile.letter - 'A'] ){ //check if it doesn't go over the permission amount
                    tileAmount[returnedTile.letter - 'A'] ++;
                    tileCounter++;
                }
            }
        }

        public int size(){ //get size of the tiles in the bag
            return tileCounter;
        }

        public int[] getQuantities(){ //get copy of the amount tail array
            int[] tileAmountCopy = new int[tileAmount.length];
            System.arraycopy(tileAmount,0, tileAmountCopy,0,tileAmount.length);
            return(tileAmountCopy);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }
}
