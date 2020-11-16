package parquimetros;


import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




@SuppressWarnings("serial")
public class Inicio extends JFrame {
	private JButton btnInspector,btnAdmin;
	private Login login;
	private IngresoInspector inspector;

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
		setTitle("Inicio");
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JLabel lblBienvenido = new JLabel("BIENVENIDO!");
		lblBienvenido.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenido.setBounds(47, 34, 292, 25);
		getContentPane().add(lblBienvenido);
		
		JButton btnInspector = new JButton("INGRESO INSPECTOR");
		btnInspector.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnInspector.setBounds(0, 72, 384, 97);
		getContentPane().add(btnInspector);
		btnInspector.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
			 inspector=new IngresoInspector();
         }
		});
		
		
		
		JButton btnAdmin = new JButton("INGRESO ADMINISTRADOR");
		btnAdmin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAdmin.setBounds(0, 164, 384, 97);
		getContentPane().add(btnAdmin);
		btnAdmin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
			 login=new Login();
         }
		});
		
		
		setSize(400,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}