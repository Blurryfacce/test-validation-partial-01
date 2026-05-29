package ec.edu.epn.skyroute.service;

import org.springframework.stereotype.Service;

/**
 * Calcula la tarifa total de equipaje para SkyRoute Airlines.
 *
 * <p>Reglas aplicadas:
 * <ul>
 *   <li>Cada maleta tiene una tarifa base de $30.00.</li>
 *   <li>Si la maleta supera los 23 kg, se suma un recargo fijo de $50.00.</li>
 *   <li>Un pasajero VIP obtiene la primera maleta sin costo cuando el peso no supera los 23 kg.</li>
 *   <li>Los parámetros inválidos provocan una {@link IllegalArgumentException}.</li>
 * </ul>
 */
@Service
public class BaggageFeeCalculator {

    private static final double PESO_MAXIMO_SIN_RECARGO = 23.0;
    private static final double TARIFA_BASE_POR_MALETA = 30.0;
    private static final double RECARGO_POR_EXCESO_DE_PESO = 50.0;

    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param pesoPorMaleta peso de cada maleta, en kilogramos
     * @param cantidadMaletas cantidad de maletas registradas
     * @param passengerId identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si alguno de los parámetros no es válido
     */
    public double calcularTarifaEquipaje(double pesoPorMaleta, int cantidadMaletas, Long passengerId) {
        validarParametros(pesoPorMaleta, cantidadMaletas, passengerId);

        boolean pasajeroVip = passengerService.isVip(passengerId);
        double total = 0.0;

        for (int numeroMaleta = 1; numeroMaleta <= cantidadMaletas; numeroMaleta++) {
            total += calcularCostoDeMaleta(pesoPorMaleta, pasajeroVip, numeroMaleta);
        }

        return total;
    }

    /**
     * Conserva compatibilidad con el nombre anterior del método.
     *
     * @deprecated use {@link #calcularTarifaEquipaje(double, int, Long)} instead.
     */
    @Deprecated
    public double calculateFee(double weight, int bagCount, Long passengerId) {
        return calcularTarifaEquipaje(weight, bagCount, passengerId);
    }

    private void validarParametros(double pesoPorMaleta, int cantidadMaletas, Long passengerId) {
        if (pesoPorMaleta <= 0 || cantidadMaletas < 1 || passengerId == null) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos");
        }
    }

    private double calcularCostoDeMaleta(double pesoPorMaleta, boolean pasajeroVip, int numeroMaleta) {
        if (esPrimeraMaletaExenta(pasajeroVip, pesoPorMaleta, numeroMaleta)) {
            return 0.0;
        }

        double costo = TARIFA_BASE_POR_MALETA;

        if (pesoPorMaleta > PESO_MAXIMO_SIN_RECARGO) {
            costo += RECARGO_POR_EXCESO_DE_PESO;
        }

        return costo;
    }

    private boolean esPrimeraMaletaExenta(boolean pasajeroVip, double pesoPorMaleta, int numeroMaleta) {
        return pasajeroVip && numeroMaleta == 1 && pesoPorMaleta <= PESO_MAXIMO_SIN_RECARGO;
    }
}
