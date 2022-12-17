package test;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Dictionary {
    private CacheReplacementPolicy LRU = new LRU();
    private CacheReplacementPolicy LFU = new LFU();
    private CacheManager existingWordsCache;
    private CacheManager nonExistentWordsCache;
    private BloomFilter bloomFilter;
    private String[] fileNamesArr;

    public Dictionary(String... fileNames) {
        this.existingWordsCache = new CacheManager(400, LRU);
        this.nonExistentWordsCache = new CacheManager(100, LFU);
        this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        this.fileNamesArr = fileNames.clone();
        for (String fileName: fileNames)
            enterWordsIntoBloom(fileName); //enter all the words from the file into bloomFilter
    }

    public boolean query(String word){
        if(existingWordsCache.query(word))
            return true;
        if(nonExistentWordsCache.query(word))
            return false;
        if(bloomFilter.contains(word)){
            existingWordsCache.add(word); //add to existing words cache
            return true;
        }
        else {
            nonExistentWordsCache.add(word); //add to nonexistent cache
            return false;
        }
    }

    public boolean challenge(String word){
        if(IOSearcher.search(word, fileNamesArr)){
            existingWordsCache.add(word);
            return true;
        }
        else{
            nonExistentWordsCache.add(word);
            return false;
        }

    }

    private void enterWordsIntoBloom(String fileName){
        try{
            Scanner sc = new Scanner(new File(Paths.get(fileName).toUri()));
            while(sc.hasNext())
                bloomFilter.add(sc.next());
        } catch (FileNotFoundException e){ System.out.println("file not found");}
    }
}
