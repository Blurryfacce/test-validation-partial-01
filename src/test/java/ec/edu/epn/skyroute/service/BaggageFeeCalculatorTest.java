package ec.edu.epn.skyroute.service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas UnitariasCalculadora de Tarifas de Equipaje")
class BaggageFeeCalculatorTest {
    @Mock(lenient = true)
    private PassengerService passengerService;

    @injectMocks
    private BaggageFeeCalculator calculator;

    @Test
    @DisplayName("Debe calcular tarifa estándar de $30.00 para una maleta de 20 kg para un pasajero regular")
    void Calcular 


}