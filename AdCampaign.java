import java.util.ArrayList;

public class AdCampaign {
	
	// class variables
	private ArrayList<Advertisement> ads = new ArrayList<Advertisement>();
	
	// empty constructor
	public AdCampaign() {}
	
	// constructor
	public AdCampaign(ArrayList<Advertisement> a) {
		ads = a;
	}
	
	// getters
	public ArrayList<Advertisement> getAds() {
		return ads;
	}
	
	public ArrayList<String> getTargets() {
		if (ads.isEmpty()) {
			return new ArrayList<String>();
		}
		else {
			return ads.get(0).getTargets();
		}
	}
	
	public int getPreRolls() {
		int pre = 0;
		for (Advertisement a : ads) {
			if (a.getType().equals("PRE")) {
				pre++;
			}
		}
		return pre;
	}
	
	public int getMidRolls() {
		int mid = 0;
		for (Advertisement a : ads) {
			if (a.getType().equals("MID")) {
				mid++;
			}
		}
		return mid;
	}
	
	public int getPostRolls() {
		int post = 0;
		for (Advertisement a : ads) {
			if (a.getType().equals("POST")) {
				post++;
			}
		}
		return post;
	}
	
	public int getCampaignRevenue() {
		int revenue = 0;
		for (Advertisement a : ads) {
			revenue += a.getRevenue();
		}
		return revenue;
	}
	
	// setters
	public void setAds(ArrayList<Advertisement> a) {
		ads = a;
	}

}
