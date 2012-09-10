//package ar.uba.fi.taller2.tp.xml;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//
//import ar.uba.fi.taller2.tp.control.context.ProjectContext;
//import ar.uba.fi.taller2.tp.model.Attribute;
//import ar.uba.fi.taller2.tp.model.Component;
//import ar.uba.fi.taller2.tp.model.Entity;
//import ar.uba.fi.taller2.tp.model.EntityRelation;
//import ar.uba.fi.taller2.tp.model.Identifiers;
//import ar.uba.fi.taller2.tp.model.Relation;
//import ar.uba.fi.taller2.tp.model.TypeAttribute;
//import ar.uba.fi.taller2.tp.model.TypeEntity;
//
//public class TestXmlGenerator {
//	private Integer i=1;
//	private Long getIdInc(){
//		return Long.valueOf(i++);
//	}
//	private Entity getObra(){
//		Entity entity1=new Entity(getIdInc(), "Obra");
//		entity1.setTypeEntity(TypeEntity.MAESTRA_DOMINIO);
//		Attribute attribute1=new Attribute(getIdInc(),"idObra",TypeAttribute.SIMPLE);
//		Attribute attribute2=new Attribute(getIdInc(),"titulo",TypeAttribute.SIMPLE);
//		Attribute attribute3=new Attribute(getIdInc(),"director",TypeAttribute.SIMPLE);
//		Attribute attribute4=new Attribute(getIdInc(),"autor",TypeAttribute.SIMPLE);
//		attribute4.setCardinality(Attribute.CARDINALIDAD_1_N);
//		Attribute attribute5=new Attribute(getIdInc(),"genero",TypeAttribute.SIMPLE);
//		entity1.getAttributes().add(attribute1);
//		entity1.getAttributes().add(attribute2);
//		entity1.getAttributes().add(attribute3);
//		entity1.getAttributes().add(attribute4);
//		entity1.getAttributes().add(attribute5);
//
//		Attribute attributeComposed= new Attribute(getIdInc() ,"encartel",TypeAttribute.COMPOSED);
//		attributeComposed.addComponent(new Attribute(getIdInc(),"desde",TypeAttribute.SIMPLE));
//		Attribute attribute6=new Attribute(getIdInc(),"hasta",TypeAttribute.SIMPLE);
//		attribute4.setCardinality(Attribute.CARDINALIDAD_0_1);
//		attributeComposed.addComponent(attribute6);
//		entity1.getAttributes().add(attributeComposed);
//		
//		Identifiers id= new Identifiers();
//		id.addIds(attribute2.getId());
//		id.addIds(attribute3.getId());
//		entity1.getIdentifiers().add(id);
//		Identifiers id2= new Identifiers();
//		id2.addIds(attribute1.getId());
//		entity1.getIdentifiers().add(id2);
//		return entity1;
//	}
//	private Entity getTeatro(){
//		Entity entity1=new Entity(getIdInc(), "Teatro");
//		entity1.setTypeEntity(TypeEntity.MAESTRA_DOMINIO);
//		Attribute attribute3=new Attribute(getIdInc(),"idTeatro",TypeAttribute.SIMPLE);
//		Attribute attribute1=new Attribute(getIdInc(),"nomTeatro",TypeAttribute.SIMPLE);
//		Attribute attribute2=new Attribute(getIdInc(),"localidad",TypeAttribute.SIMPLE);
//		entity1.getAttributes().add(attribute3);
//		entity1.getAttributes().add(attribute1);
//		entity1.getAttributes().add(attribute2);
//
//		Attribute attributeComposed= new Attribute(getIdInc() ,"dir",TypeAttribute.COMPOSED);
//		attributeComposed.addComponent(new Attribute(getIdInc(),"calle",TypeAttribute.SIMPLE));
//		attributeComposed.addComponent(new Attribute(getIdInc(),"ubicacion",TypeAttribute.SIMPLE));
//		entity1.getAttributes().add(attributeComposed);
//		
//		Identifiers id= new Identifiers();
//		id.addIds(attribute1.getId());
//		id.addIds(attribute2.getId());
//		entity1.getIdentifiers().add(id);
//		Identifiers id2= new Identifiers();
//		id2.addIds(attribute3.getId());
//		entity1.getIdentifiers().add(id2);
//		return entity1;
//	}
//	private Entity getInterprete(){
//		Entity entity1=new Entity(getIdInc(), "Interprete");
//		entity1.setTypeEntity(TypeEntity.MAESTRA_DOMINIO);
//		Attribute attribute1=new Attribute(getIdInc(),"idInterprete",TypeAttribute.SIMPLE);
//		Attribute attribute2=new Attribute(getIdInc(),"nombre",TypeAttribute.SIMPLE);
//		entity1.getAttributes().add(attribute1);
//		entity1.getAttributes().add(attribute2);
//		
//		Identifiers id= new Identifiers();
//		id.addIds(attribute1.getId());
//		entity1.getIdentifiers().add(id);
//		Identifiers id2= new Identifiers();
//		id2.addIds(attribute2.getId());
//		entity1.getIdentifiers().add(id2);
//		return entity1;
//	}
//	private Entity getFuncion(Entity teatro, Entity obra){
//		Entity entity1=new Entity(getIdInc(), "Funcion");
//		entity1.setTypeEntity(TypeEntity.MAESTRA_DOMINIO);
//		Attribute attribute1=new Attribute(getIdInc(),"dia",TypeAttribute.SIMPLE);
//		Attribute attribute2=new Attribute(getIdInc(),"hora",TypeAttribute.SIMPLE);
//		entity1.getAttributes().add(attribute1);
//		entity1.getAttributes().add(attribute2);
//		
//		Identifiers id= new Identifiers();
//		id.addIds(attribute1.getId());
//		id.addIds(attribute2.getId());
//		id.addIds(teatro.getId());
//		entity1.getIdentifiers().add(id);
//		Identifiers id2= new Identifiers();
//		id2.addIds(teatro.getId());
//		id2.addIds(obra.getId());
//		entity1.getIdentifiers().add(id2);
//		return entity1;
//	}
//	private Relation getElenco(Entity inter, Entity obra){
//		Relation relation= new Relation(getIdInc(), "Elenco");
//		Attribute attribute1=new Attribute(getIdInc(),"protag",TypeAttribute.SIMPLE);
//		Attribute attribute2=new Attribute(getIdInc(),"rol",TypeAttribute.SIMPLE);
//		attribute2.setCardinality(Component.CARDINALIDAD_0_1);
//		relation.getAttributes().add(attribute1);
//		relation.getAttributes().add(attribute2);
//		relation.getEntitys().add(new EntityRelation(inter.getId(), Component.CARDINALIDAD_0_N));
//		relation.getEntitys().add(new EntityRelation(obra.getId(), Component.CARDINALIDAD_0_N));
//		return relation;
//	}
//	private Relation getFO(Entity funcion, Entity obra){
//		Relation relation= new Relation(getIdInc(), "FO");
//		relation.getEntitys().add(new EntityRelation(funcion.getId(), Component.CARDINALIDAD_1_1));
//		relation.getEntitys().add(new EntityRelation(obra.getId(), Component.CARDINALIDAD_0_N));
//		return relation;
//	}
//	private Relation getFT(Entity teatro, Entity obra){
//		Relation relation= new Relation(getIdInc(), "FT");
//		relation.getEntitys().add(new EntityRelation(teatro.getId(), Component.CARDINALIDAD_1_1));
//		relation.getEntitys().add(new EntityRelation(obra.getId(), Component.CARDINALIDAD_0_N));
//		return relation;
//	}
////	@Test
//	public void testSaveDiagramXml() throws UnsupportedEncodingException, FileNotFoundException{
//		XmlGenerator generator= new  XmlGenerator();
//		ProjectContext diagram = new ProjectContext();
//		diagram.setIdIncrement(Long.valueOf(0));
//		List<Entity> entities= new ArrayList<Entity>();
//		Entity teatro=getTeatro();
//		Entity obra=getObra();
//		Entity inter=getInterprete();
//		Entity funcion=getFuncion(teatro,obra);
//		
//		entities.add(teatro);
//		entities.add(inter);
//		entities.add(obra);
//		entities.add(funcion);
//		diagram.setEntities(entities);
//		List<Relation> relations= new ArrayList<Relation>();
//		relations.add(getElenco(inter,obra));
//		relations.add(getFO(funcion, obra));
//		relations.add(getFT(funcion, obra));
//		diagram.setRelations(relations);
//		generator.saveDiagramXml(diagram , "diagrama");
//
//	}
////	@Test
//	public void testloadDiagramXML() throws IOException{
//		XmlGenerator generator= new XmlGenerator();
//		ProjectContext context=generator.loadDiagramXML("diagrama");
//		Entity entity= context.getEntity(Long.valueOf(-1));
//		Assert.assertEquals(entity.getName(),"Entity");
//		entity= context.getEntity(Long.valueOf(1));
//		Assert.assertEquals(entity.getName(),"Teatro");
//		entity= context.getEntity(Long.valueOf(17));
//		Assert.assertEquals(entity.getName(),"Interprete");
//		entity= context.getEntity(Long.valueOf(-1));
//		Assert.assertEquals(entity.getId(),Long.valueOf(2));
//		entity= context.getEntity(Long.valueOf(-1));
//		Assert.assertEquals(entity.getId(),Long.valueOf(3));
//	}
//    @Test
//   public void testParserPath(){
//	String path="/home/oscar/Documentos/TPTaller/TPTaller.xml";
//	String[] subDirs = path.split(Pattern.quote(File.separator));
//	List<String> listPath=new ArrayList<String>();
//	String nameFile=subDirs[subDirs.length-1];
//	listPath.add(nameFile);
//	nameFile=File.separator+nameFile;
//	listPath.add(path.replaceAll(nameFile,""));
//	System.out.println(File.separator);
//	for (int i = 0; i < subDirs.length; i++) {
//		System.out.println(subDirs[i]);
//	}
//	
//   }
//}
