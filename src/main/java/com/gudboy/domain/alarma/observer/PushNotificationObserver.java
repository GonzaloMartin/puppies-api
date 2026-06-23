package com.gudboy.domain.alarma.observer;

import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.controller.UsuarioController;
import com.gudboy.domain.Usuario.Veterinario;

import java.util.List;

public class PushNotificationObserver implements IAlarmaObserver {

    private final UsuarioController usuarioController;

    public PushNotificationObserver(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    @Override
    public void actualizarEstado(Alarma alarma) {
        // Solo enviamos notificaciones push si la alarma necesita ser atendida.
        // No tiene sentido alertar si un veterinario acaba de marcarla como completada.
        if (!alarma.isCompletada() && !"FINALIZADO".equals(alarma.getEstado())) {

            List<Veterinario> veterinarios = usuarioController.listarVeterinarios();

            if (veterinarios.isEmpty()) {
                System.out.println("[PUSH NOTIFICATION] Error: No hay veterinarios registrados en el sistema para atender la alarma: " + alarma.getTitulo());
                return;
            }

            System.out.println("\n==================================================");
            System.out.println("🚨 [PUSH NOTIFICATION] DISPARANDO ALARMA: " + alarma.getTitulo());
            System.out.println("==================================================");

            for (Veterinario vet : veterinarios) {
                // En un entorno de producción, aquí se llamaría a la API de Firebase (FCM) o APNs.
                // Para el alcance del TP, documentamos la simulación del envío.
                System.out.println(" -> Enviando alerta al dispositivo móvil de: Dr/Dra. "
                        + vet.getNombre() + " " + vet.getApellido()
                        + " (Matrícula: " + vet.getMatricula() + ")");
            }
            System.out.println("==================================================\n");
        }
    }
}