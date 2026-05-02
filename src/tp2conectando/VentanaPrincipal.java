package tp2conectando;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class VentanaPrincipal extends JFrame {

    private ArrayList<Localidad> localidades = new ArrayList<>();

    private JTextField campoNombre;
    private JTextField campoProvincia;
    private JTextField campoLatitud;
    private JTextField campoLongitud;

    private JTextArea areaSalida;

    public VentanaPrincipal() {
        setTitle("Conexión de Localidades");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());


        campoNombre = new JTextField(10);
        campoProvincia = new JTextField(10);
        campoLatitud = new JTextField(8);
        campoLongitud = new JTextField(8);


        JButton botonAgregar = new JButton("Agregar Localidad");
        JButton botonCalcular = new JButton("Calcular");

        // Área de salida
        areaSalida = new JTextArea(12, 40);
        areaSalida.setEditable(false);


        add(new JLabel("Nombre:"));
        add(campoNombre);

        add(new JLabel("Provincia:"));
        add(campoProvincia);

        add(new JLabel("Latitud:"));
        add(campoLatitud);

        add(new JLabel("Longitud:"));
        add(campoLongitud);

        add(botonAgregar);
        add(botonCalcular);

        add(new JScrollPane(areaSalida));

        botonAgregar.addActionListener(e -> agregarLocalidad());
        botonCalcular.addActionListener(e -> calcularAGM());

        setVisible(true);
    }

    private void agregarLocalidad() {
        try {
            String nombre = campoNombre.getText();
            String provincia = campoProvincia.getText();
            double lat = Double.parseDouble(campoLatitud.getText());
            double lon = Double.parseDouble(campoLongitud.getText());

            Localidad nueva = new Localidad(nombre, provincia, lat, lon);
            localidades.add(nueva);

            areaSalida.append("Agregada: " + nombre + "\n");


            campoNombre.setText("");
            campoProvincia.setText("");
            campoLatitud.setText("");
            campoLongitud.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en los datos");
        }
    }

    private void calcularAGM() {

        if (localidades.size() < 2) {
            JOptionPane.showMessageDialog(this, "Necesitás al menos 2 localidades");
            return;
        }

        CalculadorDeCostos calc = new CalculadorDeCostos(10, 20, 5000);
        ArrayList<Conexion> conexiones = new ArrayList<>();

        // Generar conexiones
        for (int i = 0; i < localidades.size(); i++) {
            for (int j = i + 1; j < localidades.size(); j++) {

                Localidad a = localidades.get(i);
                Localidad b = localidades.get(j);

                double costo = calc.calcularCosto(a, b);
                conexiones.add(new Conexion(a, b, costo));
            }
        }

        // Ordenar por costo
        Collections.sort(conexiones);

        
        ArrayList<Conexion> arbol = new ArrayList<>();
        UnionFind uf = new UnionFind(localidades);

        for (Conexion c : conexiones) {
            Localidad a = c.getOrigen();
            Localidad b = c.getDestino();

            if (uf.find(a) != uf.find(b)) {
                arbol.add(c);
                uf.union(a, b);
            }
        }

        areaSalida.append("\n=== ARBOL GENERADOR MINIMO ===\n");

        double total = 0;

        for (Conexion c : arbol) {
            areaSalida.append(
                c.getOrigen().getNombre() + " - " +
                c.getDestino().getNombre() +
                " | $" + c.getCosto() + "\n"
            );
            total += c.getCosto();
        }

        areaSalida.append("Costo total: $" + total + "\n\n");
    }
}