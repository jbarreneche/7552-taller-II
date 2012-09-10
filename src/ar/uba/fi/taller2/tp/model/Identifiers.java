package ar.uba.fi.taller2.tp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Identifiers {

	private List<Long> idsIdentifiers;
	
    
	public Identifiers() {
		super();
       this.idsIdentifiers= new ArrayList<Long>();
	}

	
	public void addIds(Long id){
		this.idsIdentifiers.add(id);
	}

	public boolean isId( Long id){
		if (this.idsIdentifiers.size()==1 && this.idsIdentifiers.contains(id))
			return true;
		return false;
	}

	public List<Long> getIdsIdentifiers() {
		return idsIdentifiers;
	}


	public void setIdsIdentifiers(List<Long> idsIdentifiers) {
		this.idsIdentifiers = idsIdentifiers;
	}
	
	public boolean isEqual ( Identifiers ident ) {
		
		Iterator<Long> itLocal = this.idsIdentifiers.iterator();
		Iterator<Long> itExterno = ident.getIdsIdentifiers().iterator();
		int contadorA = 0;
		while( itLocal.hasNext() ) {
			itLocal.next();
			contadorA++;
		}
		int contadorB = 0;
		while( itExterno.hasNext() ) {
			itExterno.next();
			contadorB++;
		}
		if( contadorA != contadorB ) {
			return false;
		}
		
		itLocal = this.idsIdentifiers.iterator();
		itExterno = ident.getIdsIdentifiers().iterator();
		while( itLocal.hasNext() && itExterno.hasNext() ) {
			Long local = itLocal.next();
			Long externo = itExterno.next();
			Long dif = local - externo;
			if( dif != 0 ) {
				return false;
			}
		}
		
		return true;
	}
	
}
