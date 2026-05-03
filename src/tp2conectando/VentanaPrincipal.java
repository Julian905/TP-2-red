package tp2conectando;

import tp2conectando.Conexion;
import tp2conectando.Localidad;
import tp2conectando.CalculadorDeCostos;
import tp2conectando.Planificador;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private List<Localidad> localidades = new ArrayList<>();
    private JTextArea salida = new JTextArea(15, 50);

    private JTextField txtNombre = new JTextField(10), txtProv = new JTextField(10);
    private JTextField txtLat = new JTextField(6), txtLon = new JTextField(6);
    
    private JTextField txtCostoKm = new JTextField("10", 5);
    private JTextField txtPorcentaje = new JTextField("20", 5);
    private JTextField txtCostoProv = new JTextField("5000", 5);

    public VentanaPrincipal() {
        super("Planificador de Conexiones - TP 2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelInputs = new JPanel(new GridLayout(0, 4, 5, 5));
        panelInputs.setBorder(BorderFactory.createTitledBorder("Registro de Localidades y Parámetros"));
        
        panelInputs.add(new JLabel("Nombre:")); panelInputs.add(txtNombre);
        panelInputs.add(new JLabel("Costo por Km ($):")); panelInputs.add(txtCostoKm);
        
        panelInputs.add(new JLabel("Provincia:")); panelInputs.add(txtProv);
        panelInputs.add(new JLabel("% Aumento (>300km):")); panelInputs.add(txtPorcentaje);
        
        panelInputs.add(new JLabel("Latitud:")); panelInputs.add(txtLat);
        panelInputs.add(new JLabel("Costo Fijo Provincias ($):")); panelInputs.add(txtCostoProv);
        
        panelInputs.add(new JLabel("Longitud:")); panelInputs.add(txtLon);
        panelInputs.add(new JLabel("")); panelInputs.add(new JLabel("")); // Espaciadores

        JButton btnAdd = new JButton("Agregar Localidad");
        JButton btnCalc = new JButton("Planificar Red");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAdd); 
        panelBotones.add(btnCalc);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelInputs, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        salida.setEditable(false);
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(salida), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> agregarLocalidad());
        btnCalc.addActionListener(e -> calcular());

        pack();
        setLocationRelativeTo(null);
    }

    private void agregarLocalidad() {
        try {
            Localidad l = new Localidad(txtNombre.getText().trim(), txtProv.getText().trim(),
                    Double.parseDouble(txtLat.getText()), Double.parseDouble(txtLon.getText()));
            localidades.add(l);
            salida.append("Localidad registrada: " + l.getNombre() + " (" + l.getProvincia() + ")\n");
            
            txtNombre.setText(""); txtLat.setText(""); txtLon.setText("");
            txtNombre.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Latitud y longitud deben ser valores numéricos.");
        }
    }

    private void calcular() {
        if (localidades.size() < 2) {
            JOptionPane.showMessageDialog(this, "Debe registrar al menos 2 localidades.");
            return;
        }
        
        try {
            double cKm = Double.parseDouble(txtCostoKm.getText());
            double porc = Double.parseDouble(txtPorcentaje.getText());
            double cProv = Double.parseDouble(txtCostoProv.getText());

            CalculadorDeCostos calc = new CalculadorDeCostos(cKm, porc, cProv);
            Planificador planificador = new Planificador(calc);
            List<Conexion> resultado = planificador.calcularAGM(localidades);

            salida.setText("--- SOLUCIÓN DE RED ÓPTIMA ---\n");
            double total = 0;
            
            for (Conexion con : resultado) {
                salida.append(String.format("Conexión: %s <---> %s | Costo: $%.2f\n", 
                    con.getOrigen().getNombre(), con.getDestino().getNombre(), con.getCosto()));
                total += con.getCosto();
            }
            salida.append("\nCOSTO TOTAL DE INSTALACIONES: $" + String.format("%.2f", total));

            MapaApp mapa = new MapaApp();
            mapa.setVisible(true);
            mapa.mostrar(localidades, resultado);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los parámetros de costo deben ser numéricos.");
        }
    }
}
