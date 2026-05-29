package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de la calculadora de tarifas de equipaje")
class BaggageFeeCalculatorTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private BaggageFeeCalculator calculator;

    @Test
    @DisplayName("Debe cobrar $30.00 por una maleta estándar de 20 kg para un pasajero regular")
    void debeCobrarTarifaEstandarParaPasajeroRegular() {
        Long passengerId = 1L;
        when(passengerService.isVip(passengerId)).thenReturn(false);

        double tarifa = calculator.calcularTarifaEquipaje(20.0, 1, passengerId);

        assertEquals(30.0, tarifa, "La tarifa base debe aplicarse cuando no hay recargos ni beneficios VIP");
    }

    @Test
    @DisplayName("Debe cobrar $80.00 por una maleta de 25 kg para un pasajero regular")
    void debeCobrarRecargoPorExcesoDePeso() {
        Long passengerId = 2L;
        when(passengerService.isVip(passengerId)).thenReturn(false);

        double tarifa = calculator.calcularTarifaEquipaje(25.0, 1, passengerId);

        assertEquals(80.0, tarifa, "La tarifa debe incluir base y recargo por exceso de peso");
    }

    @Test
    @DisplayName("Debe exonerar la primera maleta VIP de 15 kg")
    void debeExonerarPrimeraMaletaVip() {
        Long passengerId = 3L;
        when(passengerService.isVip(passengerId)).thenReturn(true);

        double tarifa = calculator.calcularTarifaEquipaje(15.0, 1, passengerId);

        assertEquals(0.0, tarifa, "La primera maleta VIP no debe cobrarse si no supera 23 kg");
    }

    @Test
    @DisplayName("Debe cobrar solo una maleta cuando un VIP lleva dos maletas de 15 kg")
    void debeCobrarSoloLaSegundaMaletaEnCasoVipConDosMaletas() {
        Long passengerId = 4L;
        when(passengerService.isVip(passengerId)).thenReturn(true);

        double tarifa = calculator.calcularTarifaEquipaje(15.0, 2, passengerId);

        assertEquals(30.0, tarifa, "La primera maleta debe ser gratis y la segunda debe cobrarse con la tarifa base");
    }

    @Test
    @DisplayName("Debe tratar 23 kg como peso exento para un VIP")
    void debeConsiderarVeintitresKgComoLimiteExentoParaVip() {
        Long passengerId = 5L;
        when(passengerService.isVip(passengerId)).thenReturn(true);

        double tarifa = calculator.calcularTarifaEquipaje(23.0, 1, passengerId);

        assertEquals(0.0, tarifa, "El límite de 23 kg debe mantenerse dentro del beneficio VIP");
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el peso es cero")
    void debeLanzarExcepcionCuandoElPesoEsCero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calcularTarifaEquipaje(0.0, 1, 6L)
        );

        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
        verifyNoInteractions(passengerService);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando la cantidad de maletas es inválida")
    void debeLanzarExcepcionCuandoLaCantidadDeMaletasEsInvalida() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calcularTarifaEquipaje(20.0, 0, 7L)
        );

        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
        verifyNoInteractions(passengerService);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el identificador del pasajero es nulo")
    void debeLanzarExcepcionCuandoElIdentificadorEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calcularTarifaEquipaje(20.0, 1, null)
        );

        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
        verifyNoInteractions(passengerService);
    }


}