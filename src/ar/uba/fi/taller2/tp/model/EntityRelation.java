/**
 * 
 */
package ar.uba.fi.taller2.tp.model;

/**
 * @author oscar
 *
 */
public class EntityRelation {
	private Long idEntity;
	private String cardinality;
	private Long id;
	private String role;
	
	
	public EntityRelation(Long id, String cardinality, Long idEntity ,String role) {
		super();
		this.idEntity = idEntity;
		this.cardinality = cardinality;
		this.id = id;
		this.role=role;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdEntity() {
		return idEntity;
	}
	public String getCardinality() {
		return cardinality;
	}
	public void setIdEntity(Long idEntity) {
		this.idEntity = idEntity;
	}
	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

}
