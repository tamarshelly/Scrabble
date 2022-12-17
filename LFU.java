package test;


import java.util.*;

public class LFU implements CacheReplacementPolicy {
//least frequently used
LinkedHashMap<String, Integer> wordsMap;
    public LFU(){
        wordsMap = new LinkedHashMap<String, Integer>();
    }
    @Override
    public void add(String word) {
        int count = 0;
        if (wordsMap.containsKey(word)) {
            count = wordsMap.get(word) + 1;
            wordsMap.replace(word, count);
        }
        else
            wordsMap.put(word, 1);
    }

    @Override
    public String remove() {
        //return the string we ask for it the minimum time - the word with the smallest count
        int minCount=100; //find min count
        Map.Entry searchedWord;
        for(Map.Entry w: wordsMap.entrySet()){
            if ((int)w.getValue() < minCount)
                minCount = (int)w.getValue();
        }
        //find first word with min count
        String wordToRemove = null;
        Set<Map.Entry<String, Integer>> set = wordsMap.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = set.iterator();
        while (iterator.hasNext() && wordToRemove == null) {
            Map.Entry<String, Integer> tempIterator = iterator.next();
            if (tempIterator.getValue()  == minCount)
                wordToRemove = tempIterator.getKey();
        }
        return wordToRemove;
    }
}
