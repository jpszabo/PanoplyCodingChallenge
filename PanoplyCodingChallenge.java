import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class PanoplyCodingChallenge {
	public static void main(String[] args) {
		
		// We are expecting args to be 
		// EPISODE AD_CAMPAIGN_DIRECTORY 
		// or
		// EPISODE AD_CAMPAIGN_1 AD_CAMPAIGN_2 AD_CAMPAIGN_3...
        if (args.length == 0) {
        		System.out.println("Proper arguments: EPISODE AD_CAMPAIGN_DIRECTORY or EPISODE AD_CAMPAIGN_1 AD_CAMPAIGN_2 ...");
        		return;
        }
        
        // Create episode from episode file
        Episode episode = readEpisodeFromFile(args[0]);
        
        // Grab the numbers of ads for episode
    		int episodePreRolls = episode.getPreRolls();
    		int episodeMidRolls = episode.getMidRolls();
    		int episodePostRolls = episode.getPostRolls();
        
    		// If we don't have any ad spots, either there are no ad spots for this episode and it's a no-op or
    		// the episode wasn't constructed properly
        if (episodePreRolls == 0 &&
        		episodeMidRolls == 0 &&
        		episodePostRolls == 0) {
        		// Clean the episode so there is just text and get out (garbage in, garbage out)
        		episode.setAudio(episode.getAudio().replaceAll("[^+]", ""));
            System.out.println(episode.getAudio());
            // We will still add "_withAds" to the fileName to signify that it went through this process
            writeEpisodeToFile(episode, args[0]);
        		return;
        }
        
        if (args.length == 1) {
        		System.out.println("WARNING: No ad campaigns entered. Proper arguments: EPISODE AD_CAMPAIGN_DIRECTORY or EPISODE AD_CAMPAIGN_1 AD_CAMPAIGN_2 ...");// Clean the episode so there is just text and get out (garbage in, garbage out)
        		episode.setAudio(episode.getAudio().replaceAll("[^+]", ""));
        		System.out.println(episode.getAudio());
        		// We will still add "_withAds" to the fileName to signify that it went through this process
            writeEpisodeToFile(episode, args[0]);
        		return;
        }
        
        // Grab our adCampaigns
        ArrayList<AdCampaign> adCampaigns = new ArrayList<AdCampaign>();
        if (args.length == 2) {
        		adCampaigns = readAdCampaignsFromDirectory(args[1]);
        }
        else {
        		for (int i = 1; i < args.length; i++) {
        			adCampaigns.add(readAdCampaignFromFile(args[i]));
        		}
        }
        
        // In lieu of recursive optimization, organize the adCampaigns by revenue per ad
        // This is not perfectly optimized but it's a good proxy for the optimal solution
        adCampaigns = sortByRevenuePerAd(adCampaigns);
        
        // Fill episode with ads
        for (AdCampaign ac : adCampaigns) {
        		int campaignPreRolls = ac.getPreRolls();
        		int campaignMidRolls = ac.getMidRolls();
        		int campaignPostRolls = ac.getPostRolls();
        		if (campaignPreRolls <= episodePreRolls &&
        				campaignMidRolls <= episodeMidRolls &&
        				campaignPostRolls <= episodePostRolls &&
        				ac.getTargets().contains(episode.getId())) {
        			episode.insertAds(ac);
        			episodePreRolls -= campaignPreRolls;
        			episodeMidRolls -= campaignMidRolls;
        			episodePostRolls -= campaignPostRolls;
        		}
        }
        
        // Get rid of extraneous brackets from ad insertion
        episode.cleanAudio();
        
        // Printing updated episode and writing it to a file, for convenience sake
        System.out.println(episode.getAudio());
        writeEpisodeToFile(episode, args[0]);
        return;
    }
	
	private static void writeEpisodeToFile(Episode episode, String fileName) {
		// append "_withAds" to fileName, but don't disturb the extension
		fileName = fileName.substring(0, fileName.lastIndexOf('.')) + "_withAds" + fileName.substring(fileName.lastIndexOf('.'));
		
		try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // Write the episode out as it was written in
            bufferedWriter.write("{ id: \"" + episode.getId() + "\", audio: \"" + episode.getAudio() + "\" }");

            bufferedWriter.close();
        }
        catch (IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
        }
	}
	
	private static Episode readEpisodeFromFile(String fileName) {
		Episode e = new Episode();
		String line = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // The only line in the file should be the episode
            while((line = bufferedReader.readLine()) != null) {
            		e.setId(parseLineForId(line));
            		e.setAudio(parseLineForAudio(line));
            }   

            bufferedReader.close();         
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open episode file '" + fileName + "'");                
        }
        catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
		return e;
	}
	
	private static ArrayList<AdCampaign> readAdCampaignsFromDirectory(String directory) {
		ArrayList<AdCampaign> adCampaigns = new ArrayList<AdCampaign>();
		
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    		if (listOfFiles[i].isFile() && 
	    				!listOfFiles[i].getName().startsWith(".")) { // no hidden files for us
	    			adCampaigns.add(readAdCampaignFromFile(listOfFiles[i].getPath()));
	    		}
	    }
	    return adCampaigns;
	}
	
	private static AdCampaign readAdCampaignFromFile(String fileName) {
		AdCampaign ac = new AdCampaign();
		ArrayList<Advertisement> adList = new ArrayList<Advertisement>();
		String line = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
            		// we want to skip all empty lines and the first and last lines which contain only brackets
            		if (line.equals("[") || line.equals("]") || line.equals("\n") || line.equals("")) continue;
            		Advertisement a = new Advertisement();
            		a.setType(parseLineForType(line));
            		a.setRevenue(parseLineForRevenue(line));
            		a.setTargets(parseLineForTargets(line));
            		a.setAudio(parseLineForAudio(line));
            		adList.add(a);
            }   

            bufferedReader.close();         
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open episode file '" + fileName + "'");                
        }
        catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        ac.setAds(adList);
		return ac;
	}
	
	private static String parseLineForId(String line) {
		return parseLineForX("id", line, '"', '"');
	}
	
	private static String parseLineForAudio(String line) {
		return parseLineForX("audio", line, '"', '"');
	}
	
	private static int parseLineForRevenue(String line) {
		String revenue = parseLineForX("revenue", line, ' ', ' ');
		// Sanity check
		if (revenue.equals("")) {
			return 0;
		}
		else {
			return Integer.parseInt(revenue);
		}
	}
	
	private static String parseLineForType(String line) {
		return parseLineForX("type", line, '"', '"');
	}
	
	private static ArrayList<String> parseLineForTargets(String line) {
		String targets = parseLineForX("targets", line, '[', ']');
		// Make sure the targets are clean so they can be compared to the episode ids later
		targets = targets.replaceAll("\"", "");
		// Sanity check
		if (targets.equals("")) {
			return new ArrayList<String>();
		}
		else {
			return new ArrayList<String>(Arrays.asList(targets.trim().split(", ")));
		}
	}
	
	private static String parseLineForX(String x, String line, char openDelimiter, char closeDelimiter) {
		if (line.contains(x + ":")) {
			int idIndex = line.indexOf(x + ":");
			int firstDelimiterIndex = line.indexOf(openDelimiter, idIndex);
			int nextDelimiterIndex = line.indexOf(closeDelimiter, firstDelimiterIndex + 1);
			return line.substring(firstDelimiterIndex + 1, nextDelimiterIndex);
		}
		else {
			return "";
		}
	}
    
	// put the elements into a treeMap to sort them
    private static ArrayList<AdCampaign> sortByRevenuePerAd(ArrayList<AdCampaign> adCampaigns) {
    		Map<Double, AdCampaign> sortedAds = new TreeMap<Double, AdCampaign>();
    		// We're using revenuePerAd as a key and there's a possibility for duplicates
    		double adjustment = 0.01;
    		for (AdCampaign ac : adCampaigns) {
    			double ads = ac.getPreRolls() + ac.getMidRolls() + ac.getPostRolls();
    			double revenuePerAd = ac.getCampaignRevenue() / ads;
    			if (sortedAds.containsKey(revenuePerAd)) {
    				// If we find a duplicate, just increment the revenuePerAd by this nominal amount
    				revenuePerAd += adjustment;
    				// and increase the adjustment for next time to keep it unique
    				adjustment += 0.01;
    			}
    			sortedAds.put(revenuePerAd, ac);
    		}
    		ArrayList<AdCampaign> returnAds = new ArrayList<AdCampaign>();
    		for (AdCampaign ac : sortedAds.values()) {
    			returnAds.add(ac);
    		}
    		return returnAds;
    }
}
