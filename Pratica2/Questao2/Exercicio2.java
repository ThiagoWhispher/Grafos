package pratica2;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.HierholzerEulerianCycle;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;

/**
 * Questão 2 da prática 2. Determina se é possível encontrar a rota desejada
 * para o problema proposto. Se sim, retorna a rota.
 * 
 * @author  Thiago Yuri Evaristo de Souza - 117211156 
 * 			Igor Silveira de Andrade -  117210286 
 * 			Igor Santa Ritta Seabra - 117210304 
 * 			José Adrião Barbosa Mendes Junior - 117210335
 *
 */
public class Exercicio2 {
	// Conectividade para grafos não-direcionados
	// Testar com connected e unconnected.gml

	public static void main(String[] args) {
		SimpleGraph<DefaultVertex, RelationshipDirectedEdge> mygraph = importaGrafo();
		System.out.println("Caminho Euleriano partindo de C:");
		System.out.println(caminho(mygraph));
	}

	/**
	 * Recebe um grafo, verifica se é euleriano. Caso seja, retorna um ciclo
	 * euleriano partindo de C. Caso não, informa que o grafo não é euleriano.
	 * 
	 * @param g Um grafo simples.
	 * @return O ciclo referente ao problema.
	 */
	private static String caminho(SimpleGraph<DefaultVertex, RelationshipDirectedEdge> g) {
		HierholzerEulerianCycle<DefaultVertex, RelationshipDirectedEdge> eulertour = new HierholzerEulerianCycle<DefaultVertex, RelationshipDirectedEdge>();

		try {
			eulertour.isEulerian(g);
		} catch (IllegalArgumentException e) {
			return "O grafo não é euleriano";
		}

		GraphPath<DefaultVertex, RelationshipDirectedEdge> walk = eulertour.getEulerianCycle(g);
		List<RelationshipDirectedEdge> result = new LinkedList<RelationshipDirectedEdge>();

		boolean flag = false;
		for (RelationshipDirectedEdge e : walk.getEdgeList()) {
			if (e.getTarget().toString().equals("C")) {
				flag = true;
			}
			if (flag) {
				result.add(e);
			}
		}

		flag = true;
		for (RelationshipDirectedEdge e : walk.getEdgeList()) {
			if (e.getTarget().toString().equals("C")) {
				break;
			}
			result.add(e);
		}
		return result.toString();
	}

	/**
	 * Importa o grafo rede.gml para um objeto do tipo SimpleGraph e o retorna.
	 * @return Grafo importado.
	 */
	private static SimpleGraph<DefaultVertex, RelationshipDirectedEdge> importaGrafo() {
		VertexProvider<DefaultVertex> vp1 = (label, attributes) -> new DefaultVertex(label, attributes);
		EdgeProvider<DefaultVertex, RelationshipDirectedEdge> ep1 = (from, to, label,
				attributes) -> new RelationshipDirectedEdge(from, to, attributes);
		GmlImporter<DefaultVertex, RelationshipDirectedEdge> gmlImporter = new GmlImporter<>(vp1, ep1);
		SimpleGraph<DefaultVertex, RelationshipDirectedEdge> graphgml = new SimpleGraph<>(
				RelationshipDirectedEdge.class);
		try {
			gmlImporter.importGraph(graphgml, ImportGraph.readFile("/home/whispher/Desktop/Pratica2/Questao2/rede.gml"));
			return graphgml;
		} catch (ImportException e) {
			throw new RuntimeException(e);
		}
	}
}