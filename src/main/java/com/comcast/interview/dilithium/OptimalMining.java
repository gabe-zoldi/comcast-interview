/*
 * *********************************************************
 *  Copyright (c) 2015 Comcast Corp.  All rights reserved.
 * *********************************************************
 */
package com.comcast.interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author gzoldi
 *
 */
public class OptimalMining {
	
	private MineType type;
	private ArrayList<String> patterns = new ArrayList<String>();
    Map<String, Integer> results = new HashMap<String, Integer>();

	public OptimalMining(MineType type) {
		this.type = type;
	}

	public void printMaxProfits(int[] discovered) {
		
		int totalMines = discovered.length;
		
		// check number of mines are valid > 0
	    if (totalMines <= 0) 
	    	// log warning
	    	System.out.println("Error computing optimal configuration - Invalid number of mines specified: " + totalMines);
	    
	    // define number of placeholder based on number of bits specified
	    String placeholder = "";
	    
	    for (int i = 0; i < totalMines; i++) 
	    	placeholder += "0";

	    /*
	     *  create possibilities based on number of bit permutations
	     */
	    int[] possible = new int[] {0, 1};  // binary: 2 possible values

	    for (int i = 1; i < totalMines; i++) {
	        int[] permutation = new int[possible.length * 2];
	        
	        // bit shift
	        for (int j = 0; j < possible.length; j++) {
	        	permutation[j * 2] = possible[j] << 1;
	        	permutation[j * 2 + 1] = possible[j] << 1 | 1;
	        }
	        
	        possible = permutation;
	    }
	    
	    /*
	     *  create list of possible patterns
	     */
	    for (int value: possible) {
	    	String bin = Integer.toBinaryString(value);
	    	
	    	// pad zeroes
	    	String padded = placeholder.substring(bin.length()) + bin;
	    	patterns.add(padded);
	    }
	    
	    // get rid of neighboring mines - cause they can explode, boom!
	    removeNeighbors();
	    
		for (String pattern : getPatterns()) {
			//System.out.println(pattern);
			
			int i = 0;
			String minePattern = "";
			int mineTotal = 0;
			
			for (char c : pattern.toCharArray() ) {
		        switch (c) {
		            case '0': 
		            	minePattern += "x";
		            	break;
		            case '1': 
		            	minePattern += discovered[i];
		            	mineTotal += Integer.valueOf(discovered[i]);
		            	break;
		            default: 
		            	throw new RuntimeException("Error: Unexpected bit value in pattern.");
		        }
		        
		        // add comma sep for each element except last element
		        int lastIndex = totalMines - 1;
		        if (i < lastIndex) 
		        	minePattern += ", "; 
		        i++;
			}
			
			results.put(minePattern, mineTotal);
		}
		
		// print max profits including any mines with duplicate max profits
        int maxTotal = Collections.max(results.values());
        
        for (Entry<String, Integer> entry : results.entrySet()) 
            if (entry.getValue() == maxTotal) 
                System.out.println(entry.getKey() + " = " + maxTotal);
	}

	public ArrayList<String> getPatterns() {
		return patterns;
	}
	
	protected void removeNeighbors() {
		ArrayList<String> removeItems = new ArrayList<String>();
		
		for (String pat : patterns) 
			if (hasNeighbor(pat)) 
				removeItems.add(pat);
		
		for (String remove : removeItems) 
			patterns = removeElement(patterns, remove);
	}
	
	@SuppressWarnings("unchecked")
	protected ArrayList<String> removeElement(ArrayList<String> input, String deleteMe) {
		ArrayList<String> cleaned = (ArrayList<String>) input.clone();
		
	    for (String item : input)
	        if (!deleteMe.equals(item))
	        	cleaned.remove(deleteMe);
	    
	    return cleaned;
	}
	
	protected boolean hasNeighbor(String pattern) {
		char[] token = pattern.toCharArray();
		
		for (int i = 0; i < token.length - 1; i++) {
			if (token[i] == '1') 
				if (token[i] == token[i + 1]) 
					return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		//int[] discoveredMines = { 206, 140, 300, 52, 107 };
		int[] discoveredMines = { 147, 206, 52, 240, 300 };
				
		OptimalMining optimal = new OptimalMining(MineType.DILITHIUM);
		optimal.printMaxProfits(discoveredMines);
	}
	
}