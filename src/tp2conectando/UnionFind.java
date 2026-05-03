package tp2conectando;

import tp2conectando.Localidad;
import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private Map<Localidad, Localidad> padre = new HashMap<>();
    private Map<Localidad, Integer> rango = new HashMap<>();

    public UnionFind(Iterable<Localidad> locs) {
        for (Localidad l : locs) {
            padre.put(l, l);
            rango.put(l, 0);
        }
    }

    public Localidad find(Localidad l) {
        if (!padre.get(l).equals(l)) {
            padre.put(l, find(padre.get(l))); // Compresión de camino
        }
        return padre.get(l);
    }

    public void union(Localidad a, Localidad b) {
        Localidad raizA = find(a);
        Localidad raizB = find(b);

        if (!raizA.equals(raizB)) {
            int rangoA = rango.get(raizA);
            int rangoB = rango.get(raizB);

            if (rangoA < rangoB) {
                padre.put(raizA, raizB);
            } else if (rangoA > rangoB) {
                padre.put(raizB, raizA);
            } else {
                padre.put(raizA, raizB);
                rango.put(raizB, rangoB + 1);
            }
        }
    }
}
