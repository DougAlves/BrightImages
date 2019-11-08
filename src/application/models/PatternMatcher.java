package application.models;

public class PatternMatcher {
	
	
	private String pattern;
	public PatternMatcher(String pattern) {
		this.pattern = pattern;
	}
	public String getPattern() {
		return pattern;
	}
	public String extractPathern(String obj) {
    	String toReturn = "";
    	int pLenght = pattern.length();
    	for(int i = 0; i < obj.length(); i++) {
    		if (i+ pLenght >=obj.length()) return toReturn;
    		String current = obj.substring(i, i+ pLenght);
    		if (mathPathern(current)) {
    			return current;
    		}
    	}
    	
    	return toReturn;
    }
	public boolean mathPathern(String obj) {
    	int i = 0;
    	boolean solv = true;
    	for (; i< pattern.length(); i++) {
    		if (Character.isDigit(obj.charAt(i))
    				&& pattern.charAt(i) == 'n') {
    			solv &=true;
    		}
    		else if (Character.isAlphabetic(
    				obj.charAt(i)) && pattern.charAt(i)== '#') {
    			solv &=true;
    		}
    		else if (obj.charAt(i) != pattern.charAt(i)) {
    			solv &=false;
    		}
    	}
    	
    	return solv;
    }
	
	public static String extractPathern(String pattern, String obj) {
    	String toReturn = "";
    	int pLenght = pattern.length();
    	for(int i = 0; i < obj.length(); i++) {
    		if (i+ pLenght >=obj.length()) return toReturn;
    		
    		String current = obj.substring(i, i+ pLenght);
    		if (mathPathern(pattern, current)) {
    			return current;
    		}
    	}
    	
    	return toReturn;
    }
    
    public static boolean mathPathern(String pattern, String obj) {
    	int i = 0;
    	boolean solv = true;
    	for (char l :obj.toCharArray()) {
    		if (Character.isDigit(l) && pattern.charAt(i) == 'n') {
    			solv &=true;
    		}
    		else if (Character.isAlphabetic(l) && pattern.charAt(i)== '#') {
    			solv &=true;
    		}
    		else if (l !=pattern.charAt(i)) {
    			solv &=false;
    		}
    		i++;
    	}
    	
    	return solv;
    }
}
