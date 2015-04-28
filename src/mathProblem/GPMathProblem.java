package mathProblem;

import java.util.List;
import java.util.Random;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.GPProblem;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

public class GPMathProblem extends GPProblem {
	/** String containing the CVS revision. Read out via reflection! */
	private final static String CVS_REVISION = "$Revision: 1.25 $";

	public static Variable vx;

	protected static Float[] x = new Float[20];

	protected static Float[] y = new Float[20];

	private final GPConfiguration config;

	public GPMathProblem(GPConfiguration a_conf, List<Double> input, List<Double> output)
			throws InvalidConfigurationException {
		super(a_conf);
		config = a_conf;
		
		for (int i = 0; i < 20; i++) {
			x[i] = input.get(i).floatValue();
			y[i] = output.get(i).floatValue();
			System.out.println(i + ") " + x[i] + " " + y[i]);
		}
	}

	@Override
	public GPGenotype create() throws InvalidConfigurationException {
		Class[] types = { CommandGene.FloatClass };
		Class[][] argTypes = { {} };

		// Define the commands and terminals the GP is allowed to use.
		// -----------------------------------------------------------
		CommandGene[][] nodeSets = { {
				vx = Variable.create(config, "X", CommandGene.FloatClass),
				new Add(config, CommandGene.FloatClass),
				new Subtract(config, CommandGene.FloatClass),
				new Multiply(config, CommandGene.FloatClass),
				new Divide(config, CommandGene.FloatClass),
				new Pow(config, CommandGene.FloatClass),
				new Exp(config, CommandGene.FloatClass),
				// Use terminal with possible value from 2.0 to 10.0 decimal
				new Terminal(config, CommandGene.FloatClass, 2.0d, 10.0d, false), } };
		
		// Create genotype with initial population.
		// Allow max. 100 nodes within one program.
		// ----------------------------------------
		return GPGenotype.randomInitialGenotype(config, types, argTypes,
				nodeSets, 100, true);
	}

	public static class FunctionFitnessFormula extends GPFitnessFunction {

		private static final long serialVersionUID = 1L;

		@Override
		protected double evaluate(IGPProgram ind) {
			double error = 0.0f;
			Object[] noargs = new Object[0];
			// Evaluate function for input numbers 0 to 20.
			// --------------------------------------------
			for (int i = 0; i < 20; i++) {
				// Provide the variable X with the input number.
				// See method create(), declaration of "nodeSets" for where X is
				// defined.
				// -------------------------------------------------------------
				vx.set(x[i]);
				try {
					// Execute the GP program representing the function to be
					// evolved.
					// As in method create(), the return type is declared as
					// float (see
					// declaration of array "types").
					// ----------------------------------------------------------------
					double result = ind.execute_float(0, noargs);
					// Sum up the error between actual and expected result to
					// get a defect
					// rate.
					// -------------------------------------------------------------------
					error += Math.abs(result - y[i]);
					// If the error is too high, stop evlauation and return
					// worst error
					// possible.
					// ----------------------------------------------------------------
					if (Double.isInfinite(error)) {
						return Double.MAX_VALUE;
					}
				} catch (ArithmeticException ex) {
					// This should not happen, some illegal operation was
					// executed.
					// ------------------------------------------------------------
					System.out.println("x = " + x[i].floatValue());
					System.out.println(ind);
					throw ex;
				}
			}
			// In case the error is small enough, consider it perfect.
			// -------------------------------------------------------
			if (error < 0.001) {
				error = 0.0d;
			}
			return error;
		}

	}
}
