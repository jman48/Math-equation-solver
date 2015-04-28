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
		config.setPopulationSize(200);
		config.setFitnessFunction(new GPMathProblem.FunctionFitnessFormula());
		config.setMutationProb(2f);
		config.setCrossoverProb(1.2f);
		GPMathProblem problem = new GPMathProblem(config, data.getX(), data.getY());
		GPGenotype gp = problem.create();
		
		gp.setVerboseOutput(true);
		gp.evolve(5000);
		gp.outputSolution(gp.getAllTimeBest());  
	}

}
