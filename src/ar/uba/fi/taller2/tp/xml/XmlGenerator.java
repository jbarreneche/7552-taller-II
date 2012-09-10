package ar.uba.fi.taller2.tp.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ar.uba.fi.taller2.tp.control.context.ProjectContext;
import ar.uba.fi.taller2.tp.control.context.ProjectEntity;
import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.EntityRelation;
import ar.uba.fi.taller2.tp.model.Identifiers;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.Specialization;

import com.thoughtworks.xstream.XStream;

public class XmlGenerator {



	public void saveDiagramXml(ProjectContext diagram,String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
		getXStream().toXML(diagram,out);
	
	}
	public void saveDiagramXml(ProjectEntity project,String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
		getXStream().toXML(project,out);
	
	}
    private  XStream getXStream(){
    	XStream xstream = new XStream();
		xstream.alias("Entidad", Entity.class);
		xstream.alias("Specialization", Specialization.class);
		xstream.alias("ProjectEntity", ProjectEntity.class);
		xstream.alias("ProjectContext", ProjectContext.class);
		xstream.alias("Relacion", Relation.class);
		xstream.alias("RelacionEntidad", EntityRelation.class);
		xstream.alias("Atributo", Attribute.class);
		xstream.alias("Identificador",Identifiers.class);
		xstream.aliasField("Identificadores", Entity.class, "identifiers");
		xstream.aliasField("Componentes", Attribute.class, "component");
		xstream.aliasField("IDsComponentes", Identifiers.class, "idsIdentifiers");
		xstream.aliasField("IDsSpecializations", Entity.class, "specializations");
//		xstream.addImplicitCollection(Entity.class, "attributes");
		return xstream;
    }
	public ProjectContext loadDiagramXML(String fileName) throws IOException {
		return (ProjectContext)getXStream().fromXML(new FileInputStream(new File(fileName))); 
	}
	
	public ProjectEntity loadProjectXML(String fileName) throws IOException {
		return (ProjectEntity)getXStream().fromXML(new FileInputStream(new File(fileName))); 
	}

}
