import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Cluster {
	
	//each cluster is supposed to have one article and a number of source files associated with it
	///there might be some clusters which have 2 articles 
	//these clusters will have to be split into smaller clusters
	List<String> articles = new ArrayList<String>();
	List<String> sourceFiles = new ArrayList<String>();
	
	//the keywords that are associated with this cluster
	//these keywords are to be read from the topic.txt
	HashSet<String> keywords = new HashSet<String>();
	
	//each topic will and cluster will also be given a cluster/topicno
	String clusterNo = "-1";
	
	//reads the data from the topic.txt and distribution.txt
	//create cluster for each topic
	//assigns the keywords to the appropraite topic
	public static List<Cluster> createClusters() {
		
		
		//System.out.println("inside create clusters");
		
		
		
		List<Cluster> clusters = new ArrayList<Cluster> ();
		
		//read the topic.txt to identify the number of clusters and the number
		//of the cluster and the keywords belonging to each cluster
		 File file = new File("topic.txt");
		 try {

		        Scanner sc = new Scanner(file);
		        

		        //the first line is empty so
		        sc.nextLine();
		        
		        //for every topic create a cluster, read the topic no and the 
		        //top 20 keywords associated with the topic
		        while (sc.hasNextLine()) {
		        	//System.out.println(sc.nextInt());
		        	//System.out.println(sc.nextFloat());
		        	//System.out.println(sc.nextLine());
		            Cluster newCluster = new Cluster();
		            newCluster.clusterNo = "" + sc.nextInt();
		            sc.nextFloat();
		            for(int i = 0 ; i < 20 ; i++) {
		            	newCluster.keywords.add(sc.next());
		            }
		            clusters.add(newCluster);
		            sc.nextLine();
		            
		        }
		        sc.close();
		 } catch (FileNotFoundException e) {
			 
			 	System.out.println("Hit error while reading the topic.txt ");
		        e.printStackTrace();
		 }
		 
		// System.out.println("Successfully scanned the file");
		 
		 //read the distribution.txt to find which file belongs to which topic
		 file = new File("distribution.txt");
		 try {

		        Scanner sc = new Scanner(file);

		        //the first line is empty so
		        sc.nextLine();
		        
		        //in every row there is a document and there is the proportional distribution of the document
		        //The first topic number is the topic the document belongs to
		        while (sc.hasNextLine()) {
		            //read the third string or int which is the topic number
		        	sc.nextInt();
		        	String name = sc.next();   //the document name
		        	int topicNo = sc.nextInt(); //he topic it belongs to
		        	
		        	//see if the document is an article or source file by seeing the name
		        	if(name.contains("$AAA$")) {
		        		clusters.get(topicNo).articles.add(name);
		        	} else {
		        		clusters.get(topicNo).sourceFiles.add(name);
		        	}
		        	sc.nextLine();
		        }
		        sc.close();
		 } catch (FileNotFoundException e) {
			 System.out.println("Hit error while reading the topic.txt ");
		        e.printStackTrace();
		 }
		 
		//System.out.println("returning clusters");
		return clusters;
		
	}
	
	//there might be some clusters which have onyl source files and no articles
	//the source files in such clusters should be distributed to clusters with articles
	//the keywords that are associated with those clusters need to be found 
	//the source file can be transferred to the cluster with which it matches the most
	public static void cleanSourceFileCluster(List<Cluster> clusters, Map<String, SourceFile> sourceFileMap) {
		
		int clusterNo = 0;
		
		//collect all the source files from clusters without an article
		List<SourceFile> sourceFiles = new ArrayList<SourceFile>();
		
		//go through all the clusters
		while(clusterNo < clusters.size()) {
			Cluster cl = clusters.get(clusterNo);
			
			//check if the cluster has atleast one article
			//at this point the clusters cannot have more than one article
			
			if(cl.articles.size() == 1) {
				clusterNo++;
			} else {
				//if it has no articles then

				
				for(String name : cl.sourceFiles) {
					SourceFile sf = sourceFileMap.get(name);
					sourceFiles.add(sf);
				}
				
				clusters.remove(clusterNo);
			}
		}
		
		
		//once all the source files have been collected figure out which cluster these files belong to
		for(int i = 0 ; i < sourceFiles.size() ; i++) {
			int maxMatch = Integer.MIN_VALUE;
			int maxMatchClusterNo = 0;
			
			//get all the keywords of the source file
			String[] split = sourceFiles.get(i).keyWords.split(" ");
			
			//find how it matches with the keywords of each of the clusters
			for(int j = 0 ; j < clusters.size() ; j++) {
				int count = 0;
				for(int k = 0 ; k < split.length ; k++) {
					if(clusters.get(j).keywords.contains(split[k])) {
						count++;
					}
				}
				
				//find the cluster with which it matches the most
				if(count > maxMatch) {
					maxMatch = count;
					maxMatchClusterNo = j;
				}
			}
			
			
			//assign the source file to that cluster
			clusters.get(maxMatchClusterNo).sourceFiles.add(sourceFiles.get(i).name);
			
		}
			
	}
	
	
	
	
	//find clusters that have 2 articles in them
	//pick these clusters and identify the words that are unique to each of these articles
	//classify the source files into these articles
	//create new cluster for each of these article and add them to the main list, 
	//remove the cluster which had more then one article from the main list "clusters"
	public static void cleanCluster(List<Cluster> clusters, Map<String, Article> articleMap, Map<String, SourceFile> sourceFileMap) {
		int clusterNo = 0;
		
		//System.out.println("inside Clean cluster");
		
		//this is to make sure all the clusters are checked
		while(clusterNo < clusters.size()) {
			
			//get each cluster
			Cluster cluster = clusters.get(clusterNo);
			
			//check if the cluster has 1 article or more than 1 article
			if(cluster.articles.size() <= 1) {
				
				//go check the next cluster
				clusterNo++;
			} else {
				
				/*
				 * technique 3
				 */
				
				//the no of articles in this cluster
				int articleListSize  = cluster.articles.size();
				
				//get the articles of this cluster
				List<Article> articlesInCluster = new ArrayList<Article>();
				
				for(int i = 0 ; i < articleListSize ; i++) {
					//retrieve the article with the specific name from the map
					//System.out.println(cluster.articles.get(i));
					Article article = articleMap.get(cluster.articles.get(i));
					//System.out.println(article.name);
					//System.out.println(article.getKeyWords());
					articlesInCluster.add(article);
				}
				
				//System.out.println("The number of articles in the cluster " + articlesInCluster.size());
				
				//for each of the articles in the cluster
				//count the number of occurrence of each word
				//find the unique keywords as well
				//in the same time also count the total number of words in this article
				//so instead of having the unique words as a set, lets change the unique
				//words into a hashmap
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					System.out.println(articlesInCluster.get(i).name);
					System.out.println(articlesInCluster.get(i).getKeyWords());
					String[] keywordArray = articlesInCluster.get(i).getKeyWords().split(" ");
					Set<String> keyWordSet = new HashSet<String>(Arrays.asList(keywordArray));
					articlesInCluster.get(i).uniqueKeyWordSet = keyWordSet;
					
					//for every keywords add it to the hashtable
					for(int j = 0 ; j < keywordArray.length ; j++) {
						articlesInCluster.get(i).totalWordCount = articlesInCluster.get(i).totalWordCount + 1;
						if(articlesInCluster.get(i).uniqueKeyWords.containsKey(keywordArray[j])) {
							articlesInCluster.get(i).uniqueKeyWords.put(keywordArray[j], articlesInCluster.get(i).uniqueKeyWords.get(keywordArray[j]) + 1);
						} else {
							articlesInCluster.get(i).uniqueKeyWords.put(keywordArray[j], 1);
						}
					}
				}
				
				//then remove the words from the articles that belong to other articles to
				//now remove all the common keywords that are there between any two articles
				// the articles should be left with keywords that are soleley special to them
				//and do not overlap with the keywords of any other article
				for(int i = 0 ; i < articlesInCluster.size(); i++) {
					for(int j = i + 1 ; j < articlesInCluster.size() ; j++) {
						articlesInCluster.get(i).uniqueKeyWordSet.removeAll(articlesInCluster.get(j).uniqueKeyWordSet);
						articlesInCluster.get(j).uniqueKeyWordSet.removeAll(articlesInCluster.get(i).uniqueKeyWordSet);
					}
				}
				
				//go through the unique keyword and identify the percentage of occurence of each word
				//let us assume the percentage of occurrence needs to be more than 1.5%
				//remove the words that occur less than the threshold
				
				//for each of the articles
				for(int i = 0 ; i < articlesInCluster.size(); i++) {
					
					//for every unique word in the article
					for(String word : articlesInCluster.get(i).uniqueKeyWordSet){
						//get the count of it from the hashtable
						int count = articlesInCluster.get(i).uniqueKeyWords.get(word);
						float percentage = (float)count / (float) articlesInCluster.get(i).totalWordCount;
						
						//if the percentage does not meet the threshold, remove the word from the set
						if(percentage < 1.5) {
							articlesInCluster.get(i).uniqueKeyWords.remove(word);
						}
					}
				}
				
				//get the list of source files in the cluster
				List<SourceFile> sourceFilesInCluster = new ArrayList<SourceFile> ();
				
				//retrieve the sourcefiles from the SourceFileMap
				for(int i = 0 ; i < cluster.sourceFiles.size() ; i++) {
					SourceFile source = sourceFileMap.get(cluster.sourceFiles.get(i));
					sourceFilesInCluster.add(source);
				}
				
				
				
				//create a new cluster for each of the article
				//add the name of the article to the article list
				//add this to the new cluster list
				
				ArrayList<Cluster> newClusterList = new ArrayList<Cluster>();
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					Cluster newCluster = new Cluster();
					newCluster.clusterNo = cluster.clusterNo+"_" + i;
					newCluster.keywords = (HashSet<String>) articlesInCluster.get(i).uniqueKeyWordSet;
					newCluster.articles.add(articlesInCluster.get(i).name);
					newClusterList.add(newCluster);
				}
				
				//remove the old cluster from the clusters list
				clusters.remove(clusterNo);
				
				//for each source file find the amount of overlap it has with each of the article
				for(int i = 0 ; i < sourceFilesInCluster.size(); i++) {
					System.out.println(sourceFilesInCluster.get(i).getName());
					int max = Integer.MIN_VALUE;
					int clusterOverLapNo = -1;
					//compare the amount of overlap of the source file with each of the articles 
					for(int j = 0 ; j < articlesInCluster.size() ; j++) {
						int count = 0;
						for(String keyword:articlesInCluster.get(j).uniqueKeyWordSet) {
							if(sourceFilesInCluster.get(i).keyWords.contains(" " + keyword + " ")) {
								count = count + 1;
							}
						}
						
						//System.out.println(articlesInCluster.get(j).getName() + " : " + count);
						if(count > max) {
							max = count;
							clusterOverLapNo = j;
						}
					}
					
					newClusterList.get(clusterOverLapNo).sourceFiles.add(sourceFilesInCluster.get(i).name);
				}
				
				clusters.addAll(newClusterList);
				
				
				
				
				
				/*
				 * end of technique 3
				 */
				
				
				/*
				 * technique 2
				 *
				
				//the no of articles in this cluster
				int articleListSize  = cluster.articles.size();
				
				//get the articles of this cluster
				List<Article> articlesInCluster = new ArrayList<Article>();
				
				for(int i = 0 ; i < articleListSize ; i++) {
					//retrieve the article with the specific name from the map
					Article article = articleMap.get(cluster.articles.get(i));
					articlesInCluster.add(article);
				}
				
				//for each of the articles in the cluster
				//count the number of occurrence of each word
				//find the unique keywords as well
				//in the same time also count the total number of words in this article
				//so instead of having the unique words as a set, lets change the unique
				//words into a hashmap
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					String[] keywordArray = articlesInCluster.get(i).getKeyWords().split(" ");
					Set<String> keyWordSet = new HashSet<String>(Arrays.asList(keywordArray));
					articlesInCluster.get(i).uniqueKeyWordSet = keyWordSet;
					
					//for every keywords add it to the hashtable
					for(int j = 0 ; j < keywordArray.length ; j++) {
						articlesInCluster.get(i).totalWordCount = articlesInCluster.get(i).totalWordCount + 1;
						if(articlesInCluster.get(i).uniqueKeyWords.containsKey(keywordArray[j])) {
							articlesInCluster.get(i).uniqueKeyWords.put(keywordArray[j], articlesInCluster.get(i).uniqueKeyWords.get(keywordArray[j]) + 1);
						} else {
							articlesInCluster.get(i).uniqueKeyWords.put(keywordArray[j], 1);
						}
					}
				}
				
				//then remove the words from the articles that belong to other articles to
				//now remove all the common keywords that are there between any two articles
				// the articles should be left with keywords that are soleley special to them
				//and do not overlap with the keywords of any other article
				for(int i = 0 ; i < articlesInCluster.size(); i++) {
					for(int j = i + 1 ; j < articlesInCluster.size() ; j++) {
						articlesInCluster.get(i).uniqueKeyWordSet.removeAll(articlesInCluster.get(j).uniqueKeyWordSet);
						articlesInCluster.get(j).uniqueKeyWordSet.removeAll(articlesInCluster.get(i).uniqueKeyWordSet);
					}
				}
				
				//go through the unique keyword and identify the percentage of occurence of each word
				//let us assume the percentage of occurrence needs to be more than 1.5%
				//remove the words that occur less than the threshold
				
				//for each of the articles
				for(int i = 0 ; i < articlesInCluster.size(); i++) {
					
					//for every unique word in the article
					for(String word : articlesInCluster.get(i).uniqueKeyWordSet){
						//get the count of it from the hashtable
						int count = articlesInCluster.get(i).uniqueKeyWords.get(word);
						float percentage = (float)count / (float) articlesInCluster.get(i).totalWordCount;
						
						//if the percentage does not meet the threshold, remove the word from the set
						if(percentage < 1.5) {
							articlesInCluster.get(i).uniqueKeyWords.remove(word);
						}
					}
				}
				
				//get the list of source files in the cluster
				List<SourceFile> sourceFilesInCluster = new ArrayList<SourceFile> ();
				
				//retrieve the sourcefiles from the SourceFileMap
				for(int i = 0 ; i < cluster.sourceFiles.size() ; i++) {
					SourceFile source = sourceFileMap.get(cluster.sourceFiles.get(i));
					sourceFilesInCluster.add(source);
				}
				
				//create a new cluster for each of the article
				//add the name of the article to the article list
				//add the list of source files which contains any of the unique keywords 
				//to the list of source files of the particular cluster
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					Cluster newCluster = new Cluster();
					newCluster.clusterNo = cluster.clusterNo+"_" + i;
					newCluster.articles.add(articlesInCluster.get(i).name);
					
					System.out.println(clusterNo +"  " + articlesInCluster.get(i).uniqueKeyWordSet);
					
					//find the list of source files by finding the 
					//unique keywords of the article in the source file
					//maintain a hashtable to see how much the article and source file overlaps
					Hashtable<SourceFile, Integer> overLapCount = new Hashtable<SourceFile, Integer>();
					Set<String> sourceFile = new HashSet<String> ();
					for(String keyword:articlesInCluster.get(i).uniqueKeyWordSet) {
						
						for(int j = 0 ; j < sourceFilesInCluster.size(); j++) {
							if(sourceFilesInCluster.get(j).keyWords.contains(" " + keyword + " ")) {
								sourceFile.add(sourceFilesInCluster.get(j).name);
								if(overLapCount.containsKey(sourceFilesInCluster.get(j))) {
									int val = overLapCount.get(sourceFilesInCluster.get(j));
									overLapCount.put(sourceFilesInCluster.get(j), val + 1);
								} else {
									overLapCount.put(sourceFilesInCluster.get(j), 1);
								}
							}
						}
					}
					
					//printing the hashset
					for(SourceFile sf : overLapCount.keySet()) {
						System.out.println(sf.name +":" + overLapCount.get(sf));
					}
					
					//converting the set of sourcefiles to a list
					newCluster.sourceFiles.addAll(sourceFile);
					
					
					//add this cluster to the mainlist of clusters
					clusters.add(newCluster);
					
				}
			
				//now the cluster with more than one article can be removed
				clusters.remove(clusterNo);
				*
				 * end of technique 2
				 */
				
				
				/*
				 * cluster cleaning technique 1
				 *
				 
				
				//the no of articles in this cluster
				int articleListSize  = cluster.articles.size();
				
				//get the articles of this cluster
				List<Article> articlesInCluster = new ArrayList<Article>();
				
				for(int i = 0 ; i < articleListSize ; i++) {
					//retrieve the article with the specific name from the map
					Article article = articleMap.get(cluster.articles.get(i));
					articlesInCluster.add(article);
				}
				
				//for each of the articles in the cluster
				//put the keywords in these clusters into sets
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					String[] keywordArray = articlesInCluster.get(i).getKeyWords().split(" ");
					Set<String> keyWordSet = new HashSet<String>(Arrays.asList(keywordArray));
					 articlesInCluster.get(i).uniqueKeyWords = keyWordSet;
				}
				
				//now remove all the common keywords that are there between any two articles
				// the articles should be left with keywords that are soleley special to them
				//and do not overlap with the keywords of any other article
				for(int i = 0 ; i < articlesInCluster.size(); i++) {
					for(int j = i + 1 ; j < articlesInCluster.size() ; j++) {
						articlesInCluster.get(i).uniqueKeyWords.removeAll(articlesInCluster.get(j).uniqueKeyWords);
						articlesInCluster.get(j).uniqueKeyWords.removeAll(articlesInCluster.get(i).uniqueKeyWords);
					}
				}
				
				//get the list of source files in the cluster
				List<SourceFile> sourceFilesInCluster = new ArrayList<SourceFile> ();
				
				//retrieve the sourcefiles from the SourceFileMap
				for(int i = 0 ; i < cluster.sourceFiles.size() ; i++) {
					SourceFile source = sourceFileMap.get(cluster.sourceFiles.get(i));
					sourceFilesInCluster.add(source);
				}
				
				//create a new cluster for each of the article
				//add the name of the article to the article list
				//add the list of source files which contains any of the unique keywords 
				//to the list of source files of the particular cluster
				for(int i = 0 ; i < articlesInCluster.size() ; i++) {
					Cluster newCluster = new Cluster();
					newCluster.clusterNo = cluster.clusterNo+"_" + i;
					newCluster.articles.add(articlesInCluster.get(i).name);
					
					System.out.println(clusterNo +"  " + articlesInCluster.get(i).uniqueKeyWords);
					
					//find the list of source files by finding the 
					//unique keywords of the article in the source file
					Set<String> sourceFile = new HashSet<String> ();
					for(String keyword:articlesInCluster.get(i).uniqueKeyWords) {
						
						for(int j = 0 ; j < sourceFilesInCluster.size(); j++) {
							if(sourceFilesInCluster.get(j).keyWords.contains(" " + keyword + " ")) {
								sourceFile.add(sourceFilesInCluster.get(j).name);
							}
						}
					}
					
					//converting the set of sourcefiles to a list
					newCluster.sourceFiles.addAll(sourceFile);
					
					
					//add this cluster to the mainlist of clusters
					clusters.add(newCluster);
					
				}
			
				//now the cluster with more than one article can be removed
				clusters.remove(clusterNo);
				
				*
				*
				*Cluster cleaning technique 1
				*/
			}
			
			
		}
		
		
	}
	
	
	
}
