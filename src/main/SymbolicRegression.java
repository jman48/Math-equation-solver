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

	public static void main(String[] args) throws InvalidConfigurationException, FileNotFoundException {
		DataReader data;
		
		//Check arg length
		if(args.length < 1) {
			data = new DataReader("regressionold.txt");
		} else {
			data = new DataReader(args[0]);
		}
		
		GPConfiguration config = new GPConfiguration();
		config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
		config.setMaxInitDepth(6);
		config.setPopulationSize(100);
		config.setFitnessFunction(new GPMathProblem.FunctionFitnessFormula());
		GPMathProblem problem = new GPMathProblem(config, data.getX(), data.getY());
		GPGenotype gp = problem.create();
		
		gp.setVerboseOutput(true);
		gp.evolve(10000);
		gp.outputSolution(gp.getAllTimeBest());  
	}

}
