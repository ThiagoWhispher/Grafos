import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.GmlImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.alg.clique.DegeneracyBronKerboschCliqueFinder;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.alg.scoring.AlphaCentrality;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.alg.scoring.ClosenessCentrality;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class Questao2 {
	// Main
	public static void main(String[] args) {
		Graph<DefaultVertex, RelationshipEdge> graph1;
		graph1 = importGraph1("/home/thiagoyes/eclipse-workspace/Grafos/Pratica3/Questao2/Graph.gml");
		Graph<Object, RelationshipEdge> graph2;
		graph2 = importGraph2("/home/thiagoyes/eclipse-workspace/Grafos/Pratica3/Questao2/Graph.gml");
		metricasNoGrafo(graph1);
		cliquesMaximosNoGrafo(graph2);
	}
	
	private static void cliquesMaximosNoGrafo(Graph<Object, RelationshipEdge> graph) {
		System.out.print("\nCliques Maximos: \n");
		
		PivotBronKerboschCliqueFinder <Object,RelationshipEdge> cf3 = 
                new PivotBronKerboschCliqueFinder <> (graph); 
        Iterator  <Set <Object>> it3 = cf3.iterator();
        System.out.println("\nUtilizando PivotBronKerboschCliqueFinder:");
        while (it3.hasNext()) {
            System.out.print(it3.next() + " ");
             
        }
        
        DegeneracyBronKerboschCliqueFinder <Object,RelationshipEdge> cf2 = 
                new DegeneracyBronKerboschCliqueFinder <> (graph); 
        Iterator  <Set <Object>> it2 = cf2.iterator();
        System.out.print("\n\nUtilizando DegeneracyBronKerboschCliqueFinder: \n");
        while (it2.hasNext()) {
            System.out.print(it2.next() + " ");
        }
	}

	private static Graph<DefaultVertex, RelationshipEdge> importGraph1(String path) {
		Graph<DefaultVertex, RelationshipEdge> graphgml = new SimpleGraph<>(RelationshipEdge.class);
        VertexProvider <DefaultVertex> vp1 = 
                (label,attributes) -> new DefaultVertex (label,attributes);
        EdgeProvider <DefaultVertex,RelationshipEdge> ep1 = 
                (from,to,label,attributes) -> new RelationshipEdge(from,to,attributes);
        GmlImporter <DefaultVertex,RelationshipEdge> gmlImporter = new GmlImporter <> (vp1,ep1);
        try {
            gmlImporter.importGraph(graphgml, 
                    ImportGraph.readFile(path));
          } catch (ImportException e) {
            throw new RuntimeException(e);
        }
        return graphgml;
	}
	
	private static Graph<Object, RelationshipEdge> importGraph2(String path){
		VertexProvider <Object> vp1 = 
                (label,attributes) -> new DefaultVertex (label,attributes);
        EdgeProvider <Object,RelationshipEdge> ep1 = 
                (from,to,label,attributes) -> new RelationshipEdge(from,to,attributes);
        GmlImporter <Object,RelationshipEdge> gmlImporter = new GmlImporter <> (vp1,ep1);
        Graph<Object, RelationshipEdge> graphgml = new SimpleGraph<>(RelationshipEdge.class);
        try {
            gmlImporter.importGraph(graphgml, 
                    ImportGraph.readFile(path));
          } catch (ImportException e) {
            throw new RuntimeException(e);
          }  
        return graphgml;
	}
	
	public static void metricasNoGrafo(Graph<DefaultVertex, RelationshipEdge> graph) { 
		// Compute Metrics
        
        System.out.println("As seguintes métricas para cada vértice do grafo são:");

        //Closeness centrality
        System.out.println("\n-CLOSENESS CENTRALITY- ");
        ClosenessCentrality <DefaultVertex, RelationshipEdge> cc = 
                new ClosenessCentrality <> (graph);
        printOrderedDouble (cc.getScores());
        
        //Alpha centrality
        System.out.println("\n\n-ALPHA CENTRALITY- ");
        AlphaCentrality <DefaultVertex, RelationshipEdge> ac = 
                new AlphaCentrality <> (graph,0.1);
        printOrderedDouble (ac.getScores());
        
        //Betweeness centrality
        System.out.println("\n\n-BETWEENESS CENTRALITY- ");
        BetweennessCentrality <DefaultVertex, RelationshipEdge> bc = 
                new BetweennessCentrality <> (graph,true);
        printOrderedDouble (bc.getScores());
        
        System.out.println("\n");
        System.out.println("As seguintes métricas para o grafo:");
        
        //Coeficiente de clutering
        double triplets = get_NTriplets(graph);
        //System.out.println("\nN. Triplets: " + triplets);
        double triangles = get_NTriangles(graph);
        //System.out.println("N. Triangles: " + triangles);
        double coefCluster = 3*triangles/triplets;
        System.out.println("\nCOEFICIENTE DE CLUTERING: " + new Double(coefCluster));
        
        //Diametro e distancia
        int diameter = 0;
        ArrayList <Integer> a = get_allpathLenghts(graph);
            int sum = 0;
        for(int i=0; i < a.size() ; i++) {
              sum = sum + a.get(i);
             if (diameter<a.get(i)) {
                 diameter = a.get(i);
             }
        }
        double average = sum / a.size();
        System.out.println("DISTANCIA: " + average);
        System.out.println("DIAMETRO: " + diameter); 
        
	}
	
	// Métodos Auxiliares
	
	static <V> void printOrderedDouble (Map <V,Double> M) {
        Set<Entry<V, Double>> set = M.entrySet();
        List<Entry<V, Double>> list = new ArrayList<Entry<V, Double>>(set);
        Collections.sort( list, new Comparator<Map.Entry<V, Double>>()
        {
            public int compare( Map.Entry<V, Double> o1, Map.Entry<V, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        for(Map.Entry<V,Double> entry:list){
            System.out.print(entry.getKey()+": "+ String.format("%.2f",(entry.getValue()))+ "; ");
        }   
    }
	
	static <V,E> double get_NTriangles (Graph <V,E> g) {
        double triangles = 0;
        PatonCycleBase <V,E> pc = new PatonCycleBase <> (g);
        Iterator <List<E>> it2 = ((pc.getCycleBasis()).getCycles()).iterator();
        while (it2.hasNext()) {
            List <E> s = it2.next();
            if ((s).size()==3) {
                //System.out.println(s);
                triangles++;
            }
        }
        return triangles;
    }
	
	static <V,E> double get_NTriplets (Graph <V, E> g) {
	     
        double triplets = 0;
        BreadthFirstIterator <V,E> cfi = 
                new BreadthFirstIterator <> (g);
        while (cfi.hasNext()) {
            V v = cfi.next();
            int n = (g.edgesOf(v)).size();
            if (n >=2) {
                triplets = triplets + fact(n) / (2*fact(n-2));
            }           
        }
        return triplets;
    }
	
    static int fact (int n) {
        if (n == 1 || n == 0) {
            return 1;
        } else {
            return n * fact(n-1);
        }
    }
    
    static <V,E> ArrayList <Integer> get_allpathLenghts (Graph <V,E> g) {
        DijkstraShortestPath <V,E>  p = 
                new DijkstraShortestPath <> (g);
        ArrayList <Integer> a = new ArrayList <Integer> ();
        BreadthFirstIterator <V,E> pf = 
                new BreadthFirstIterator <> (g);
        while (pf.hasNext()) {
            V v1 = pf.next();
            Iterator <V> vs = g.vertexSet().iterator();
            while (vs.hasNext()) {
                V v2 = vs.next();
                int dist = (p.getPath(v1, v2)).getLength();
                if (v1.equals(v2) == false) {
                    a.add(dist);
                }
            }           
        }
        return a;
    }
}
