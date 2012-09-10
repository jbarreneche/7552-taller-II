package ar.uba.fi.taller2.tp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Entity extends Component {


	public Entity(Long id, String name) {
		super(id,name);
		specializations= new ArrayList<Specialization>(); 
		identifiers= new ArrayList<Identifiers>();
	}
    private TypeEntity typeEntity; 
	private List<Identifiers> identifiers= new ArrayList<Identifiers>();
    private List<Specialization> specializations= new ArrayList<Specialization>(); 
	

    public Specialization getGeneralization(Long numAttribute) {
    	
    	for (Iterator<Specialization> iterator = specializations.iterator(); iterator.hasNext();) {
    		Specialization attribute = (Specialization) iterator.next();
			if(attribute.getId().equals(numAttribute))
				return attribute;
		}
		return null;
}
	public List<Specialization> getGeneralizations() {
		return specializations;
	}
	
	public void eliminarIdentificadoresCompuestos() {
		List<Identifiers> listaNegra = new ArrayList<Identifiers>();
		for (int i = 0; i < this.identifiers.size(); i++) {
			Identifiers unIds = this.identifiers.get(i);
			if( unIds.getIdsIdentifiers().size() > 1 ) {
				listaNegra.add( unIds );
			}
		}
		this.identifiers.removeAll( listaNegra );
	}

	public void setGeneralizations(List<Specialization> generalizations) {
		this.specializations = generalizations;
	}

	public List<Identifiers> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<Identifiers> identifiers) {
		this.identifiers = identifiers;
	}

	

	public TypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	

}
