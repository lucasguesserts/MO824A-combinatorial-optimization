/**
 *
 */
package tabusearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import problems.Evaluator;
import solutions.Solution;

public abstract class AbstractTS<E, V extends Number> {

	public static boolean verbose = true;

	static Random rng = new Random(0);

	protected Evaluator<E, V> ObjFunction;

	protected Double bestCost;

	protected Double cost;

	protected Solution<E, V> bestSol;

	protected Solution<E, V> sol;

	protected Integer iterations;

	protected Integer tenure;

	protected ArrayList<E> CL;

	protected ArrayList<E> RCL;

	protected ArrayDeque<E> TL;

	public abstract ArrayList<E> makeCL();

	public abstract ArrayList<E> makeRCL();

	public abstract ArrayDeque<E> makeTL();

	public abstract void updateCL();

	public abstract Solution<E, V> createEmptySol();

	/**
	 * The TS local search phase is responsible for repeatedly applying a
	 * neighborhood operation while the solution is getting improved, i.e.,
	 * until a local optimum is attained. When a local optimum is attained
	 * the search continues by exploring moves which can make the current
	 * solution worse. Cycling is prevented by not allowing forbidden
	 * (tabu) moves that would otherwise backtrack to a previous solution.
	 *
	 * @return An local optimum solution.
	 */
	public abstract Solution<E, V> neighborhoodMove();

	/**
	 * Constructor for the AbstractTS class.
	 *
	 * @param objFunction
	 *            The objective function being minimized.
	 * @param tenure
	 *            The Tabu tenure parameter.
	 * @param iterations
	 *            The number of iterations which the TS will be executed.
	 */
	public AbstractTS(Evaluator<E, V> objFunction, Integer tenure, Integer iterations) {
		this.ObjFunction = objFunction;
		this.tenure = tenure;
		this.iterations = iterations;
	}

	/**
	 * The TS constructive heuristic, which is responsible for building a
	 * feasible solution by selecting in a greedy fashion, candidate
	 * elements to enter the solution.
	 *
	 * @return A feasible solution to the problem being minimized.
	 */
	public Solution<E, V> constructiveHeuristic() {
		CL = makeCL();
		RCL = makeRCL();
		sol = createEmptySol();
		cost = sol.getCost().doubleValue();
		while (!constructiveStopCriteria()) {
			Double maxCost = Double.NEGATIVE_INFINITY, minCost = Double.POSITIVE_INFINITY;
			cost = sol.getCost().doubleValue();
			updateCL();
			/*
			 * Explore all candidate elements to enter the solution, saving the
			 * highest and lowest cost variation achieved by the candidates.
			 */
			for (E c : CL) {
				Double deltaCost = ObjFunction.evaluateInsertionCost(c, sol).doubleValue();
				if (deltaCost < minCost)
					minCost = deltaCost;
				if (deltaCost > maxCost)
					maxCost = deltaCost;
			}
			/*
			 * Among all candidates, insert into the RCL those with the highest
			 * performance.
			 */
			for (E c : CL) {
				Double deltaCost = ObjFunction.evaluateInsertionCost(c, sol).doubleValue();
				if (deltaCost <= minCost) {
					RCL.add(c);
				}
			}
			/* Choose a candidate randomly from the RCL */
			int rndIndex = rng.nextInt(RCL.size());
			E inCand = RCL.get(rndIndex);
			CL.remove(inCand);
            final var insertionIncrement = ObjFunction.evaluateInsertionCost(inCand, sol);
			sol.add(inCand, insertionIncrement);
			ObjFunction.evaluate(sol);
			RCL.clear();

		}

		return sol;
	}

	/**
	 * The TS mainframe. It consists of a constructive heuristic followed by
	 * a loop, in which each iteration a neighborhood move is performed on
	 * the current solution. The best solution is returned as result.
	 *
	 * @return The best feasible solution obtained throughout all iterations.
	 */
	public Solution<E, V> solve() {

		bestSol = createEmptySol();
		constructiveHeuristic();
		TL = makeTL();
		for (int i = 0; i < iterations; i++) {
			neighborhoodMove();
			if (bestSol.getCost().doubleValue() > sol.getCost().doubleValue()) {
				bestSol = sol.clone();
				if (verbose)
					System.out.println("(Iter. " + i + ") BestSol = " + bestSol);
			}
		}

		return bestSol;
	}

	/**
	 * A standard stopping criteria for the constructive heuristic is to repeat
	 * until the incumbent solution improves by inserting a new candidate
	 * element.
	 *
	 * @return true if the criteria is met.
	 */
	public Boolean constructiveStopCriteria() {
		return (cost > sol.getCost().doubleValue()) ? false : true;
	}

}
