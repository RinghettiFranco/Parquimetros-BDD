package parquimetros;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.sql.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class IngresoInspector extends javax.swing.JInternalFrame {

	private Connection conexionBD;
	private JPanel contentPane;
	private JPasswordField edContraseña;
	private JTextField edLegajo;
	private JTextArea txEstado;
	private CargaInspector carga;
	private JDesktopPane pane;
	public IngresoInspector(JDesktopPane jdp) {
		super();
		pane=jdp;
		setVisible(true);
		crearInterfaz();
		conectarBD();
	}
	
	private void crearInterfaz() {	
		setTitle("Ingreso inspector");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setClosable(true);
		setMaximizable(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	    contentPane.setLayout(new GridLayout(2, 0, 0, 0));
	    addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               thisComponentHidden(evt);
            }
         });
	    
	    JPanel pnDatos = new JPanel();
	    contentPane.add(pnDatos);
	    pnDatos.setLayout(null);
	    
	    JLabel lbLegajo = new JLabel("Legajo N\u00B0:");
	    lbLegajo.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    lbLegajo.setBounds(10, 32, 98, 25);
	    pnDatos.add(lbLegajo);
	    
	    JLabel lbContraseña = new JLabel("Contrase\u00F1a:");
	    lbContraseña.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    lbContraseña.setBounds(10, 62, 115, 25);
	    pnDatos.add(lbContraseña);
	    
	    edContraseña = new JPasswordField();
	    edContraseña.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    edContraseña.setBounds(157, 62, 257, 25);
	    pnDatos.add(edContraseña);
	    
	    edLegajo = new JTextField();
	    edLegajo.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    edLegajo.setColumns(10);
	    edLegajo.setBounds(157, 32, 257, 25);
	    pnDatos.add(edLegajo);
	    
	    JPanel pnIngreso = new JPanel();
	    contentPane.add(pnIngreso);
	    pnIngreso.setLayout(null);
	    
	    JButton btIngresar = new JButton("Ingresar");
	    btIngresar.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    btIngresar.setBounds(139, 11, 146, 33);
	    btIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               verificar(evt);
            }
         });
	    pnIngreso.add(btIngresar);
	    
	    txEstado = new JTextArea();
	    txEstado.setBackground(UIManager.getColor("Panel.background"));
	    txEstado.setEditable(false);
	    txEstado.setFont(new Font("Tahoma", Font.BOLD, 20));
	    txEstado.setBounds(10, 55, 404, 70);
	    pnIngreso.add(txEstado);
	}
	
	//Manejo bdd
	private void conectarBD() {
		if (this.conexionBD == null) {               
			try {
				String servidor = "localhost:3306";
				String baseDatos = "parquimetros";
	            String usuario = "inspector";
	            String clave = "inspector";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	        
	         }
	         catch (SQLException ex) {
	            JOptionPane.showMessageDialog(this,
	             "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
	              "Error", JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
		}
	}	

	private void desconectarBD() {
		if (this.conexionBD != null) {
			try {
				this.conexionBD.close();
				this.conexionBD = null;
			} catch (SQLException ex) {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
	
	private void clean() {
		this.setVisible(false);
		txEstado.setText("");
		edContraseña.setText("");
		edLegajo.setText("");
		
	}
	
	//Funciones oyentes
	private void thisComponentHidden(ComponentEvent evt) {
		desconectarBD();
		clean();
	}
	
	private void verificar(ActionEvent evt) {
		try {
			conectarBD();
			Statement stmt = this.conexionBD.createStatement();
			System.out.println(stmt.getFetchSize());
			String pass = String.valueOf(edContraseña.getPassword());
			int legajo = Integer.parseInt(edLegajo.getText());
			String sql = "SELECT legajo, password FROM inspectores WHERE password=md5('"+pass+"') and legajo="+legajo;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				carga = new CargaInspector(rs.getInt("legajo"));
				pane.add(carga);
				try
			      {
					carga.setMaximum(true);
					
			      }
			      catch (PropertyVetoException e) {}
				 
				carga.setVisible(true); 
				clean();
			} else {
				txEstado.setForeground(new Color(255,0,0));
				txEstado.setText("Ingreso fallido: \n Revise legajo y/o contraseña.");
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (NumberFormatException ex) {
			txEstado.setForeground(new Color(255,0,0));
			txEstado.setText("Ingreso fallido: \n Legajo inválido.");
		}
	}
}
