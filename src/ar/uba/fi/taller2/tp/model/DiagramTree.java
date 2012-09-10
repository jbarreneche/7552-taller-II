package ar.uba.fi.taller2.tp.model;

import java.util.List;

public class DiagramTree {
	
	
	private List<Entity> entities;
	private List<Relation> relations;
	private String nombre;
	private EstadoValidacion estado;
	
	public DiagramTree() {
		estado = EstadoValidacion.SIN_VALIDAR;
	}
	
	public void validar (boolean conObs ) {
		if( !conObs) {
			estado = EstadoValidacion.CON_OBSERVACIONES;
		} else {
			estado = EstadoValidacion.VALIDADO;
		}
	}
	
	public void actualizar() {
		// Deja de estar validado.
		estado = EstadoValidacion.SIN_VALIDAR;
	}
	
	public String getEstadoValidacion() {
		return estado.toString();
	}
	
    public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public String toString() {
		return nombre;
	}
	
	private enum EstadoValidacion {
		SIN_VALIDAR, CON_OBSERVACIONES, VALIDADO
	}
}
