package tp2conectando;

public class Localidad {
    private String nombre;
    private String provincia;
    private double latitud;
    private double longitud;

    public Localidad(String nombre, String provincia, double latitud, double longitud) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public double calcularDistancia(Localidad otra) {
        double radioTierra = 6371;

        double lat1 = Math.toRadians(this.latitud);
        double lon1 = Math.toRadians(this.longitud);
        double lat2 = Math.toRadians(otra.latitud);
        double lon2 = Math.toRadians(otra.longitud);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat/2) * Math.sin(dlat/2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dlon/2) * Math.sin(dlon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radioTierra * c;
    }
}