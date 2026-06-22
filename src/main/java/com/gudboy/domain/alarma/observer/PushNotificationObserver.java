package com.gudboy.domain.alarma.observer;

import com.gudboy.domain.alarma.model.Alarma;

public class PushNotificationObserver implements IAlarmaObserver {

    private String type;

    public PushNotificationObserver(String type) {
        this.type = type;
    }

    @Override
    public void actualizarEstado(Alarma alarma) {
        // En un entorno de escritorio, esto podría enlazar con la vista Swing (JOptionPane o tray icon)
        System.out.println("[" + type + "] Notificación Push: La alarma '" + alarma.getTitulo() + "' ha cambiado su estado a " + alarma.getEstado());
    }

    public String getType() {
        return type;
    }
}