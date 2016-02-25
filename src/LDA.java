import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class LDA {
	
	public static void justLDA() throws IOException{
	
			TopicModelling tm = new TopicModelling();
			tm.LDA(200,500, true, -1);
			Ranking.createDocTopMatrix(252 , 200);
			//System.out.println("the best distribution is " + initialPopulation[0][0] + " topics and " + initialPopulation[0][1] + "iterations ");
	}
		
	}
	
	

	