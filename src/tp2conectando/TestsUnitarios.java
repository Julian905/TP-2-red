package tp2conectando;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5 usa jupiter.api
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import tp2conectando.*;
import tp2conectando.*;

import java.util.Arrays;
import java.util.List;

public class TestsUnitarios {

    private CalculadorDeCostos calc;
    private final double costoKm = 100.0;
    private final double porcentajeExtra = 20.0;
    private final double costoProvincia = 5000.0;

    @BeforeEach
    public void setUp() {
        // Inicializamos el calculador antes de cada test para asegurar independencia
        calc = new CalculadorDeCostos(costoKm, porcentajeExtra, costoProvincia);
    }

    // --- TESTS DE LOCALIDAD (Modelo) ---

    @Test
    @DisplayName("Test de distancia: misma localidad")
    public void testDistanciaMismoPuntoEsCero() {
        Localidad l1 = new Localidad("Luján", "BsAs", -34.5, -59.1);
        assertEquals(0.0, l1.distancia(l1), 0.01);
    }

    @Test
    @DisplayName("Test de igualdad: objetos con mismos datos")
    public void testIgualdadLocalidades() {
        Localidad l1 = new Localidad("Pilar", "BsAs", -34.4, -58.9);
        Localidad l2 = new Localidad("Pilar", "BsAs", -34.4, -58.9);
        assertEquals(l1, l2, "Las localidades deberían ser iguales por nombre y provincia");
    }

    // --- TESTS DE CALCULADOR DE COSTOS (Reglas de Negocio del TP) ---

    @Test
    @DisplayName("Costo base: sin recargos (misma provincia, < 300km)")
    public void testCostoSimpleMismaProvincia() {
        Localidad a = new Localidad("A", "BsAs", -34.0, -58.0);
        Localidad b = new Localidad("B", "BsAs", -34.05, -58.05);
        
        double dist = a.distancia(b);
        double esperado = dist * costoKm;
        assertEquals(esperado, calc.calcular(a, b), 0.01, "El costo base debe ser dist * costoKm");
    }

    @Test
    @DisplayName("Recargo por provincia: distintas provincias")
    public void testCostoConRecargoPorProvinciaDistinta() {
        Localidad a = new Localidad("A", "BsAs", -34.0, -58.0);
        Localidad b = new Localidad("B", "Santa Fe", -34.05, -58.05); 
        
        double dist = a.distancia(b);
        double esperado = (dist * costoKm) + costoProvincia;
        assertEquals(esperado, calc.calcular(a, b), 0.01, "Debe sumar el costo fijo por cambio de provincia");
    }

    @Test
    @DisplayName("Recargo por distancia: más de 300km")
    public void testCostoConRecargoPorDistanciaMayorA300km() {
        // Coordenadas aproximadas para > 300km
        Localidad a = new Localidad("A", "Prov", -34.0, -58.0);
        Localidad b = new Localidad("B", "Prov", -38.0, -58.0); 
        
        double dist = a.distancia(b);
        assertTrue(dist > 300);
        
        double costoBase = dist * costoKm;
        double esperado = costoBase + (costoBase * (porcentajeExtra / 100));
        
        assertEquals(esperado, calc.calcular(a, b), 0.01, "Debe aplicar el porcentaje de aumento");
    }

    // --- TESTS DE ALGORITMO (Estructuras y Planificador) ---

    @Test
    @DisplayName("UnionFind: detección de conectividad")
    public void testUnionFindConectividad() {
        Localidad l1 = new Localidad("1", "P", 0, 0);
        Localidad l2 = new Localidad("2", "P", 0, 0);
        UnionFind uf = new UnionFind(Arrays.asList(l1, l2));

        assertNotSame(uf.find(l1), uf.find(l2), "Al inicio deben estar en conjuntos separados");
        uf.union(l1, l2);
        assertEquals(uf.find(l1), uf.find(l2), "Tras la unión deben compartir el mismo representante");
    }

    @Test
    @DisplayName("Planificador: Árbol Generador Mínimo (MST)")
    public void testPlanificadorGeneraArbolMinimo() {
        Localidad a = new Localidad("A", "P", -34.0, -58.0);
        Localidad b = new Localidad("B", "P", -34.1, -58.0);
        Localidad c = new Localidad("C", "P", -34.2, -58.0);
        
        Planificador p = new Planificador(calc);
        List<Conexion> resultado = p.calcularAGM(Arrays.asList(a, b, c));

        // Un MST de N nodos siempre tiene N-1 conexiones
        assertEquals(2, resultado.size(), "El MST de 3 localidades debe tener 2 conexiones");
        
        double costoTotal = resultado.stream().mapToDouble(Conexion::getCosto).sum();
        double costoDirectoAC = calc.calcular(a, c);

        // El algoritmo debe elegir conectar A-B y B-C porque la suma es menor que A-C + (otra)
        assertTrue(costoTotal < (calc.calcular(a, c) + calc.calcular(b, c)), "El costo total debe ser el mínimo posible");
    }
}
