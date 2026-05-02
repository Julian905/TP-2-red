package tp2conectando;

import java.util.HashMap;

public class UnionFind {

    private HashMap<Localidad, Localidad> padre = new HashMap<>();

    public UnionFind(Iterable<Localidad> localidades) {
        for (Localidad l : localidades) {
            padre.put(l, l);
        }
    }

    public Localidad find(Localidad l) {
        if (padre.get(l) != l) {
            padre.put(l, find(padre.get(l)));
        }
        return padre.get(l);
    }

    public void union(Localidad a, Localidad b) {
        Localidad raizA = find(a);
        Localidad raizB = find(b);

        if (raizA != raizB) {
            padre.put(raizA, raizB);
        }
    }
}