package test;
import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {

    Map<String, Dictionary> dictionaries = new HashMap<>();
    private static DictionaryManager _instance = null;
    public static DictionaryManager get() { //returns a singleton instance of the dictionary manager
        if (_instance == null)
            _instance = new DictionaryManager();
        return _instance;
    }

    public int getSize() { //returns the number of items in the dictionary
        return dictionaries.size();
    }

    public boolean query(String...args) {
        boolean wordHasFound = false;
        String wordForSearch = args[args.length-1];
        for (String s: args) {
            if (s != wordForSearch) {
                Dictionary d = getDictionary(s); //gets (create as needed) the dictionary
                if (d.query(wordForSearch))
                    wordHasFound = true;
            }
        }
        return wordHasFound;
    }

    public boolean challenge(String...args) {
        boolean wordHasFound = false;
        String wordForSearch = args[args.length-1];
        for (String s: args) {
            if (s != wordForSearch) {
                Dictionary d = getDictionary(s); //gets (create as needed) the dictionary
                if (d.challenge(wordForSearch))
                    wordHasFound = true;
            }
        }
        return wordHasFound;
    }

    private Dictionary getDictionary(String bookName) {
        if (dictionaries.containsKey(bookName))
            return dictionaries.get(bookName);
        else{
            Dictionary dictionary = new Dictionary(bookName);
            dictionaries.put(bookName, dictionary);
            return dictionary;
        }
    }
}
