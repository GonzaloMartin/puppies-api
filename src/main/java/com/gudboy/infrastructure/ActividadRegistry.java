package com.gudboy.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ActividadRegistry {
    private static final List<Consumer<String>> listeners = new ArrayList<>();
    private static boolean habilitarConsola = true;

    public static synchronized void setHabilitarConsola(boolean habilitar) {
        habilitarConsola = habilitar;
    }

    public static synchronized void registrarListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public static synchronized void publicar(String mensaje) {
        if (habilitarConsola) {
            System.out.println(mensaje);
        }
        for (Consumer<String> listener : listeners) {
            try {
                listener.accept(mensaje);
            } catch (Exception ignored) {
            }
        }
    }
}
