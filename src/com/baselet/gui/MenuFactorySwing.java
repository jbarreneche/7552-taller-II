package com.baselet.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Entity;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.umlet.element.custom.EER_Entity;

public class MenuFactorySwing extends MenuFactory {

	private static MenuFactorySwing instance = null;
	public static MenuFactorySwing getInstance() {
		if (instance == null) instance = new MenuFactorySwing();
		return instance;
	}
	
	public JMenuItem createNew() {
		return createJMenuItem(false, NEW, KeyEvent.VK_N, true, null);
	}

	public JMenuItem createNewProject() {
		return createJMenuItem(false, NEW_PRO, KeyEvent.VK_P, true, null);
	}
//	public JMenuItem createOpen() {
//		return createJMenuItem(false, OPEN, KeyEvent.VK_O, true, null);
//	}
	public JMenuItem createOpenProject() {
		return createJMenuItem(false, OPEN_PRO, KeyEvent.VK_O, true, null);
	}
//	public JMenu createRecentFiles() {
//		final JMenu recentFiles = new JMenu();
//		recentFiles.setText(RECENT_FILES);
//		recentFiles.addMenuListener(new MenuListener() {
//			@Override public void menuDeselected(MenuEvent e) {}
//			@Override public void menuCanceled(MenuEvent e) {}
//			@Override public void menuSelected(MenuEvent e) {
//				recentFiles.removeAll();
//				for (final String file : Main.getInstance().getRecentFiles()) {
//					recentFiles.add(createJMenuItem(false, file, RECENT_FILES, file));
//				}
//			}
//		});
//		return recentFiles;
//	}

	public JMenuItem createSave() {
		return createJMenuItem(false, SAVE, KeyEvent.VK_S, true, null);
	}

//	public JMenuItem createSaveAs() {
//		return createJMenuItem(true, SAVE_AS, null);
//	}

	public JMenu createExportAs() {
		final JMenu export = new JMenu();
		export.setText(EXPORT_AS);
		diagramDependendComponents.add(export);
		for (final String format : Constants.exportFormatList) {
			export.add(createJMenuItem(true, format.toUpperCase() + "...", EXPORT_AS, format));
		}
		return export;
	}


//	public JMenuItem createEditCurrentPalette() {
//		return createJMenuItem(false, EDIT_CURRENT_PALETTE, null);
//	}

	public JMenuItem createOptions() {
		return createJMenuItem(false, OPTIONS, null);
	}

	public JMenuItem createPrint() {
		return createJMenuItem(true, PRINT, KeyEvent.VK_P, true, null);
	}

	public JMenuItem createExit() {
		return createJMenuItem(false, EXIT, null);
	}

	public JMenuItem createUndo() {
		return createJMenuItem(false, UNDO, KeyEvent.VK_Z, true, null);
	}

	public JMenuItem createRedo() {
		return createJMenuItem(false, REDO, KeyEvent.VK_Y, true, null);
	}

	public JMenuItem createDelete() {
		return createJMenuItem(false, DELETE, KeyEvent.VK_DELETE, false, null);
	}

	public JMenuItem createSelectAll() {
		return createJMenuItem(false, SELECT_ALL, KeyEvent.VK_A, true, null);
	}

	public JMenuItem createGroup() {
		return createJMenuItem(false, GROUP, KeyEvent.VK_G, true, null);
	}

	public JMenuItem createUngroup() {
		return createJMenuItem(false, UNGROUP, KeyEvent.VK_U, true, null);
	}

	public JMenuItem createCut() {
		return createJMenuItem(false, CUT, KeyEvent.VK_X, true, null);
	}

	public JMenuItem createCopy() {
		return createJMenuItem(false, COPY, KeyEvent.VK_C, true, null);
	}

	public JMenuItem createPaste() {
		return createJMenuItem(false, PASTE, KeyEvent.VK_V, true, null);
	}
	
	public JMenuItem createAddAttributeElement() {
		return createJMenuItem(false, ADD_ATTRIBUTE, null, true, null);
	}
	
	public JMenuItem createAddComposedAttributeElement() {
		return createJMenuItem(false, ADD_COMPOSED_ATTRIBUTE, null, true, null);
	}
	
	public JMenuItem createAddIdElement() {
		return createJMenuItem(false, ADD_ID, null, true, null);
	}
	
	public JMenuItem createCambiarTipo() {
		return createJMenuItem(false, CAMBIAR_TIPO, null, true, null);
	}
	
