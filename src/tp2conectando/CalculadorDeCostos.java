package tp2conectando;

import tp2conectando.Localidad;

public class CalculadorDeCostos {
    private double costoKm;
    private double porcentajeExtra;
    private double costoProvincia;

    public CalculadorDeCostos(double costoKm, double porcentajeExtra, double costoProvincia) {
        this.costoKm = costoKm;
        this.porcentajeExtra = porcentajeExtra;
        this.costoProvincia = costoProvincia;
    }

    public double calcular(Localidad a, Localidad b) {
        double distancia = a.distancia(b);
        double costo = distancia * costoKm;

        if (distancia > 300) {
            costo += costo * (porcentajeExtra / 100);
        }
        
        // Se ignoran mayúsculas/minúsculas para evitar errores de tipeo
        if (!a.getProvincia().equalsIgnoreCase(b.getProvincia())) {
            costo += costoProvincia;
        }

        return costo;
    }
}
