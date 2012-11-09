/*******************************************************************************
 * Copyright (c) 2012 Melanie Bats and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Melanie Bats - Initial implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.pkgconfig.preferences;

import java.io.File;

import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class PkgConfigBinPathFieldEditor extends StringButtonFieldEditor {

	/**
	 * Initial path for the Browse dialog.
	 */
	private File filterPath = null;

	/**
	 * Creates a pkg-config bin editor
	 */
	protected PkgConfigBinPathFieldEditor() {
	}

	/**
	 * Creates a pkg-config bin editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public PkgConfigBinPathFieldEditor(String name, String labelText,
			Composite parent) {
		init(name, labelText);
		setErrorMessage(JFaceResources
				.getString("FileFieldEditor.errorMessage"));//$NON-NLS-1$
		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
		createControl(parent);
	}

	/**
	 * Helper that opens the directory chooser dialog.
	 * 
	 * @param startingDirectory
	 *            The directory the dialog will open in.
	 * @return File File or <code>null</code>.
	 * 
	 */
	private File getFile(File startingDirectory) {
		FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);

		if (startingDirectory != null) {
			fileDialog.setFilterPath(startingDirectory.getPath());
		} else if (this.filterPath != null) {
			fileDialog.setFilterPath(this.filterPath.getPath());
		}

		String dir = fileDialog.open();
		if (dir != null) {
			dir = dir.trim();
			if (dir.length() > 0) {
				return new File(dir);
			}
		}
		return null;
	}

	@Override
	/*
	 * (non-Javadoc) Method declared on StringButtonFieldEditor. Opens the
	 * directory chooser dialog and returns the selected directory.
	 */
	protected String changePressed() {
		File f = new File(getTextControl().getText());
		if (!f.exists()) {
			f = null;
		}
		File d = getFile(f);
		if (d == null) {
			return null;
		}

		return d.getAbsolutePath();
	}

	@Override
	/*
	 * (non-Javadoc) Method declared on StringFieldEditor. Checks whether the
	 * text input field contains a valid directory.
	 */
	protected boolean doCheckState() {
		String fileName = getTextControl().getText();
		fileName = fileName.trim();
		if (fileName.length() == 0 && isEmptyStringAllowed()) {
			return true;
		}
		File file = new File(fileName);
		return !file.isDirectory() && file.getName().contains("pkg-config"); //$NON-NLS-1$
	}

	/**
	 * Sets the initial path for the Browse dialog.
	 * 
	 * @param path
	 *            initial path for the Browse dialog
	 * @since 3.6
	 */
	public void setFilterPath(File path) {
		this.filterPath = path;
	}

}
