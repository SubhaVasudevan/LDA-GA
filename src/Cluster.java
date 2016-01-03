import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
	List<String> keywords = new ArrayList<String>();
	
	//each topic will and cluster will also be given a cluster/topicno
	String clusterNo = "-1";
	
	/*public void Cluster (List<Document> documents) {
		//if the document name has an _a in it classify it as a article else 
		//classify it as a source file
		
		for(Document d: documents) {
			if(d.getName().contains("_a")) {
				articles.add((Article) d);
			} else {
				sourceFiles.add((SourceFile) d);
			}
		}
	}*/
	
	
	//reads the data from the topic.txt and distribution.txt
	//create cluster for each topic
	//assigns the keywords to the appropraite topic
	public static List<Cluster> createClusters() {
		
		//
		//
		//
		System.out.println("inside create clusters");
		//
		//
		//
		
		
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
		 
		 System.out.println("Successfully scanned the file");
		 
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
		        	if(name.contains("_a")) {
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
		 
		System.out.println("returning clusters");
		return clusters;
		
	}
	
	//find clusters that have 2 articles in them
	//pick these clusters and identify the words that are unique to each of these articles
	//classify the source files into these articles
	//create new cluster for each of these article and add them to the main list, 
	//remove the cluster which had more then one article from the main list "clusters"
	public static void cleanCluster(List<Cluster> clusters, Map<String, Article> articleMap, Map<String, SourceFile> sourceFileMap) {
		int clusterNo = 0;
		
		System.out.println("inside Clean cluster");
		
		//this is to make sure all the clusters are checked
		while(clusterNo < clusters.size()) {
			
			//get each cluster
			Cluster cluster = clusters.get(clusterNo);
			
			//check if the cluster has 1 article or more than 1 article
			if(cluster.articles.size() == 1) {
				
				//go check the next cluster
				clusterNo++;
			} else {
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
			}
			
			
		}
		
		
	}
	
	
	
}
