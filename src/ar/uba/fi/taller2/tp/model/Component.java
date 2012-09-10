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
public class Component {
	
	public static final String CARDINALIDAD_1_N="(0,N)";
	public static final String CARDINALIDAD_0_1="(0,1)";
	public static final String CARDINALIDAD_0_N = "(0,N)";
	public static final String CARDINALIDAD_1_1 = "(1,1)";
	private Long id;
	private String name;
	private List<Attribute> attributes= new ArrayList<Attribute>();

	public List<Attribute> getAttributes() {
		return attributes;
	}
    
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public Component(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// El arbol necesita que este método exista.
	public String toString() {
		return getName();
	}
}
