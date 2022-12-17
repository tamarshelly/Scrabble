package test;


import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy {
    //least recently used
    LinkedList<String> wordsList;
    public  LRU(){
        wordsList = new LinkedList<String>();
    }

    @Override
    public void add(String word) {
        //check if word already in the list
        if (wordsList.contains(word))
            wordsList.remove(word);
        wordsList.add(word);
    }

    @Override
    public String remove() {
        //return the string that we *ask about* it in the most far time
        return wordsList.removeFirst();
    }
}
