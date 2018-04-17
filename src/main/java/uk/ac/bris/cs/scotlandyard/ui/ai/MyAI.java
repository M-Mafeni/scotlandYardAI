package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.*;
import static uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra.weighGraph;

// TODO name the AI
@ManagedAI("Moriarty")
public class MyAI implements PlayerFactory {

	// TODO create a new player here
	@Override
	public Player createPlayer(Colour colour) {
		if(colour.isMrX())
			return new MrXplayer();
		return new MyPlayer();
	}

	// TODO A sample player that selects a random move
	private static class MyPlayer implements Player {

		private final Random random = new Random();

		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
				Consumer<Move> callback) {
			// TODO do something interesting here; find the best move
			// picks a random move
			callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));

		}
	}
	private static class MrXplayer implements Player,MoveVisitor{
		Graph<Integer,Integer> weightedGraph;
		Dijkstra newPos;
		Graph<Integer,Transport> gameGraph;
		private final Random random = new Random();
		int bMax = 0;
		Move bestMove = null;
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
							 Consumer<Move> callback) {
			gameGraph = view.getGraph();
			weightedGraph = weighGraph(gameGraph);
			Dijkstra getScores = new Dijkstra(weightedGraph,new Node<>(location));
			List<Node<Integer>> detectiveLoc = getDetectiveLoc(view); //stores all detective locations
			Set<Move> possMoves = new HashSet<>(); //stores a list of possible moves mrX can make
			int avgScore = score(getScores,detectiveLoc);
			for(Move m: moves){
				m.visit(this);
				int newAvg = score(newPos,detectiveLoc);
				if(newAvg >= avgScore){
					possMoves.add(m);
				}
			}
			for(Move m : possMoves){
				m.visit(this);
			}

			if(bestMove != null)
				callback.accept(bestMove);
			else
				callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));
		}

		@Override
		public void visit(PassMove move) {

		}

		@Override
		public void visit(DoubleMove move) {
			newPos = new Dijkstra(weightedGraph,new Node<>(move.finalDestination()));
			if(gameGraph.getEdgesFrom(new Node<>(move.finalDestination())).size() > bMax){
				bestMove = move;
			}
		}

		@Override
		public void visit(TicketMove move) {
			newPos = new Dijkstra(weightedGraph,new Node<>(move.destination()));
			if(gameGraph.getEdgesFrom(new Node<>(move.destination())).size() > bMax){
				bestMove = move;
			}
		}


		private int score(Dijkstra getScores, List<Node<Integer>> detectiveLoc){
			int total = 0;
			int avgScore;
			for(Node<Integer> n: detectiveLoc){
				total += getScores.getDistanceFrom(n);
			}
			avgScore = total/detectiveLoc.size();
			return avgScore;
		}
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
