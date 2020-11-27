
package parquimetros;

import java.util.*;

import java.sql.*;


import quick.dbtable.*;

import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Font;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class VentanaParquimetro extends javax.swing.JInternalFrame {
	private JPanel contentPane;
	private Connection conexionBD;
	private JTextArea txInfo;
	@SuppressWarnings("rawtypes")
	private JComboBox cbUbicaciones;
	@SuppressWarnings("rawtypes")
	private JComboBox cbTarjetas;
	@SuppressWarnings("rawtypes")
	private JComboBox cbParquimetros;
		

	public VentanaParquimetro() {
		super();
		conectarBD();
		crearInterfaz();
		
	}

	@SuppressWarnings("rawtypes")
	private void crearInterfaz() {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setClosable(true);
		setMaximizable(true);
		setBounds(0, 0, 800, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel pnCarga = new JPanel();
		contentPane.add(pnCarga);
		pnCarga.setLayout(null);
		
		cbUbicaciones = new JComboBox();
		cbUbicaciones.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbUbicaciones.setBounds(10, 31, 367, 22);
		pnCarga.add(cbUbicaciones);
		
		cbTarjetas = new JComboBox();
		cbTarjetas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbTarjetas.setBounds(198, 77, 178, 22);
		pnCarga.add(cbTarjetas);
		
		cbParquimetros = new JComboBox();
		cbParquimetros.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbParquimetros.setBounds(10, 77, 178, 22);
		pnCarga.add(cbParquimetros);
		
		JButton btAceptar = new JButton("Aceptar");
		btAceptar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btAceptar.setBounds(139, 317, 109, 22);
		pnCarga.add(btAceptar);
		btAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aceptar();
            }
		});
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 110, 367, 198);
		pnCarga.add(scrollPane);
		
		txInfo = new JTextArea();
		scrollPane.setViewportView(txInfo);
		txInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txInfo.setEditable(false);
		txInfo.setColumns(4);
		
		JLabel lblUbicacion = new JLabel("Ubicacion:");
		lblUbicacion.setBounds(30, 11, 46, 14);
		pnCarga.add(lblUbicacion);
		
		JLabel lblParquimetro = new JLabel("Parquimetro:");
		lblParquimetro.setBounds(30, 63, 82, 14);
		pnCarga.add(lblParquimetro);
		
		JLabel lblTarjeta = new JLabel("Tarjeta:");
		lblTarjeta.setBounds(218, 63, 46, 14);
		pnCarga.add(lblTarjeta);
	
	
		cargarUbicaciones();
		cargarTarjetas();
		cbUbicaciones.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				cargarTarjetas();
			}
		});
		cbParquimetros.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) seleccionarParquimetro();
			}
		});
		
	}

	@SuppressWarnings("unchecked")
	private void cargarUbicaciones() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT calle,altura FROM ubicaciones";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbUbicaciones.addItem(rs.getString("calle")+" al " +rs.getInt("altura"));
			}
			cargarParquimetros();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarTarjetas() {
		try {
			cbTarjetas.removeAllItems();
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT * FROM tarjetas;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbTarjetas.addItem(rs.getInt("id_tarjeta")+"-"+ rs.getString("patente"));
			}
			cbTarjetas.setSelectedIndex(0);
			cargarParquimetros();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarParquimetros() {
		try {
			String[] parts = cbUbicaciones.getSelectedItem().toString().split("al");
			String calle=parts[0];
			String alt=parts[1];
			calle=calle.trim();
			alt=alt.trim();
			int altura=Integer.parseInt(alt);
			
			cbParquimetros.removeAllItems();
			Statement stmt = this.conexionBD.createStatement();
			String sql= "Select id_parq, numero FROM parquimetros WHERE calle = '"+calle+"' AND altura ="+altura+";";
					
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				
				cbParquimetros.addItem(String.valueOf(rs.getInt("id_parq"))+"-"+String.valueOf(rs.getInt("numero")));
			}
			//seleccionarParquimetro();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private int seleccionarParquimetro() {
		int id_parq=0;
		if(cbParquimetros.getSelectedItem()!=null) {
			String s = cbParquimetros.getSelectedItem().toString();
		int i = s.indexOf('-');
		id_parq = Integer.parseInt(s.substring(0,i));}
		return id_parq;
	}
	
	private int seleccionarTarjeta() {
		int id_tarj=0;
		if(cbTarjetas.getSelectedItem()!=null) {
			String s = cbTarjetas.getSelectedItem().toString();
		int i = s.indexOf('-');
		id_tarj = Integer.parseInt(s.substring(0,i));}
		return id_tarj;
	}
	
		
	private void aceptar() {
		try{
			if(cbParquimetros.getSelectedItem()!=null) {
				txInfo.setText("");
				int tarjeta, parquimetro;
				tarjeta= seleccionarTarjeta();
				parquimetro= seleccionarParquimetro();
				String consulta = "{call conectar(?, ?)}";
				CallableStatement stmt= conexionBD.prepareCall(consulta);
				stmt.setInt(1,tarjeta);
				stmt.setInt(2,parquimetro); 
				stmt.execute();
				ResultSet rs =stmt.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();
				if(rs.next()) {
					String resultado;
					if(rs.getString(1).startsWith("Error: ")) {
						resultado = rs.getString(1);
					} else {
						resultado = rsmd.getColumnName(1)+": "+rs.getString(1)+" \n"+
									rsmd.getColumnName(2)+": "+rs.getString(2)+" \n"+
									rsmd.getColumnName(3)+": "+rs.getString(3);
					}
					txInfo.setText(resultado);
					stmt.close();
				}
			}
	    } catch (SQLException ex) {
	    		    	
	    	System.out.println("SQLException: " + ex.getMessage());
	    	System.out.println("SQLState: " + ex.getSQLState());
	    	System.out.println("VendorError: " + ex.getErrorCode());
		}
	}	
	private void conectarBD() {
		if (this.conexionBD == null) {               
			try {
				String servidor = "localhost:3306";
				String baseDatos = "parquimetros";
	            String usuario = "parquimetros";
	            String clave = "parq";
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
}


		
	
		
	
	

