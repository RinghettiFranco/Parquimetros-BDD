package parquimetros;

import javax.swing.JFrame;

import quick.dbtable.DBTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class Login extends JFrame {
private JButton btnIngresar,btnCancelar;	
@SuppressWarnings("unused")
private Administrador administrador;
private JPasswordField passwordField;
private DBTable tabla;
private JLabel lblAviso,lblIntentos;
private int intentos=3;
	
	public Login() {
		initGUI();
		
		
		
	}
	
	private void initGUI() {
		setVisible(true);
		setTitle("Login Admin");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(400,300);
		
		
		btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(77, 192, 89, 23);
		getContentPane().add(btnIngresar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(216, 192, 89, 23);
		getContentPane().add(btnCancelar);
		
		lblAviso = new JLabel("Password incorrecto, intentelo de nuevo");
		lblAviso.setHorizontalAlignment(SwingConstants.CENTER);
		lblAviso.setForeground(Color.RED);
		lblAviso.setBounds(37, 147, 310, 14);
		getContentPane().add(lblAviso);
		lblAviso.setVisible(false);
		
		lblIntentos= new JLabel("Intentos Restantes: "+intentos);
		lblIntentos.setHorizontalAlignment(SwingConstants.CENTER);
		lblIntentos.setBounds(37, 167, 310, 14);
		getContentPane().add(lblIntentos);
		
		tabla=new DBTable();
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(135, 113, 113, 23);
		getContentPane().add(passwordField);
		passwordField.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
				String txt= new String(passwordField.getPassword());
				conectarBD(txt);
		    	}
		    	else lblAviso.setVisible(false);
		    }

		    public void keyReleased(KeyEvent e) {}

		    public void keyTyped(KeyEvent e) {  }
		});
		
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblPassword.setBounds(51, 117, 74, 14);
		getContentPane().add(lblPassword);
		
		JLabel lblBienvenido = new JLabel("Bienvenido Admin!");
		lblBienvenido.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenido.setBounds(92, 40, 199, 23);
		getContentPane().add(lblBienvenido);
		btnIngresar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
			 btnIngresarMouseCLicked(evt);
         }
		});
		
		btnCancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
			 btnCancelarMouseCLicked(evt);
         }
		});
		
	}
	
	private void btnIngresarMouseCLicked(ActionEvent evt) 
	   {
		String txt= new String(passwordField.getPassword());
		conectarBD(txt);
	   }
	
	private void btnCancelarMouseCLicked(ActionEvent evt) 
	   {
		dispose();
	   }
	
	private void conectarBD(String clave)
	   {
	         try
	         {  
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = "admin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	   
	       //establece una conexión con la  B.D. "parquimetros"  usando directamante una tabla DBTable    
	            tabla.connectDatabase(driver, uriConexion, usuario, clave);
	            administrador=new Administrador(tabla);
	            
	           
	         }
	         catch (SQLException ex)
	         {  
	        	 if (ex.getMessage().equals("Access denied for user 'admin'@'localhost' (using password: YES)")) 
	        	{lblAviso.setVisible(true); intentos--;
	        	//Si se queda sin intentos muestra un mensaje y cierra el login
	        	if(intentos==0) {
	        		JOptionPane.showMessageDialog(null,
	                           "Alcanzaste el limite de intentos para acceder",
	                           "Error",
	                            JOptionPane.ERROR_MESSAGE);
	        		dispose();
	        		
	        	}
	        	
	        	
	        	
	        	 lblIntentos.setText("Intentos restantes: "+ intentos);
	        	}
	        	 else{JOptionPane.showMessageDialog(this,
	                           "Se produjo un error al intentar conectarse a la base de datos.\n" 
	                            + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	        	 }
	         }
	         catch (ClassNotFoundException e)
	         {
	            e.printStackTrace();
	         }
	      
	   }
}
