package parquimetros;


import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDesktopPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;




@SuppressWarnings("serial")
public class Inicio extends javax.swing.JFrame {
	private JButton btnInspector,btnAdmin;
	private Login login;
	private IngresoInspector inspector;
	private JDesktopPane jDesktopPane1;

	public static void main(String[]args) 
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Inicio inicio = new Inicio();
				inicio.setLocationRelativeTo(null);
				inicio.setVisible(true);
			}
		});
	}
	
	public Inicio() {
		super();
		initGUI();
		this.login = new Login(jDesktopPane1);
	      login.setLocation(0, -12);
	      this.login.setVisible(false);
	      this.jDesktopPane1.add(this.login);
	      this.inspector = new IngresoInspector(jDesktopPane1);
	      inspector.setLocation(0, -12);
	      this.inspector.setVisible(false);
	      this.jDesktopPane1.add(this.inspector);  
	      
		}
	
	private void initGUI() {
		try 
	      {
	         javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
	      } 
	      catch(Exception e) 
	      {
	         e.printStackTrace();
	      }
		try {
		setTitle("Inicio");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		{	getContentPane().setBackground(Color.WHITE);
            jDesktopPane1 = new JDesktopPane();
            getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
            jDesktopPane1.setPreferredSize(new java.awt.Dimension(800, 600));
         }
		
		{JLabel lblBienvenido = new JLabel("BIENVENIDO!");
		lblBienvenido.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenido.setBounds(47, 34, 292, 25);
		jDesktopPane1.add(lblBienvenido);
		}
		{btnInspector = new JButton("INGRESO INSPECTOR");
		btnInspector.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnInspector.setBounds(0, 72, 384, 97);
		jDesktopPane1.add(btnInspector);}
		btnInspector.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try
			      {
			       inspector.setMaximum(true);
			      }
			      catch (PropertyVetoException e) {}
			      inspector.setVisible(true); 
				
         }
		});
		
		
		
		{ btnAdmin = new JButton("INGRESO ADMINISTRADOR");
		btnAdmin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAdmin.setBounds(0, 164, 384, 97);
		jDesktopPane1.add(btnAdmin);
		}btnAdmin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try
			      {
			       login.setMaximum(false);
			      }
			      catch (PropertyVetoException e) {}
			      login.setVisible(true); 
				
         }
		});
		
		this.setSize(800, 600);
        pack();}
		catch (Exception e) {
	         e.printStackTrace();
	      }
	
		
	}


}