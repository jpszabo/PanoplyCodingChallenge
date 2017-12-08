Hello Panoply!

Here is my solution to your challenge. I did my best to balance error checking with the time limit so I ended up making a few assumptions. I mostly stuck with the "garbage in, garbage out" approach, cleaning things up where I could but ultimately eschewing the responsibility of a properly formed episode to the producer. I also know that the sample episodes and files were presented in JSON for easy parsing and I’ve worked with it before but I tried to stick to standard libraries and such. I assume that these JSON strings are, for the most part, well-formed. 

You can see that I chose Java as my coding language. I used one snippet of code from StackOverflow, which I marked, and I used other sources for syntax refreshers, as I work mostly in C++ at work (this was my respite from that).

I wanted to shoot for optimizing revenue and I’m sure I could have thought a little harder or searched for more helpful hints but, through some testing, I confirmed that starting with the highest revenue per ad and working your way down either gets the optimal solution or at least comes close. I was comfortable with this due to the suggested time limit.

The program takes, as command line inputs: an episodeFile, which is a file (possibly .txt) that contains the episode metadata; and either an adCampaignDirectory or a list of adCampaignFile(s). The adCampaignFile(s) contain the metadata for an ad campaign, similar to the episode. I’ve included files built from the examples in your PDF.

To run this program:
1) Unzip the directory
2) From the command line, run "javac PanoplyCodingChallenge.java"
3) Then run "java PanoplyCodingChallenge [episodeFile] [adCampaignDirectory]". The episodeFile and adCampaignDirectory are required, although there’s a bit of a failsafe in case you miss those. You can also choose to list ad campaign files if you’d rather not use a directory. 

I print the audio contents of the episode out to the command line for easy viewing and also write out a new episode file, appending "_withAds" before the extension so that the original episode file can be used more than once (for more than one set of ad campaigns, for example).

Enjoy! Let me know if you have any questions or issues.