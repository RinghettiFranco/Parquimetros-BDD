
package parquimetros;

import java.util.*;

import java.sql.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import quick.dbtable.*;

import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Font;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class VentanaParquimetro extends javax.swing.JInternalFrame {
	private JPanel contentPane;
	private Connection conexionBD;
	private ArrayList<String> patentes;
	private ArrayList<String> exist;
 	private JFormattedTextField edPatentes;
	private JTextArea txInfo;
	private DBTable tabla;
	@SuppressWarnings("rawtypes")
	private JComboBox cbUbicaciones;
	@SuppressWarnings("rawtypes")
	private JComboBox cbTarjetas;
	@SuppressWarnings("rawtypes")
	private JComboBox cbParquimetros;
	private int legajo;
	

	public VentanaParquimetro() {
		super();
		tabla = new DBTable();
		crearInterfaz();
		tabla.setEditable(false);
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
		cbUbicaciones.setBounds(10, 11, 367, 22);
		pnCarga.add(cbUbicaciones);
		
		cbTarjetas = new JComboBox();
		cbTarjetas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbTarjetas.setBounds(199, 44, 178, 22);
		pnCarga.add(cbTarjetas);
		
		cbParquimetros = new JComboBox();
		cbParquimetros.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbParquimetros.setBounds(10, 44, 178, 22);
		pnCarga.add(cbParquimetros);
		
		JButton btEjecutar = new JButton("Ejecutar");
		btEjecutar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btEjecutar.setBounds(268, 77, 109, 22);
		pnCarga.add(btEjecutar);
		btEjecutar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aceptar();
            }
		});
		
		JButton btAceptar = new JButton("Aceptar");
		btAceptar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btAceptar.setBounds(139, 317, 109, 22);
		pnCarga.add(btAceptar);
		
		try {
			edPatentes = new JFormattedTextField(new MaskFormatter("UUU###"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		edPatentes.setBounds(10, 77, 248, 22);
		pnCarga.add(edPatentes);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 110, 367, 198);
		pnCarga.add(scrollPane);
		
		txInfo = new JTextArea();
		scrollPane.setViewportView(txInfo);
		txInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txInfo.setEditable(false);
		txInfo.setColumns(4);
	
		JPanel pnResultado = new JPanel();
		contentPane.add(pnResultado);
		pnResultado.setLayout(new BorderLayout(0, 0));
		
		pnResultado.add(tabla, BorderLayout.CENTER);
		tabla.setVisible(false);
		String driver ="com.mysql.cj.jdbc.Driver";
    	String servidor = "localhost:3306";
    	String baseDatos = "parquimetros"; 
    	String usuario = "parquimetros";
    	String clave = "parq";
        String uriConexion = "jdbc:mysql://" + servidor + "/" + 
    	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
		try {
			tabla.connectDatabase(driver, uriConexion, usuario, clave);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conexionBD = tabla.getConnection();
		
		cargarUbicaciones();
		
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
	
	/*private void ejecutar() {
		
	    
	    try {
	    	Statement stmt = this.conexionBD.createStatement();
	    	ResultSet rs;
	    	rs = stmt.executeQuery(sqlEjecutar);
			//ArrayList<String> est = new ArrayList<String>();
			while(rs.next()) {
				//est.add(rs.getString("patente"));
			}
				
    			tabla.setVisible(true);
    	    	tabla.setSelectSql(sqlEjecutar); 
    	    	tabla.createColumnModelFromQuery();    	    
    	    	for (int i = 0; i < tabla.getColumnCount(); i++) {  		   		  
    	    		 if	 (tabla.getColumn(i).getType()==Types.TIME) tabla.getColumn(i).setType(Types.CHAR);
    	    	} 
    	   	    tabla.refresh();
    	   	    
    	   	    
			} 
	    catch(Exception e) {}
	    
	}*/

	private void ejecutar()
	   {
	      try
	      { // tabla.setRowCountSql(rowCountSql);
	    	 int id_parq=seleccionarParquimetro();
	  		 int id_tarj=seleccionarTarjeta();
	  	     String sqlEjecutar = "call conectar(1,1);";
	  	     
	  	    Connection conexionBD=tabla.getConnection();
	         Statement stmt = conexionBD.createStatement();
	         stmt.execute(sqlEjecutar);
	    	  
	    	 	    	  
	    	   tabla.createColumnModelFromQuery();    	    
	    	  for (int i = 0; i < tabla.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tabla.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		    tabla.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tabla.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }  
	    	   tabla.refresh();
	    	  
	    	  
	    	  
	       }
	      catch (SQLException ex)
	      {
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(this,
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	      
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
	    	System.out.println("error en aceptar");	    	
	    	System.out.println("SQLException: " + ex.getMessage());
	    	System.out.println("SQLState: " + ex.getSQLState());
	    	System.out.println("VendorError: " + ex.getErrorCode());
		}
	}	
}


		
	
		
	
	

