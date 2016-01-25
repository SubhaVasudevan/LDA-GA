import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class geneticLogic {

	//public static void main(String[] args) throws IOException, InterruptedException {
	public static void geneticLogic() throws IOException, InterruptedException {
		//the initial population of size 6
		int[][] initialPopulation = new int[6][2];
		
		boolean maxFitnessFound = false;
		
		//populating the initial population
		for(int i = 0 ; i < initialPopulation.length ; i++ ) {
			
			//the first value is the number of topics. Assign a range which you think is reasonable
			//the second value is the number of iterations
			initialPopulation[i][0] = (int) Math.floor(Math.random()*10 + 3);
			initialPopulation[i][1] = (int) Math.floor(Math.random()*1000 + 1);
			
			//initialPopulation[i][0] = 2;
		    //initialPopulation[i][1] = 500;
		}
		
		while( !maxFitnessFound) {
			
			//to get the fitness values
			double[] fitnessValues = new double[6];
		
			/**
			 * the total number of documents that are being processed. Put them in a folder and add the folder path here.
			 */
			int numberOfDocuments = new File("txtData").listFiles().length;
		
			//create an instance of the topic modelling class
			TopicModelling tm = new TopicModelling();
		
			//for every chromosome in the initial population
			for( int i = 0 ; i < initialPopulation.length ; i++) {
				
				//invoke the LDA function
				tm.LDA(initialPopulation[i][0], initialPopulation[i][1], false);
						
				//number of topics - the first value
				int numberOfTopics = initialPopulation[i][0];

				//clustermatrix - matrix explaining the distribution of documents into different topics
				//the distibution is written to a text file by the name "distribution.txt"
				double[][] clusterMatrix = new double[numberOfDocuments - 1][numberOfTopics];
				Thread.sleep(2000);

				//reading the values from distribution.txt and populating the cluster matrix
				int rowNumber=0, columnNumber = 0;
				Scanner fileRead = new Scanner( new File("distribution.txt"));
				fileRead.nextLine();
			
				//Map to save the documents that belong to each cluster
				//An arraylist multimap allows to save <Key, Value[]> combination
				//each topic will have a cluster
				Multimap<Integer, Integer> clusterMap = ArrayListMultimap.create();			   
			
				int documentCount = 0;
				//read the values for every document
				while(documentCount < (numberOfDocuments - 1)){
				
					rowNumber = fileRead.nextInt();
					fileRead.next();
				
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
			
			
				//finding the distance of each documents in each cluster finding max distance from other documents in the same cluster
				double[] maxDistanceInsideCluster = new double[numberOfDocuments - 1];
				for(int k: clusterMap.keySet()){
					List<Integer> values = (List<Integer>) clusterMap.get(k);
				
					//for each of the documents find the maxDistance from other cluster members
					for(int y = 0 ; y < values.size() ; y++ ) {
						int docNo = values.get(y);
						maxDistanceInsideCluster[docNo] = 0;
						for(int z = 0 ; z < values.size() ; z++ ) {
							int otherDocNo = values.get(z);
							if(otherDocNo == docNo){
								continue;
							}
						
							//finding euclidean distance between the two points/docuemnts
							double distance = 0;
							for(int h = 0 ; h < numberOfTopics ; h++) {
								distance =  distance + Math.pow((clusterMatrix[otherDocNo][h] - clusterMatrix[docNo][h]), 2);
							}
							distance = Math.sqrt(distance);
							if (distance > maxDistanceInsideCluster[docNo]){
								maxDistanceInsideCluster[docNo] = distance;
							}
						}
					}
				}
			
			
				//finding each documents minimum distance to the centroids of other clusters
				double[] minDistanceOutsideCluster = new double[numberOfDocuments -1];
				for(int k: clusterMap.keySet()){
					List<Integer> values = (List<Integer>) clusterMap.get(k);
				
					//find the documents min distance from the centroid of other clusters
					for(int y = 0 ; y < values.size() ; y++ ) {
						int docNo = values.get(y);
						minDistanceOutsideCluster[docNo] = Integer.MAX_VALUE;
						for(int z = 0 ; z < numberOfTopics ; z++) {

							//don't calculate the distance to the same cluster
							if(z == k) {
								continue;
							}
							double distance = 0;
							for(int h = 0 ; h < numberOfTopics ; h++) {
								distance =  distance + Math.pow((clusterCentroids[z][h] - clusterMatrix[docNo][h]), 2);
							}
							distance = Math.sqrt(distance);
							if (distance < minDistanceOutsideCluster[docNo]){
								minDistanceOutsideCluster[docNo] = distance;
							}
						}
					}
				}
			
				//calculate the Silhouette coefficient for each document
				double[] silhouetteCoefficient = new double[numberOfDocuments - 1];
				for(int m = 0 ; m < (numberOfDocuments-1); m++ ) {
					silhouetteCoefficient[m] = (minDistanceOutsideCluster[m] - maxDistanceInsideCluster[m]) / Math.max(minDistanceOutsideCluster[m],maxDistanceInsideCluster[m]);
				}
			
			
				//find the average of the Silhouette coefficient of all the documents - fitness criteria
				double total = 0;
				for(int m = 0 ; m < (numberOfDocuments-1); m++ ) {
					total = total + silhouetteCoefficient[m]; 
				}
				fitnessValues[i] = total / (numberOfDocuments - 1);		
			}
		
		
			//ranking and ordering the chromosomes based on the fitness function. 
			//We need only the top 1/3rd of the chromosomes with high fitness values - Silhouette coefficient
			int[][] newPopulation = new int[initialPopulation.length][2];
		
			//copy only the top 1/3rd of the chromosomes to the new population 
			for(int i = 0 ; i < (initialPopulation.length / 3) ; i++) {
				double maxFitness = Integer.MIN_VALUE;
				int maxFitnessChromosome = -1;
				for(int j = 0 ; j < initialPopulation.length ; j++) {
					if(fitnessValues[j] > maxFitness) {
						maxFitness = fitnessValues[j];
						
						//stop reproducing or creating new generations if the expected fitness is reached
						/**
						 * Please find what would be a suitable fitness to classify the set of documents that you choose
						 */
						if(maxFitness > 0.75) {
							//run the function again to get the words in each topic
							//the third parameter states that the topics are to be written to a file
							tm.LDA(initialPopulation[j][0],initialPopulation[j][1], true);
							System.out.println("the best distribution is " + initialPopulation[j][0] + " topics and " + initialPopulation[j][1] + "iterations and fitness is " + maxFitness);
							maxFitnessFound = true;
							break;						
						}
						maxFitnessChromosome = j;
					}
				}
				
				if(maxFitnessFound) {
					break;
				}
			
				//copy the chromosome with high fitness to the next generation
				newPopulation[i] = initialPopulation[maxFitnessChromosome];
				fitnessValues[maxFitnessChromosome] = Integer.MIN_VALUE;
			}
			
			if(maxFitnessFound) {
				break;
			}
		
		
			//perform crossover - to fill the rest of the 2/3rd of the initial Population
			for(int i = 0 ; i < initialPopulation.length / 3  ; i++ ) {
				newPopulation[(i+1)*2][0] = newPopulation[i][0];
				newPopulation[(i+1)*2][1] = (int) Math.floor(Math.random()*1000 + 1);
				newPopulation[(i+1)*2+1][0] = (int) Math.floor(Math.random()*10 + 2);
				newPopulation[(i+1)*2+1][1] = newPopulation[i][1];
			}
		
			//substitute the initial population with the new population and continue 
			initialPopulation = newPopulation;
			
			/**The genetic algorithm loop will not exit until the required fitness is reached.
			 * For some cases, we might expect a very high fitness that will never be reached.
			 * In such cases add a variable to check how many times the GA loop is repeated.
			 * Terminate the loop in predetermined number of iterations.
			 */
		}		
		
	}
	
}
	