	public JMenuItem createAddComposedIdElement() {
		return createJMenuItem(false, ADD_COMPOSED_ID, null, true, null);
	}
	
//	public JMenuItem createAddComposedIdElement() {
//		// TODO Crear cuadro y mandar por parametro la lista
//		JMenuItem menuItem = new JMenuItem(ADD_COMPOSED_ID);
//		menuItem.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//TODO Abrir Popup
//			}
//		});
//		
//		
//		return createJMenuItem(false, ADD_COMPOSED_ID, null, true, new ArrayList<Long>());
//	}
	
	public JMenuItem createEditAttribute() {
		return createJMenuItem(false, EDIT_ATTRIBUTE, null, true, null);
	}
	public JMenuItem createEditRelationEntity() {
		return createJMenuItem(false, EDIT_RELATION_ENTITY, null, true, null);
	}
	

	public JMenuItem createAddEntityToRelationUp() {
		JMenu menu = new JMenu(ADD_ENTITY_TO_RELATION_UP);
		for (EER_Entity c : diagramEntities) {
			Entity entity = AppContext.getInstance().getProjectContext().getEntity(c.getId());
			JMenuItem item = createJMenuItem(false,entity!=null?entity.getName():"", ADD_ENTITY_TO_RELATION_UP, c.getId());
			menu.add(item);
			
		}
		return menu;
	}
	
	public JMenuItem createAddEntityToRelationDown() {
		JMenu menu = new JMenu(ADD_ENTITY_TO_RELATION_DOWN);
		for (EER_Entity c : diagramEntities) {
			Entity entity = AppContext.getInstance().getProjectContext().getEntity(c.getId());
			JMenuItem item = createJMenuItem(false, entity!=null?entity.getName():"", ADD_ENTITY_TO_RELATION_DOWN, c.getId());
			menu.add(item);
			
		}
		return menu;
	}
	
	public JMenuItem createGeneralizationElement() {
		return createJMenuItem(false, GENERALIZATION, null, true, null);
	}
	
	public JMenuItem createAddEntityToGeneralizationElement() {
		JMenu menu = new JMenu(ADD_ENTITY_TO_GENERALIZATION);
		for (EER_Entity c : diagramEntities) {
			Entity entity = AppContext.getInstance().getProjectContext().getEntity(c.getId());
			JMenuItem item = createJMenuItem(false, entity!=null?entity.getName():"", ADD_ENTITY_TO_GENERALIZATION, c.getId());
			menu.add(item);
			
		}
		return menu;
	}

//	public JMenu createSetColor(boolean fg) {
//		String name = (fg == true ? SET_FOREGROUND_COLOR : SET_BACKGROUND_COLOR);
//		JMenu menu = new JMenu(name);
//		menu.add(createJMenuItem(false, "default", name, "default"));
//		for (String color : Constants.colorMap.keySet()) {
//			JMenuItem item = createJMenuItem(false, color, name, color);
//			menu.add(item);
//			item.setIcon(new PlainColorIcon(color));
//		}
//		return menu;
//	}
	
	public JMenuItem createAboutProgram() {
		return createJMenuItem(false, ABOUT_PROGRAM, null);
	}

	public JMenu createAlign() {
		JMenu alignMenu = new JMenu(ALIGN);
		for (String direction : new String[]{"Left", "Right", "Top", "Bottom"}) {
			alignMenu.add(createJMenuItem(false, direction, ALIGN, direction));
		}
		return alignMenu;
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String name, Object param) {
		return createJMenuItem(grayWithoutDiagram, name, name, null, null, param);
	}
	
	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String name, Integer mnemonic, Boolean meta, Object param) {
		return createJMenuItem(grayWithoutDiagram, name, name, mnemonic, meta, param);
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String menuName, final String actionName, final Object param) {
		return createJMenuItem(grayWithoutDiagram, menuName, actionName, null, null, param);
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String menuName, final String actionName, Integer mnemonic, Boolean meta, final Object param) {
		JMenuItem menuItem = new JMenuItem(menuName);
		if (mnemonic != null) {
			menuItem.setMnemonic(mnemonic);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, (!meta ? 0 : SystemInfo.META_KEY.getMask())));
		}
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO hacer if de popupy pasar entidad con menu name = CONSTANS
				doAction(actionName, param);
			}
		});
		if (grayWithoutDiagram) diagramDependendComponents.add(menuItem);
		return menuItem;
	}

	

}
