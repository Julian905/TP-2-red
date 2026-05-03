package tp2conectando;

import tp2conectando.Conexion;
import tp2conectando.Localidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planificador {
    private CalculadorDeCostos calc;

    public Planificador(CalculadorDeCostos calc) {
        this.calc = calc;
    }

    public List<Conexion> calcularAGM(List<Localidad> locs) {
        if (locs == null || locs.size() < 2) {
            return new ArrayList<>();
        }

        List<Conexion> conexiones = new ArrayList<>();

        // Generar grafo completo
        for (int i = 0; i < locs.size(); i++) {
            for (int j = i + 1; j < locs.size(); j++) {
                Localidad a = locs.get(i);
                Localidad b = locs.get(j);
                conexiones.add(new Conexion(a, b, calc.calcular(a, b)));
            }
        }

        // Ordenar aristas por costo de menor a mayor
        Collections.sort(conexiones);

        UnionFind uf = new UnionFind(locs);
        List<Conexion> arbol = new ArrayList<>();

        // Construir el Árbol Generador Mínimo
        for (Conexion c : conexiones) {
            if (!uf.find(c.getOrigen()).equals(uf.find(c.getDestino()))) {
                arbol.add(c);
                uf.union(c.getOrigen(), c.getDestino());
            }
        }

        return arbol;
    }
}
