import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class Ranking {
	
	static double[][] clusterMatrix;
	static Hashtable<Integer, String> docName = new Hashtable<Integer, String>();
	
	public static void createDocTopMatrix( int numberOfDocuments, int numberOfTopics) throws FileNotFoundException, UnsupportedEncodingException{
		clusterMatrix = new double[numberOfDocuments - 1][numberOfTopics];
		
		//reading the values from distribution.txt and populating the cluster matrix
		int rowNumber=0, columnNumber = 0;
		Scanner fileRead = null;
		try {
			fileRead = new Scanner( new File("distribution-1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileRead.nextLine();

		//Map to save the documents that belong to each cluster
		//An arraylist multimap allows to save <Key, Value[]> combination
		//each topic will have a cluster
		Multimap<Integer, Integer> clusterMap = ArrayListMultimap.create();			   

		int documentCount = 0;
		//read the values for every document
		while(documentCount < (numberOfDocuments - 1)){
	
			rowNumber = fileRead.nextInt();
			String name = fileRead.next();

			
			docName.put(rowNumber, name);
	
			for(int z = 0 ; z < numberOfTopics  ; z++) {
				columnNumber = fileRead.nextInt();

				if( z == 0 ){
					clusterMap.put(columnNumber,rowNumber);  
				}
				clusterMatrix[rowNumber][columnNumber] = fileRead.nextDouble();
			}
			documentCount = documentCount + 1;
		}
		fileRead.close();
		
		//getting the centroid of each cluster by calculating the average of their cluster distribution
		double[][] clusterCentroids = new double[numberOfTopics][numberOfTopics];
		for(int k: clusterMap.keySet()){
			List<Integer> values = (List<Integer>) clusterMap.get(k);
	
			for(int j = 0 ; j < values.size() ; j++) {
				int docNo = values.get(j);
				for(int y = 0 ; y < numberOfTopics ; y++ ) {
					clusterCentroids[k][y] = clusterCentroids[k][y] + clusterMatrix[docNo][y];
				}
			}
			for(int y = 0 ; y < numberOfTopics ; y++ ) {
				clusterCentroids[k][y] = clusterCentroids[k][y] / values.size();
			}
		}
		
		List<Integer> allvalues = new ArrayList<Integer>();
		
		
		
		//find max distance from the center of the cluster
		double[] distanceFromCenter = new double[numberOfDocuments - 1];
		for(int k: clusterMap.keySet()){
			List<Integer> values = (List<Integer>) clusterMap.get(k);
			allvalues.addAll(values);
	
			//for each of the documents find the distance from center of cluster
			for(int y = 0 ; y < values.size() ; y++ ) {
				int docNo = values.get(y);
				distanceFromCenter[docNo] = 0;
				
					for(int h = 0 ; h < numberOfTopics ; h++) {
						distanceFromCenter[docNo] =  distanceFromCenter[docNo] + Math.pow((clusterCentroids[k][h] - clusterMatrix[docNo][h]), 2);
					}
					distanceFromCenter[docNo] = Math.sqrt(distanceFromCenter[docNo]);
					
				}
			}
		
		
		//for every cluster collect the files and sort them based on the distance from the center of the cluster
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		PrintWriter writer = new PrintWriter("Ranking_Within_Cluster.txt", "UTF-8");

		for(int k: clusterMap.keySet()){
			List<Integer> values = (List<Integer>) clusterMap.get(k);
	
			List<DocDistance> ddd = new ArrayList<DocDistance>();
			//for each of the documents find the distance from center of cluster
			for(int y = 0 ; y < values.size() ; y++ ) {
				int docNo = values.get(y);
				ddd.add(new DocDistance(docNo, distanceFromCenter[docNo]));
			}
			
			Collections.sort(ddd);
			writer.print("cluster" + k +"     ");
			for(int i = 0 ; i < ddd.size() ; i++) {
				writer.print(docName.get(ddd.get(i).docNo)+"-"+ddd.get(i).distance+"  ");
			}
			writer.println();
		}
		writer.close();

		
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		writer = new PrintWriter("Ranking_Of_All_Documents.txt", "UTF-8");

		//distance of all sourcefiles from the center of a cluster
		for(int k: clusterMap.keySet()){
			
			double[] distanceFromClustercenter = new double[numberOfDocuments - 1];
			
			List<DocDistance> ddd = new ArrayList<DocDistance>();
			//for each of the documents find the distance from center of cluster
			for(int y = 0 ; y < allvalues.size() ; y++ ) {
				int docNo = allvalues.get(y);
				
				for(int h = 0 ; h < numberOfTopics ; h++) {
					distanceFromClustercenter[docNo] =  distanceFromClustercenter[docNo] + Math.pow((clusterCentroids[k][h] - clusterMatrix[docNo][h]), 2);
				}
				distanceFromClustercenter[docNo] = Math.sqrt(distanceFromClustercenter[docNo]);
				
			
				
				ddd.add(new DocDistance(docNo, distanceFromClustercenter[docNo]));
			}
			
			Collections.sort(ddd);
			writer.print("cluster" + k +"     ");
			for(int i = 0 ; i < ddd.size() ; i++) {
				writer.print(docName.get(ddd.get(i).docNo)+"-"+ddd.get(i).distance+"  ");
			}
			writer.println();
		}
		
		writer.close();
		

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
	}
	
}

class DocDistance implements Comparable<DocDistance>{
	int docNo;
	double distance;
	
	DocDistance( int no, double d) {
		docNo = no;
		distance = d;
	}
	
	public int compareTo(DocDistance o) {
		// TODO Auto-generated method stub
		if(o.distance > this.distance) {
			return -1;
		} else {
			return 1;
		}
	}



}


