package com.gudboy.domain.animal.model;

/**
 * Clase base que representa a cualquier animal del refugio.
 * Tanto animales domésticos como salvajes extienden esta clase.
 *
 * Centraliza los datos comunes y el estado de salud (patrón State), que
 * por tener un campo mutable propio de cada instancia no puede vivir en
 * una interfaz.
 */
public abstract class Animal {

    private final String nombre;
    private final String especie;
    private final double altura;
    private final double peso;
    private final int edad;
    private final String condicionMedica;
    private IEstadoDeSalud estadoDeSalud;

    protected Animal(String nombre, String especie, double altura,
                      double peso, int edad, String condicionMedica) {
        this.nombre = nombre;
        this.especie = especie;
        this.altura = altura;
        this.peso = peso;
        this.edad = edad;
        this.condicionMedica = condicionMedica;
        this.estadoDeSalud = new EstadoDisponible(this);
    }

    public void disponibilizar() {
        estadoDeSalud.Disponibilizar();
    }

    public void ponerEnTratamiento() {
        estadoDeSalud.PonerEnTratamiento();
    }

    public void adoptar() {
        estadoDeSalud.Adoptar();
    }

    void setEstadoDeSalud(IEstadoDeSalud estadoDeSalud) {
        this.estadoDeSalud = estadoDeSalud;
    }

    public IEstadoDeSalud getEstadoDeSalud() {
        return estadoDeSalud;
    }

    /**
     * Indica si el animal puede ser adoptado.
     * - Los animales salvajes NUNCA pueden ser adoptados.
     * - Los domésticos pueden adoptarse si no están en tratamiento médico.
     */
    public abstract boolean esAdoptable();

    /**
     * Retorna una descripción del tipo de animal (doméstico / salvaje).
     */
    public abstract String getTipoAnimal();

    public String getNombre()          { return nombre; }
    public String getEspecie()         { return especie; }
    public double getAltura()          { return altura; }
    public double getPeso()            { return peso; }
    public int    getEdad()            { return edad; }
    public String getCondicionMedica() { return condicionMedica; }
}
