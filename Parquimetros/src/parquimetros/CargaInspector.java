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
public class CargaInspector extends javax.swing.JInternalFrame {
	private JPanel contentPane;
	private Connection conexionBD;
	private ArrayList<String> patentes;
	private ArrayList<String> exist;
 	private JFormattedTextField edPatentes;
	private JTextArea txPatentes;
	@SuppressWarnings("rawtypes")
	private JComboBox cbCalles;
	@SuppressWarnings("rawtypes")
	private JComboBox cbAlturas;
	@SuppressWarnings("rawtypes")
	private JComboBox cbParquimetros;
	private DBTable tabla;
	private int legajo;
	private int id_parq;

	public CargaInspector(int l) {
		super();
		legajo = l;
		tabla = new DBTable();
		tabla.setEditable(false);
		patentes = new ArrayList<String>();
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
		
		cbCalles = new JComboBox();
		cbCalles.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbCalles.setBounds(10, 11, 367, 22);
		pnCarga.add(cbCalles);
		
		cbAlturas = new JComboBox();
		cbAlturas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbAlturas.setBounds(10, 44, 178, 22);
		pnCarga.add(cbAlturas);
		
		cbParquimetros = new JComboBox();
		cbParquimetros.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbParquimetros.setBounds(199, 44, 178, 22);
		pnCarga.add(cbParquimetros);
		
		JButton btAgregar = new JButton("Agregar");
		btAgregar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btAgregar.setBounds(268, 77, 109, 22);
		pnCarga.add(btAgregar);
		btAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                agregar();
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
		
		txPatentes = new JTextArea();
		scrollPane.setViewportView(txPatentes);
		txPatentes.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txPatentes.setEditable(false);
		txPatentes.setColumns(4);
		
		JPanel pnResultado = new JPanel();
		contentPane.add(pnResultado);
		pnResultado.setLayout(new BorderLayout(0, 0));
		
		pnResultado.add(tabla, BorderLayout.CENTER);
		tabla.setVisible(false);
		String driver ="com.mysql.cj.jdbc.Driver";
    	String servidor = "localhost:3306";
    	String baseDatos = "parquimetros"; 
    	String usuario = "inspector";
    	String clave = "inspector";
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
		
		cargarVehiculosExistentes();
		cargarCalles();
		cargarAlturas();
		cargarParquimetros();
		
		cbCalles.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				cargarAlturas();
			}
		});
		cbParquimetros.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) seleccionarParquimetro();
			}
		});
		btAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aceptar();
             }
		});
	}

	@SuppressWarnings("unchecked")
	private void cargarCalles() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT calle FROM parquimetros GROUP BY calle;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbCalles.addItem(rs.getString("calle"));
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	@SuppressWarnings("unchecked")
	private void cargarAlturas() {
		try {
			cbAlturas.removeAllItems();
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT altura FROM parquimetros WHERE calle LIKE '"+cbCalles.getSelectedItem().toString()+"'"
						+ "GROUP BY altura;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbAlturas.addItem(String.valueOf(rs.getInt("altura")));
			}
			cbAlturas.setSelectedIndex(0);
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
			cbParquimetros.removeAllItems();
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT id_parq, numero FROM parquimetros "
						+ "WHERE calle LIKE '"+cbCalles.getSelectedItem().toString()+"' AND altura = "+cbAlturas.getSelectedItem().toString() 
						+ ";";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbParquimetros.addItem(String.valueOf(rs.getInt("id_parq"))+"-"+String.valueOf(rs.getInt("numero")));
			}
			seleccionarParquimetro();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private void seleccionarParquimetro() {
		String s = cbParquimetros.getSelectedItem().toString();
		int i = s.indexOf('-');
		id_parq = Integer.parseInt(s.substring(0,i));
	}
	
	private void agregar() {
		if (!patentes.contains(edPatentes.getText())) {
			if (exist.contains(edPatentes.getText())) {
				patentes.add(edPatentes.getText());
				String pat = txPatentes.getText();
				txPatentes.setText(pat + edPatentes.getText() + "\n");
			} else {
				JOptionPane.showMessageDialog(null, "Error: no existe automovil con esa patente.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "La patente ya esta en la lista.");
		}
	}
	
	private void aceptar() {
		Locale locale = new Locale("es","AR");
		DateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat fmtHora = new SimpleDateFormat("HH:mm:ss");
		DateFormat fmtDia = new SimpleDateFormat("EEEE",locale);
		Calendar cal = Calendar.getInstance();
		String dia = fmtDia.format(cal.getTime()).substring(0,2);
		String fecha = fmtFecha.format(cal.getTime());
		String hora = fmtHora.format(cal.getTime());
		String calle = cbCalles.getSelectedItem().toString();
    	String altura = cbAlturas.getSelectedItem().toString();
    	int h = cal.get(Calendar.HOUR_OF_DAY);
    	
    	boolean turno;
    	boolean horario;
	    String sqlAcceso = "INSERT INTO Accede(legajo,id_parq,fecha,hora) "
							+"VALUES ("+legajo+","+id_parq+",'"+fecha+"','"+hora+"');";
	    String sqlEstacionados = "SELECT patente FROM estacionados "
							+"WHERE calle LIKE '"+calle+"' AND altura = "+altura+";";
	    String sqlMultas;
	    String sqlResultado;
	    
	    if (patentes.isEmpty()) {
	    	JOptionPane.showMessageDialog(null,"Error: No ingreso patentes.");
	    	return;
	    }
	    txPatentes.setText("");
	    try {
	    	Statement stmt = this.conexionBD.createStatement();
	    	ResultSet rs;
	    	rs = stmt.executeQuery(sqlEstacionados);
			ArrayList<String> est = new ArrayList<String>();
			while(rs.next()) {
				est.add(rs.getString("patente"));
			}
			int id = validarTurno(h,calle,altura,dia);
			if (id>0) {
				stmt.executeUpdate(sqlAcceso);
    			for(String p:patentes) {
    				if(!est.contains(p)) {
    					sqlMultas = "INSERT INTO Multa(fecha,hora,patente,id_asociado_con) "
    							+ "VALUES('"+fecha+"','"+hora+"','"+p+"',"+id+");";
    					stmt.execute(sqlMultas);
    				}
    			}
    			sqlResultado = "SELECT m.numero, m.fecha, m.hora, a.calle, a.altura, m.patente, a.legajo " 
    					+ "FROM multa m NATURAL JOIN asociado_con a "
    					+ "WHERE a.calle LIKE '"+calle+"' AND a.altura = "+altura
    					+ " AND m.fecha = '"+fecha+"' AND m.hora = '"+hora+"' AND a.id_asociado_con = "+id+";";
    			rs = stmt.executeQuery(sqlResultado);
    			
    			
    			tabla.setVisible(true);
    	    	tabla.setSelectSql(sqlResultado); 
    	    	tabla.createColumnModelFromQuery();    	    
    	    	for (int i = 0; i < tabla.getColumnCount(); i++) {  		   		  
    	    		 if	 (tabla.getColumn(i).getType()==Types.TIME) tabla.getColumn(i).setType(Types.CHAR);
    	    	} 
    	   	    tabla.refresh();
    	   	    
    	   	    patentes.clear();
			} else {
				JOptionPane.showMessageDialog(null, "Error: No esta en turno.");
			}
	    } catch (SQLException ex) {
	    	System.out.println("aqui");
	    	System.out.println("SQLException: " + ex.getMessage());
	    	System.out.println("SQLState: " + ex.getSQLState());
	    	System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private void cargarVehiculosExistentes() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "SELECT patente FROM automoviles;";
			ResultSet rs = stmt.executeQuery(sql);
			exist = new ArrayList<String>();
			while(rs.next()) {
				exist.add(rs.getString("patente"));
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	private int validarTurno(int hora, String calle, String altura, String dia) {
		int id_asoc = -1;
		boolean valido = false;
		String sqlChequeo = "SELECT id_asociado_con, dia, turno FROM asociado_con "
				+"WHERE legajo = "+legajo+" AND calle LIKE '"+calle+"' AND altura = "+altura+";";
		String turno;
		try {
			Statement stmt = this.conexionBD.createStatement();
	    	ResultSet rs = stmt.executeQuery(sqlChequeo);
	    	while(rs.next() && !valido) {
	    		turno = rs.getString("turno");
	    		valido = rs.getString("dia").equals(dia);
	    		valido = valido && (turno.equals("M") && hora>=8 && hora<14) || (turno.equals("T") && hora>=14 && hora<=20);
	    		if (valido) id_asoc = rs.getInt("id_asociado_con");
	    	}
		} catch(SQLException ex) {			
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id_asoc;
	}

	
}
