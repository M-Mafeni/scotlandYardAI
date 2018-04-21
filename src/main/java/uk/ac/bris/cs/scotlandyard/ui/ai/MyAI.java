package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.*;
import static uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra.weighGraph;
/* AI program for mr X which works by
  picking the move which will put him furthest from the detectives
*/

@ManagedAI("Moriarty")
public class MyAI implements PlayerFactory {

	@Override
	public Player createPlayer(Colour colour) {
		//detectives should move randomly
		if(colour.isMrX())
			return new MrXplayer();
		return new MyPlayer();
	}

	private static class MyPlayer implements Player {

		private final Random random = new Random();

		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
				Consumer<Move> callback) {
			// picks a random move
			callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));

		}
	}
	private static class MrXplayer implements Player,MoveVisitor{
		Graph<Integer,Integer> weightedGraph; //
		Dijkstra newPos;
		Graph<Integer,Transport> gameGraph;
		private final Random random = new Random();
		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
							 Consumer<Move> callback) {
			Move bestMove = null;
			double max = 0;
			gameGraph = view.getGraph();
			weightedGraph = weighGraph(gameGraph);
			//contains the distances from all nodes to mr X's location
			Dijkstra getScores = new Dijkstra(weightedGraph,new Node<>(location));
			List<Node<Integer>> detectiveLoc = getDetectiveLoc(view); //stores all detective locations
			double avgScore = score(getScores,detectiveLoc);
			for(Move m: moves){
				m.visit(this);
				double newAvg = score(newPos,detectiveLoc);
				if(newAvg >= avgScore){ //only add moves that have a higher score
					if(newAvg > max)
						//the best move is the one which increases the distance from the detectives the most
						bestMove = m;
						max = newAvg;
				}
			}
			if(bestMove != null)
				callback.accept(bestMove);
			else
				//if no best move is found then simply pick a random move
				callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));
		}

		@Override
		public void visit(PassMove move) {

		}

		@Override
		public void visit(DoubleMove move) {
			//calculates what the distance would be from the new destination
			newPos = new Dijkstra(weightedGraph,new Node<>(move.finalDestination()));
		}

		@Override
		public void visit(TicketMove move) {
			//calculates what the distance would be from the new destination
			newPos = new Dijkstra(weightedGraph,new Node<>(move.destination()));
		}

		//averages the distances from mrX to the detective
		private double score(Dijkstra getScores, List<Node<Integer>> detectiveLoc){
			int total = 0;
			double avgScore;
			for(Node<Integer> n: detectiveLoc){
				total += getScores.getDistanceFrom(n);
			}
			avgScore = total/detectiveLoc.size();
			return avgScore;
		}
		//returns a list of all detective locations
		private List<Node<Integer>> getDetectiveLoc(ScotlandYardView view){
			List<Node<Integer>> detectiveLoc = new ArrayList<>(); //stores all detective locations
			for(Colour x:view.getPlayers()){
				if(!x.isMrX()){
					if(view.getPlayerLocation(x).isPresent())
						detectiveLoc.add(new Node<>(view.getPlayerLocation(x).get()));
					else
						throw new IllegalArgumentException("Location doesn't exist");
				}
			}
			return detectiveLoc;
		}
	}

}
