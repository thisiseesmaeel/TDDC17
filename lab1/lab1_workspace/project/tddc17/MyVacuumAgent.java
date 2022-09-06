package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

import tddc17.ShortestPath;
import tddc17.Node;

class MyAgentState
{
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;
	
	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
	    {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
	    }
		
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}
	
	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {
	private Queue<ArrayList<Integer>> q = new LinkedList<>();
	private ArrayList<Integer> goal_square = new ArrayList<>();
	
	private int initnialRandomActions = 10;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public int iterationCounter = 10;
	public MyAgentState state = new MyAgentState();
	
	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction<0) 
		    	state.agent_direction +=4;
		    state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
		    state.agent_last_action = state.ACTION_TURN_RIGHT;
		    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		} 
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	
	@Override
	public Action execute(Percept percept) {
		
		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initnialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initnialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}
		
    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
    	    	
    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);
    	
		
	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);
	    
	    // State update based on the percept value and the last action
	    state.updatePosition((DynamicPercept)percept);
	    if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
	    }
	    if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
	    state.printWorldDebug();
	    
	    
	    // Next action selection based on the percept value
	    if (dirt)
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } 
	    else
	    {
	    	if(home) {
	    		state.agent_last_action=state.ACTION_NONE;
		    	return NoOpAction.NO_OP;
	    	}
	    	else if (bump)
	    	{
	    		state.agent_last_action=state.ACTION_TURN_RIGHT;
		    	return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    	}
	    	else
	    	{
//	    		state.agent_last_action=state.ACTION_MOVE_FORWARD;
//	    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    		
	    		/*
	    		 *  HADI
	    		 */
	    		
	    		if(q.isEmpty()) {
	    			return NoOpAction.NO_OP;
	    		}
	    		
	    		// Adding all neighbor (which are not added yet) of current square to the queue 
	    		//     
	    		//    -
	    		//  - x -
	    		//    -
	    		//
	    		ArrayList<Integer> temp = new ArrayList<>();
	    		
	    		for(int x: Arrays.asList(1,-1)) {
	    			temp.add(state.agent_x_position + x);
	    			temp.add(state.agent_y_position);
	    			if(!q.contains(temp)) {
	    				q.add(temp);
	    			}
	    		}
	    		
	    		for(int y: Arrays.asList(1,-1)) {
	    			temp.add(state.agent_x_position);
	    			temp.add(state.agent_y_position + y);
	    			if(!q.contains(temp)) {
	    				q.add(temp);
	    			}
	    		}
	    		
	    		if(goal_square.isEmpty() || goal_square.equals(Arrays.asList(state.agent_x_position, state.agent_y_position))) {
	    			goal_square = q.remove();
	    		}
	    		
	    		
	    		Integer next_movement = findNextMovement(goal_square);
	    		
	    		if(next_movement == MyAgentState.NORTH) {
	    			
	    			switch (state.agent_direction) {
	    			case MyAgentState.NORTH:
	    				state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    			case MyAgentState.EAST:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			case MyAgentState.SOUTH:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			case MyAgentState.WEST:
	    				state.agent_last_action=state.ACTION_TURN_RIGHT;
	    				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    			}
	    		}
	    		else if(next_movement == MyAgentState.EAST) {
	    			
	    			switch (state.agent_direction) {
	    			case MyAgentState.NORTH:
	    				state.agent_last_action=state.ACTION_TURN_RIGHT;
	    				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    			case MyAgentState.EAST:
	    				state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    			case MyAgentState.SOUTH:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			case MyAgentState.WEST:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			}
	    		}
	    		else if(next_movement == MyAgentState.SOUTH) {
	    			
	    			switch (state.agent_direction) {
	    			case MyAgentState.NORTH:
	    				state.agent_last_action=state.ACTION_TURN_RIGHT;
	    				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    			case MyAgentState.EAST:
	    				state.agent_last_action=state.ACTION_TURN_RIGHT;
	    				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    			case MyAgentState.SOUTH:
	    				state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    			case MyAgentState.WEST:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			}
	    		}
	    		else {
	    			
	    			switch (state.agent_direction) {
	    			case MyAgentState.NORTH:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			case MyAgentState.EAST:
	    				state.agent_last_action=state.ACTION_TURN_LEFT;
	    				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    			case MyAgentState.SOUTH:
	    				state.agent_last_action=state.ACTION_TURN_RIGHT;
	    				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    			case MyAgentState.WEST:
	    				state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    			}
	    		}
	    		
	    		return NoOpAction.NO_OP;
	    		
	    	}
	    }
	}
	
	private Integer findNextMovement(ArrayList<Integer> goal_square) {
		// Find a path to goal and tell me where to go (up, right, down or left?)
		// Finding shortest path with help of BFS
		
		
		
		// Creating a graph representation based on current observation of the world
		ArrayList<Node> nodes = new ArrayList<>();
		for (int i=0; i < state.world.length; i++)
		{
			for (int j=0; j < state.world[i].length ; j++)
			{
				if (state.world[j][i]==state.CLEAR)
					nodes.add(new Node(Arrays.asList(i,j)));
			}
			System.out.println("");
		}
		
		
		// connecting nodes which are neighbors
		for(Node outer_node: nodes) {
			for(Node inner_node: nodes) {
				if(isNeighbor(outer_node, inner_node)) {
					outer_node.add_neighbor(inner_node);
				}
			}
		}
		
		// finding the goal node in graph
		Node goal_node = null;
		for(Node node: nodes) {
			if(node.getX() == goal_square.get(0) && node.getY() == goal_square.get(1)) {
				goal_node = node;
				break;
			}
		}
		
		Node current_node = null;
		for(Node node: nodes) {
			if(node.getX() == state.agent_x_position && node.getY() == state.agent_y_position) {
				current_node = node;
				break;
			}
		}
		
		new ShortestPath(current_node, goal_node).bfs();
		
		return MyAgentState.SOUTH;
		
	}
	
	private boolean isNeighbor(Node node1, Node node2) {
		if(node1.getX() == node2.getX() + 1 && node1.getY() == node2.getY()) {
			return true;
		}
		else if(node1.getX() == node2.getX() - 1 && node1.getY() == node2.getY()) {
			return true;
		}
		else if(node1.getX() == node2.getX() && node1.getY() == node2.getY() + 1) {
			return true;
		}
		else if(node1.getX() == node2.getX() - 1 && node1.getY() == node2.getY() - 1) {
			return true;
		}
		return false;
	}
	
}



public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}



