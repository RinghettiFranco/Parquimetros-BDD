package parquimetros;

import javax.swing.JFrame;

import quick.dbtable.DBTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class Login extends javax.swing.JInternalFrame {
private JButton btnIngresar,btnCancelar;	
@SuppressWarnings("unused")
private Administrador administrador;
private JTextField textfield;
private JPasswordField passwordField;
private DBTable tabla;
private JLabel lblAviso,labelUsuario,labelContra;
private JDesktopPane pane;	
	public Login(JDesktopPane jdp) {
		super();
		initGUI();
		pane=jdp;
		
		
	}
	
	private void initGUI() {
		setVisible(true);
		setTitle("Login Admin");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(400,300);
		this.setMaximizable(true);
		this.setClosable(true);
		btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(77, 192, 89, 23);
		getContentPane().add(btnIngresar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(216, 192, 89, 23);
		getContentPane().add(btnCancelar);
		
		lblAviso = new JLabel("Password incorrecto, intentelo de nuevo");
		lblAviso.setHorizontalAlignment(SwingConstants.CENTER);
		lblAviso.setForeground(Color.RED);
		lblAviso.setBounds(37, 164, 310, 14);
		getContentPane().add(lblAviso);
		lblAviso.setVisible(false);
		
		tabla=new DBTable();
		addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               thisComponentHidden(evt);
            }
         });
	    
		
		
		textfield = new JTextField();
		textfield.setBounds(135, 81, 113, 23);
		getContentPane().add(textfield);
		textfield.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
				conectarBD();
		    	}
		    	else lblAviso.setVisible(false);
		    }

		    public void keyReleased(KeyEvent e) {}

		    public void keyTyped(KeyEvent e) {  }
		});

		
		passwordField = new JPasswordField();
		passwordField.setBounds(135, 115, 113, 23);
		getContentPane().add(passwordField);
		passwordField.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
				
				conectarBD();
		    	}
		    	else lblAviso.setVisible(false);
		    }

		    public void keyReleased(KeyEvent e) {}

		    public void keyTyped(KeyEvent e) {  }
		    });
		
		
		labelUsuario= new JLabel("Usuario:");
		labelUsuario.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelUsuario.setBounds(51, 84, 74, 14);
		getContentPane().add(labelUsuario);
		
		labelContra = new JLabel("Password:");
		labelContra.setFont(new Font("Verdana", Font.PLAIN, 11));
		labelContra.setBounds(51, 118, 74, 14);
		getContentPane().add(labelContra);
		
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
		conectarBD();
	   }
	
	private void btnCancelarMouseCLicked(ActionEvent evt) 
	   {
		clean();
		
	   }
	
	private void thisComponentHidden(ComponentEvent evt) {
		clean();
	}
	
	private void clean() {
		
        this.setVisible(false);
        lblAviso.setVisible(false);
	    textfield.setText("");
	    passwordField.setText("");

		
	}
	
	private void conectarBD()
	   {
	         try
	         {  
	        	String clave=new String(passwordField.getPassword());
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = textfield.getText();
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	   
	       //establece una conexión con la  B.D. "parquimetros"  usando directamante una tabla DBTable    
	            tabla.connectDatabase(driver, uriConexion, usuario, clave);
	            administrador=new Administrador(tabla);
	            try
			      {
	               pane.add(administrador);
	               administrador.setMaximum(true);
			       clean();
			      }
			      catch (PropertyVetoException e) {}
			      administrador.setVisible(true); 
	           
	         }
	         catch (SQLException ex)
	         {  
	        	 if (ex.getMessage().equals("Access denied for user '"+textfield.getText()+"'@'localhost' (using password: YES)")) 
	        	{lblAviso.setVisible(true);	
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
