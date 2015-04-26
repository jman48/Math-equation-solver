package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataReader {
	private List<Double> x = new ArrayList<Double>();

	private List<Double> y = new ArrayList<Double>();

	public DataReader(String fileName) throws FileNotFoundException {
		Scanner in = new Scanner(new File(fileName));
		
		//Skip through the headers
		while(!in.hasNextDouble()) {
			in.nextLine();
		}
		
		while(in.hasNextLine() && in.hasNextDouble()) {
			x.add(in.nextDouble());
			y.add(in.nextDouble());
		}
		
		in.close();
	}
	
	public List<Double> getX() {
		return new ArrayList<Double>(x);
	}

	public List<Double> getY() {
		return new ArrayList<Double>(y);
	}
}
