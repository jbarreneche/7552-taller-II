package ar.uba.fi.taller2.tp.model;


public class Specialization {
	private Long idEntity;
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String cardinality;
	
	public Specialization(Long id,Long idEntity, String cardinality) {
		super();
		this.idEntity = idEntity;
		this.id = id;
		this.cardinality = cardinality;
	}
	public Long getIdEntity() {
		return idEntity;
	}
	public void setIdEntity(Long idEntity) {
		this.idEntity = idEntity;
	}

	public String getCardinality() {
		return cardinality;
	}
	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}
	

	
	
}
