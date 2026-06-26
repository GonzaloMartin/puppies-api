package com.gudboy;

import com.gudboy.domain.tratamiento.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TratamientoTest {

    @Test
    void tratamientoNuevo_tieneEstadoPendiente() {
        Tratamiento t = new Tratamiento(TipoTratamiento.COLOCAR_VACUNA);
        assertInstanceOf(Pendiente.class, t.getEstado());
    }

    @Test
    void aplicarTratamiento_cambiaAEnCurso() {
        Tratamiento t = new Tratamiento(TipoTratamiento.CONTROL_DE_PARASITOS);
        t.aplicarTratamiento();
        assertInstanceOf(EnCurso.class, t.getEstado());
    }

    @Test
    void finalizarTratamiento_desde_pendiente_debeFuncionar() {
        Tratamiento t = new Tratamiento(TipoTratamiento.CHEQUEAR_NUTRICION);
        t.finalizarTratamiento();
        assertInstanceOf(Finalizado.class, t.getEstado());
    }

    @Test
    void cancelarTratamiento_cambiaACancelado() {
        Tratamiento t = new Tratamiento(TipoTratamiento.COLOCAR_ANTIPARASITARIOS);
        t.cancelarTratamiento();
        assertInstanceOf(Cancelado.class, t.getEstado());
    }

    @Test
    void aplicarYFinalizar_esFlujoCompleto() {
        Tratamiento t = new Tratamiento(TipoTratamiento.COMPROBAR_PESO_TAMANIO);
        t.aplicarTratamiento();
        assertInstanceOf(EnCurso.class, t.getEstado());
        t.finalizarTratamiento();
        assertInstanceOf(Finalizado.class, t.getEstado());
    }
}
