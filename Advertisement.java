import java.util.ArrayList;

public class Advertisement {
    
    // class variables
    private String audio = "";
    private String type = "";
    private ArrayList<String> targets = new ArrayList<String>();
    private int revenue = -1;

    // empty constructor
    public Advertisement() {}
        
    // constructor
    public Advertisement(String a, String t, ArrayList<String> tar, int r) {
        audio = a;
        type = t;
        targets = tar;
        revenue = r;
    }

    // getters
    public String getAudio() {
        return audio;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getTargets() {
        return targets;
    }

    public int getRevenue() {
        return revenue;
    }

    // setters
    public void setAudio(String a) {
        audio = a;
    }

    public void setType(String t) {
        type = t;
    }

    public void setTargets(ArrayList<String> t) {
        targets = t;
    }

    public void setRevenue(int r) {
        revenue = r;
    }
}
