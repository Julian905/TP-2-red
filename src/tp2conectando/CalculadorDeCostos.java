package tp2conectando;

public class CalculadorDeCostos {

    private double costoPorKm;
    private double porcentajeExtra;
    private double costoFijoEntreProvincias;

    public CalculadorDeCostos(double costoPorKm, double porcentajeExtra, double costoFijoEntreProvincias) {
        this.costoPorKm = costoPorKm;
        this.porcentajeExtra = porcentajeExtra;
        this.costoFijoEntreProvincias = costoFijoEntreProvincias;
    }

    public double calcularCosto(Localidad a, Localidad b) {
        double distancia = a.calcularDistancia(b);

        double costo = distancia * costoPorKm;

        if (distancia > 300) {
            costo += costo * (porcentajeExtra / 100);
        }

        if (!a.getProvincia().equals(b.getProvincia())) {
            costo += costoFijoEntreProvincias;
        }

        return costo;
    }
}