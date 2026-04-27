package tp2conectando;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import javax.swing.JFrame;

public class MapaApp {
    public static void main(String[] args) {
        
        JFrame ventana = new JFrame("Mapa");
        
        JMapViewer mapa = new JMapViewer();
        
        mapa.setDisplayPosition(new Coordinate(-34.52, -58.70), 10);
        
        
        ventana.add(mapa);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}