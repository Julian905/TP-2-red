package tp2conectando;

import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import tp2conectando.Conexion;
import tp2conectando.Localidad;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList; // Importante para la lista de coordenadas
import java.util.List;

public class MapaApp extends JFrame {
    private JMapViewer mapa;

    public MapaApp() {
        setTitle("Visualización de Red - UNGS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mapa = new JMapViewer();
        add(mapa);
    }

    public void mostrar(List<Localidad> locs, List<Conexion> con) {
        // CORRECCIÓN 1: Limpiar marcadores y polígonos (líneas)
        mapa.removeAllMapMarkers();
        mapa.getMapPolygonList().clear(); // JMapViewer usa polígonos para las líneas
        mapa.repaint(); // Refrescar el mapa

        // Dibujar Marcadores
        for (Localidad l : locs) {
            Coordinate coord = new Coordinate(l.getLatitud(), l.getLongitud());
            MapMarker marker = new MapMarkerDot(l.getNombre(), coord);
            mapa.addMapMarker(marker);
        }

        // CORRECCIÓN 2: Dibujar Conexiones usando MapPolygonImpl
        // (En JMapViewer, una línea es un polígono de 2 o 3 puntos)
        for (Conexion c : con) {
            List<Coordinate> puntos = new ArrayList<>();
            puntos.add(new Coordinate(c.getOrigen().getLatitud(), c.getOrigen().getLongitud()));
            puntos.add(new Coordinate(c.getDestino().getLatitud(), c.getDestino().getLongitud()));
            puntos.add(new Coordinate(c.getDestino().getLatitud(), c.getDestino().getLongitud())); // Se repite el último punto para cerrar la línea

            MapPolygonImpl linea = new MapPolygonImpl(puntos);
            linea.setColor(Color.BLUE);
            mapa.addMapPolygon(linea);
        }

        if (!locs.isEmpty()) {
            mapa.setDisplayPosition(new Coordinate(locs.get(0).getLatitud(), locs.get(0).getLongitud()), 5);
        }
    }
}
