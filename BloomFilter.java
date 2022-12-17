package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.BitSet;
import java.security.NoSuchAlgorithmException;


public class BloomFilter {
    BitSet bitSet;
    MessageDigest[] hashFunctions;
    private int size;
    public BloomFilter(int size, String... K)  { //K is all hash functions
        this.size = size;
        bitSet = new BitSet(size);
        hashFunctions = new MessageDigest[K.length];
        int i = 0;
        //active the hash functions
        for (String s: K){
            try {
                hashFunctions[i] = MessageDigest.getInstance(s);
                i++;
            }
            catch (NoSuchAlgorithmException e) {
                System.err.println("I'm sorry, the hash name is not a valid message digest algorithm");
            }
        }
    }
    public void add(String word){ //insert the word to bloom filter
        for (MessageDigest hashFunc: hashFunctions) //run all hush functions on the word
            bitSet.set(getWordBit(word,hashFunc)); //turn on the relevant bites
    }
    public boolean contains(String word){ //check if the word is in bloom filter
        for (MessageDigest hashFunc: hashFunctions){
            if(!bitSet.get(getWordBit(word,hashFunc)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() { //return the bitSet of 0,1
        StringBuilder bitesString = new StringBuilder();
        for (int i= 0; i <bitSet.length(); i++){
            bitesString.append(bitSet.get(i) ? "1" : "0");
        }
        return bitesString.toString();
    }

    private int getWordBit(String word, MessageDigest hashFunc) {
        int bit;
        byte[] bts;
        BigInteger bigInt = null;
        bts = hashFunc.digest(word.getBytes());
        bigInt= new BigInteger(bts);
        return (Math.abs(bigInt.intValue()))%size;
    }
}
