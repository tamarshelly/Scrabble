package test;


import java.util.HashSet;
import java.util.Set;

public class CacheManager {
    private int size;
    private CacheReplacementPolicy crp;
    public Set<String> cache;
    int counter; //how many words we have in the cache
    public CacheManager(int size, CacheReplacementPolicy crp){
        this.size = size;
        this.crp = crp;
        cache= new HashSet<String>();
        counter = 0;
    }

    boolean query(String word){ //check if word is in cache
        return cache.contains(word);
    }

    void add(String word){
        String wordToRemove = null;
        crp.add(word);
        if (counter == size){
            wordToRemove = crp.remove();
            cache.remove(wordToRemove);
        }
        else
            counter ++;
        cache.add(word);
    }
}
