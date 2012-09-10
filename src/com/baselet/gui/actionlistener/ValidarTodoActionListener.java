package com.baselet.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.DiagramTree;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.Specialization;

import com.baselet.control.Main;

public class ValidarTodoActionListener  implements ActionListener{
	private Main main;
	public ValidarTodoActionListener(Main main) {
		this.main = main;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO dicernir entre pedidos de validacion del proyecto completo y sólo de ciertos diagramas.
		
		Date date = new Date();
		String ruta = AppContext.getInstance().getProjectEntity().getParentDir() + "/";
		String prefijo = AppContext.getInstance().getProjectEntity().getName();
		
		DateFormat dateFormat2 = new SimpleDateFormat("yyMMdd");
		String sufijo = dateFormat2.format(date);
		
		String nombreArchivo = ruta + prefijo + sufijo + ".txt";
		
		validarTodoElProyecto( nombreArchivo );
		
	}
	
	private void validarTodoElProyecto( String nombreArchivo ) {
		
		boolean componentesCompletos = true;
		String str_completo = new String();
		float cantidadEntidades = 0;
		float cantidadTotalAtributos = 0;
		float cantidadRelaciones = 0;
		float promedioAtributosEntidades = 0;
		float promedioAtributosRelaciones = 0;
		float cantidadDiagramas = 0;
		float promedioElementosPorDiagrama = 0;
		
		File archivo;
		FileOutputStream arStream;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String fechaFormato = dateFormat.format(date);
		
		try {
			archivo = new File(nombreArchivo);
			arStream = new FileOutputStream(archivo);
			
			if (!archivo.exists()) {
				archivo.createNewFile();
			}
			
			String con = "Validación global.\n" + fechaFormato + "\n\n";
			byte[] cBy = con.getBytes();
			
			str_completo = str_completo.concat(con);
			arStream.write(cBy);
			arStream.flush();
			

			String presentacion = "Estado de las entidades del proyecto (id/nombre).\n";
			byte[] pBytes = presentacion.getBytes();
			str_completo = str_completo.concat(presentacion);
			arStream.write(pBytes);
			arStream.flush();
			
			List<Entity> entidades = AppContext.getInstance().getProjectContext().getEntities();
			Iterator<Entity> iter = entidades.iterator();
			while (iter.hasNext()) {
				Entity unaEntidad = iter.next();
				
				boolean componenteActualCompleto = true;
				String carencias = "";
				cantidadEntidades++;
				cantidadTotalAtributos += unaEntidad.getAttributes().size();

				if (unaEntidad.getName() == "") {
					componentesCompletos = false;
					componenteActualCompleto = false;
					carencias += "\tNombre inválido o inexistente.\n";
				}
				if (unaEntidad.getTypeEntity() == null) {
					componentesCompletos = false;
					componenteActualCompleto = false;
					carencias += "\tTipo inválido.\n";

				}
				if (unaEntidad.getIdentifiers().isEmpty()) {
					Iterator<Specialization> iter2 = unaEntidad.getGeneralizations().iterator();
					if ( iter2.hasNext() ) {
						Specialization unaEspecializacion = iter2.next();
						Long unId = unaEspecializacion.getIdEntity();
						Entity otraEnt = AppContext.getInstance().getEntity( unId );
								
						if ( otraEnt.getIdentifiers().isEmpty() ) {
							componentesCompletos = false;
							componenteActualCompleto = false;
							carencias += "\tSu padre carece de identificador.\n";
						}
					} else {
						componentesCompletos = false;
						componenteActualCompleto = false;
						carencias += "\tCarece de identificador y no tiene padre.\n";
					}
				}
				
				if( componenteActualCompleto ) {
					String contenido = "* " + unaEntidad.getId() + "/" + unaEntidad.getName() 
							+ " [completa]\n";
					byte[] cBytes = contenido.getBytes();
					str_completo = str_completo.concat(contenido);
					arStream.write(cBytes);
					arStream.flush();
				} else {
					String contenido = "* " + unaEntidad.getId() + "/" + unaEntidad.getName() 
							+ " [incompleta]...\n"
							+ carencias;
					byte[] cBytes = contenido.getBytes();
					str_completo = str_completo.concat(contenido);
					arStream.write(cBytes);
					arStream.flush();
				}
			}
			if (cantidadEntidades != 0)
				promedioAtributosEntidades = cantidadTotalAtributos
						/ cantidadEntidades;
			cantidadTotalAtributos = 0;

			
			String presentacion2 = "\nEstado de las relaciones del proyecto (id/nombre).\n";
			byte[] pBytes2 = presentacion2.getBytes();
			str_completo = str_completo.concat(presentacion2);
			arStream.write(pBytes2);
			arStream.flush();		

			List<ar.uba.fi.taller2.tp.model.Relation> relaciones = AppContext.getInstance().getProjectContext().getRelations();
			Iterator<ar.uba.fi.taller2.tp.model.Relation> iterRel = relaciones.iterator();
			while (iterRel.hasNext()) {
				ar.uba.fi.taller2.tp.model.Relation unaRelacion = iterRel.next();
				cantidadRelaciones++;
				cantidadTotalAtributos += unaRelacion.getAttributes().size();
				
				boolean componenteActualCompleto = true;
				String carencias = "";
				
				if( unaRelacion.getName() == "") {
					componentesCompletos = false;
					componenteActualCompleto = false;
					carencias += "\tNombre inválido o inexistente.\n";
				}
				
				if( unaRelacion.getEntitys().isEmpty() ) {
					componentesCompletos = false;
					componenteActualCompleto = false;
					carencias += "\tCantidad de entidades insuficiente.\n";
				}

				if( componenteActualCompleto ) {
					String contenido = "* " + unaRelacion.getId() + "/" + unaRelacion.getName() 
							+ " [completa]\n";
					byte[] cBytes = contenido.getBytes();
					str_completo = str_completo.concat(contenido);
					arStream.write(cBytes);
					arStream.flush();
				} else {
					String contenido = "* " + unaRelacion.getId() + "/" + unaRelacion.getName() 
							+ " [incompleta]...\n"
							+ carencias;
					byte[] cBytes = contenido.getBytes();
					str_completo = str_completo.concat(contenido);
					arStream.write(cBytes);
					arStream.flush();
				}
			}
			if (cantidadRelaciones != 0)
				promedioAtributosRelaciones = cantidadTotalAtributos
						/ cantidadRelaciones;

			List<DiagramTree> diagramas = AppContext.getInstance().getProjectEntity().getDiagramas();
			Iterator<DiagramTree> iterDiag = diagramas.iterator();
			while (iterDiag.hasNext()) {
				DiagramTree unDiag = iterDiag.next();
				cantidadDiagramas++;
				promedioElementosPorDiagrama += unDiag.getEntities().size()
						+ unDiag.getRelations().size();
			}
			
			float cantTotalElementos = promedioElementosPorDiagrama;
			if (cantidadDiagramas != 0)
				promedioElementosPorDiagrama /= cantidadDiagramas;
			
			// Establece las métricas que aplican sobre los distitos
			// diagramas.
			String presentacionA = "\nMetricas generales del proyecto."
					+ "\n\tPromedio de atributos por entidades: " + promedioAtributosEntidades
					+ "\n\tPromedio de atributos por relaciones: " + promedioAtributosRelaciones
					+ "\n\tCantidad total de entidades: " + cantidadEntidades
					+ "\n\tCantidad total de relaciones: " + cantidadRelaciones
					+ "\n\tCantidad total de diagramas: " + cantidadDiagramas
					+ "\n\tCantidad total de elementos (!): " + cantTotalElementos
					+ "\n\tPromedio de elementos por diagrama: " + promedioElementosPorDiagrama + "\n";
			byte[] pBytesA = presentacionA.getBytes();
			str_completo = str_completo.concat(presentacionA);
			arStream.write(pBytesA);
			arStream.flush();
			
			String presentacion3 = "\nMetricas que aplican a los diagramas del proyecto.\n";
			byte[] pBytes3 = presentacion3.getBytes();
			str_completo = str_completo.concat(presentacion3);
			arStream.write(pBytes3);
			arStream.flush();
			
			// Vuelve a recorrer todos los diagramas.
			iterDiag = diagramas.iterator();
			while (iterDiag.hasNext()) {
				DiagramTree unDiag = iterDiag.next();
				float cantElementos = unDiag.getEntities().size()
						+ unDiag.getRelations().size();
				float dif = cantElementos - promedioElementosPorDiagrama;
				
				boolean desequilibrio = false;
				if ( ( dif < -2*promedioElementosPorDiagrama  ) || 
						( dif > 2*promedioElementosPorDiagrama  ) ) {
					desequilibrio = true;
				}
				
				String presentacion7 = "* nombre del diagrama: " + unDiag.getNombre() +
										"\n\tcantidad de elementos: " + cantElementos + 
										"\n\tdiferencia con el promedio: " + dif +
										"\n\testdo de equilibrio: ";
				
				if (desequilibrio) {
					presentacion7 +=  "desequilibrado\n";
				} else {
					presentacion7 +=  "equilibrado\n";
				}
				
				byte[] pBytes7 = presentacion7.getBytes();
				str_completo = str_completo.concat(presentacion7);
				arStream.write(pBytes7);
				arStream.flush();
			}
			
			String presentacion8 = "\n\n\n\n";
			byte[] pBytes8 = presentacion8.getBytes();
			str_completo = str_completo.concat(presentacion8);
			arStream.write(pBytes8);
			arStream.flush();
			
			arStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		
		/*Lo saco en una ventana para leerlo*/
		TextoFrame ventana = new TextoFrame(str_completo);
		ventana.setVisible(true);
		
	}

}
