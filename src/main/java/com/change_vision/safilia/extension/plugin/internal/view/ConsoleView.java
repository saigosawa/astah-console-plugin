package com.change_vision.safilia.extension.plugin.internal.view;

import java.awt.Color;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

@SuppressWarnings("serial")
public class ConsoleView extends JPanel implements IPluginExtraTabView {

	private static final SimpleAttributeSet DEFAULT_SIMPLE_ATTRIBUTE_SET = new SimpleAttributeSet();
	private static final SimpleAttributeSet ERROR_SIMPLE_ATTRIBUTE_SET;
	static{
		ERROR_SIMPLE_ATTRIBUTE_SET = new SimpleAttributeSet();
		StyleConstants.setForeground(ERROR_SIMPLE_ATTRIBUTE_SET, Color.red);
	}

	private final JTextPane pane = new JTextPane();

	private class OutputStreamExtension extends ByteArrayOutputStream{
		
		private SimpleAttributeSet attr;

		private OutputStreamExtension(){
			attr = DEFAULT_SIMPLE_ATTRIBUTE_SET;
		}
		
		private OutputStreamExtension(SimpleAttributeSet attr){
			this.attr = attr;			
		}
		
		@Override
		public void flush() throws IOException {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Document doc = pane.getDocument();
					try {
						String text = OutputStreamExtension.this.toString();
						doc.insertString(doc.getLength(), text, attr );
						OutputStreamExtension.this.reset();
					} catch (BadLocationException e) {
					}
				}
			});
		}
	}
	
	public ConsoleView(){
		PrintStream out = new PrintStream(new OutputStreamExtension(),true);
		System.setOut(out);
		PrintStream err = new PrintStream(new OutputStreamExtension(ERROR_SIMPLE_ATTRIBUTE_SET),true);
		System.setErr(err);
	}

	@Override
	public void activated() {
	}

	@Override
	public void addSelectionListener(ISelectionListener arg0) {
	}

	@Override
	public void deactivated() {
	}

	@Override
	public Component getComponent() {
		JScrollPane sp = new JScrollPane(pane);
		return sp;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getTitle() {
		return "Console";
	}
}
