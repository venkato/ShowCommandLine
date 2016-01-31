package nik.git.forcepush.jrr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.pgm.CommandRef;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.util.io.ThrowingPrintWriter;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import net.sf.jremoterun.utilities.JrrClassUtils;

public class PushForceImpl implements IObjectActionDelegate {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger
			.getLogger(JrrClassUtils.getCurrentClass().getName());

	private IWorkbenchPart activePart;
	private File selectedRepositoryLocation;

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		LOG.info("1");
		activePart = targetPart;
		updateActionEnablement(action);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		LOG.info("1");
		SelectedRepositoryComputer computer = new SelectedRepositoryComputer(selection, activePart);
		selectedRepositoryLocation = computer.compute();
		updateActionEnablement(action);
	}

	@Override
	public void run(IAction action) {
		LOG.info("1" + selectedRepositoryLocation);
		try {
			// Git git = Git.open(selectedRepositoryLocation);
			// Iterable<PushResult> call = git.push().setForce(true).call();
			forceFush(selectedRepositoryLocation);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, null, e);
		}
	}

	private static void forceFush(File file) throws Exception {
		CommandRef commandRef = org.eclipse.jgit.pgm.CommandCatalog.get("push");
		TextBuiltin create = commandRef.create();
		// create.subcommand;
		org.eclipse.jgit.api.Git git = Git.open(file);
		StringWriter outwW = new StringWriter();
		StringWriter errw2 = new StringWriter();
		// StringWriter sw2 = new StringWriter();
		org.eclipse.jgit.util.io.ThrowingPrintWriter errw = new ThrowingPrintWriter(errw2);
		org.eclipse.jgit.util.io.ThrowingPrintWriter outw = new ThrowingPrintWriter(outwW);
		ByteArrayOutputStream outs = new ByteArrayOutputStream();
		ByteArrayOutputStream errs = new ByteArrayOutputStream();
		JrrClassUtils.invokeMethod(create, "init", git.getRepository(), file.getAbsolutePath());
		JrrClassUtils.setFieldValue(create, "force", true);
		JrrClassUtils.setFieldValue(create, "outs", outs);
		JrrClassUtils.setFieldValue(create, "errs", errs);
		JrrClassUtils.setFieldValue(create, "errw", errw);
		JrrClassUtils.setFieldValue(create, "outw", outw);

		String[] args = {};
		create.execute(args);
		String output = "1: " + outwW.toString() + " 2: " + errw2.toString() + " 3: " + new String(outs.toByteArray())
				+ " 4: " + new String(errs.toByteArray());
		LOG.info(output);
		MessageDialog.openWarning(null, "Git push force", file.getAbsolutePath() + " " + output);
	}

	private void updateActionEnablement(IAction action) {
		action.setEnabled(activePart != null && selectedRepositoryLocation != null);
	}

}
