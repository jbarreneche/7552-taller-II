/**
 * 
 */
package ar.uba.fi.taller2.tp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oscar
 *
 */
public class Attribute extends Component{

	
	private String cardinality=Component.CARDINALIDAD_1_1;
	private List<Attribute> component=  new ArrayList<Attribute>();
	private TypeAttribute type;
	public Attribute(Long id, String name,TypeAttribute typeAttribute) {
		super(id, name);
		this.type=typeAttribute;
	}
	public void addComponent(Attribute attribute){
		this.component.add(attribute);
	}
	public List<Attribute> getComponent() {
		return component;
	}

	public void setComponent(List<Attribute> component) {
		this.component = component;
	}

	public String getCardinality() {
		return cardinality;
	}
	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public TypeAttribute getType() {
		return type;
	}
	public void setType(TypeAttribute type) {
		this.type = type;
	}

}
