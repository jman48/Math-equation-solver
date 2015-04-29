package main;

import java.io.FileNotFoundException;
import java.util.Random;

import mathProblem.GPMathProblem;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;

public class SymbolicRegression {
	
	private static final String defaultFileName = "regression.txt";
	private static final int maxGenerations = 2000;

	public static void main(String[] args) throws InvalidConfigurationException, FileNotFoundException {
		DataReader data;
		
		//Check arg length
		if(args.length < 1) {
			System.out.println("No data filename provided. Using default data filename");
			
			System.out.println("Reading data from: " + defaultFileName);
			data = new DataReader(defaultFileName);
		} else {
			System.out.println("Reading data from: " + args[0]);
			data = new DataReader(args[0]);
		}
		
		GPConfiguration config = new GPConfiguration();
		config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
		config.setMaxInitDepth(6);
		config.setPopulationSize(100);
		config.setFitnessFunction(new GPMathProblem.FunctionFitnessFormula());
//		config.setMutationProb(1f);
		config.setCrossoverProb(90f);
		GPMathProblem problem = new GPMathProblem(config, data.getX(), data.getY());
		GPGenotype gp = problem.create();
		
		gp.setVerboseOutput(true);
		
		System.out.println("Evolving to a maximum of " + maxGenerations + " generations");
		for(int i = 0; i < maxGenerations; i++) {
			gp.evolve(1);
			if(gp.getAllTimeBest() != null && gp.getAllTimeBest().getFitnessValue() == 0) {
				System.out.println("\nFound a program with fitness of 0.0 after " + i + " generations\n");
				break;
			}
		}
		gp.outputSolution(gp.getAllTimeBest());  
	}

}
