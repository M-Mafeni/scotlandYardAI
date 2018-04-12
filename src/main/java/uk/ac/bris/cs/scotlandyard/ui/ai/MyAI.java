package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.Dijkstra;
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
		private final Random random = new Random();
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
							 Consumer<Move> callback) {
			weightedGraph = weighGraph(view.getGraph());
			Dijkstra getScores = new Dijkstra(weightedGraph,new Node<>(location));
			List<Node<Integer>> detectiveLoc = getDetectiveLoc(view); //stores all detective locations
			Set<Move> possMoves = new HashSet<>(); //stores a list of possible moves mrX can make
			int avgScore = score(view,getScores,detectiveLoc);
			for(Move m: moves){
				m.visit(this);
				int newAvg = score(view,newPos,detectiveLoc);
				if(newAvg >= avgScore){
					possMoves.add(m);
				}
			}
			if(possMoves.size() != 0)
				callback.accept(new ArrayList<>(possMoves).get(random.nextInt(possMoves.size())));
			else
				callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));
		}

		@Override
		public void visit(PassMove move) {

		}

		@Override
		public void visit(DoubleMove move) {
			newPos = new Dijkstra(weightedGraph,new Node<>(move.finalDestination()));
		}

		@Override
		public void visit(TicketMove move) {
			newPos = new Dijkstra(weightedGraph,new Node<>(move.destination()));
		}

		private int score(ScotlandYardView view, Dijkstra getScores, List<Node<Integer>> detectiveLoc){
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
