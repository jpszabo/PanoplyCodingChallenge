public class Episode {

    // class variables
    private String audio = "";
    private String id = "";
    
    // Format from ads
    private static final String pre = "PRE";
    private static final String mid = "MID";
    private static final String post = "POST";
    // Format for substring occurrence
    private static final String preBracket = "[PRE]";
    private static final String midBracket = "[MID]";
    private static final String postBracket = "[POST]";
    // Format for replace first/all
    private static final String preBracketEscaped = "\\[PRE\\]";
    private static final String midBracketEscaped = "\\[MID\\]";
    private static final String postBracketEscaped = "\\[POST\\]";

    // empty constructor
    public Episode() {}

    // constructor
    public Episode(String a, String i) {
        audio = a;
        id = i;
    }
    
    public int getPreRolls() {
	    return occurrencesOfSubstring(preBracket);
    }
    
	public int getMidRolls() {
		return occurrencesOfSubstring(midBracket);
	}

	public int getPostRolls() {
		return occurrencesOfSubstring(postBracket);
	}

    // getters
    public String getAudio() {
        return audio;
    }
    
    public String getId() {
        return id;
    }

    // setters
    public void setAudio(String a) {
        audio = a;
    }

    public void setId(String i) {
        id = i;
    }
    
    // https://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string
    private int occurrencesOfSubstring(String str)
    {
	    	int lastIndex = 0;
	    	int count = 0;
	
	    	while  (lastIndex != -1){
	
	    	    lastIndex = audio.indexOf(str,lastIndex);
	
	    	    if (lastIndex != -1) {
	    	        count ++;
	    	        lastIndex += str.length();
	    	    }
	    	}
	    	return count;
    }
    
    public void insertAds(AdCampaign ac) {
    		// replace first instance of ad type in episode
    		for (Advertisement a : ac.getAds()) {
    			if (a.getType().equals(pre)) {
    				audio = audio.replaceFirst(preBracketEscaped, a.getAudio());
    			}
    			else if (a.getType().equals(mid)) {
    				audio = audio.replaceFirst(midBracketEscaped, a.getAudio());
    			}
    			else if (a.getType().equals(post)) {
    				audio = audio.replaceFirst(postBracketEscaped, a.getAudio());
    			}
    		}
    }
    
    public void cleanAudio() {
    		// remove any ads that weren't filled
    		audio = audio.replaceAll(preBracketEscaped, "");
    		audio = audio.replaceAll(midBracketEscaped, "");
    		audio = audio.replaceAll(postBracketEscaped, "");
    }
}
