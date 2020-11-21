package parquimetros;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.sql.Types;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quick.dbtable.*;
import javax.swing.JList;
import javax.swing.ListSelectionModel; 


@SuppressWarnings("serial")
public class Administrador extends javax.swing.JInternalFrame
{
   private JPanel pnlConsulta;
   private JTextArea txtConsulta;
   private JButton botonBorrar;
   private JButton btnEjecutar;
   private DBTable tabla;    
   private JScrollPane scrConsulta;
   private JList<String> listaTablas,listaAtributos;
   private DefaultListModel<String> listmodel,listmodelAtributos;
   private Connection c;
   private Statement stmt;
   

   
   
   public Administrador(DBTable tabla) 
   {
	   super();
	  c= tabla.getConnection();
      IniciarListaTablas();
      initGUI(tabla);
      
   }
   
   private void IniciarListaTablas() {
	  
	   
	try {
		stmt = c.createStatement();
		ResultSet rs1=stmt.executeQuery("show tables;");
		listmodel=new DefaultListModel<String>();
		
		while(rs1.next())
			listmodel.addElement(rs1.getString("Tables_in_parquimetros"));
		
		listaTablas=new JList<String>(listmodel);
		listaTablas.setBounds(753, 98, 0, 0);
		listaTablas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		listaTablas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		rs1.close();
		stmt.close();
		
		
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	   
	      
   }
   
   private void initGUI(DBTable tabla) 
   {
      try {
         setPreferredSize(new Dimension(800, 600));
         this.setBounds(0, 0, 800, 600);
         setVisible(true);
         this.setTitle("Admin");
         setClosable(true);
         setMaximizable(true);
         
         this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
         
         this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               thisComponentHidden(evt);
            } 
          });
         getContentPane().setLayout(null);
         {
            pnlConsulta = new JPanel();
            pnlConsulta.setBounds(26, 5, 800, 186);
            getContentPane().add(pnlConsulta);
            {
               scrConsulta = new JScrollPane();
               pnlConsulta.add(scrConsulta);
               {
                  txtConsulta = new JTextArea();
                  scrConsulta.setViewportView(txtConsulta);
                  txtConsulta.setTabSize(3);
                  txtConsulta.setColumns(80);
                  txtConsulta.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                  txtConsulta.setText("Ingrese una nueva operacion");
                  txtConsulta.setFont(new java.awt.Font("Monospaced",0,12));
                  txtConsulta.setRows(10);
               }
            }
            {
               btnEjecutar = new JButton();
               pnlConsulta.add(btnEjecutar);
               btnEjecutar.setText("Ejecutar");
               btnEjecutar.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent evt) {
                     btnEjecutarActionPerformed(evt);
                  }
               });
            }
            {
            	botonBorrar = new JButton();
            	pnlConsulta.add(botonBorrar);
            	botonBorrar.setVisible(true);
            	botonBorrar.setText("Borrar");            
            	botonBorrar.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent arg0) {
            		  txtConsulta.setText("");            			
            		}
            	});
            }	
         }
         {
        	 // crea la tabla  
        	this.tabla = tabla;
        	     
           // setea la tabla para sólo lectura (no se puede editar su contenido)  
           tabla.setEditable(false);       
           tabla.setAutoscrolls(true);
         }
         {listaTablas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				cambioSeleccionado(evt);
			}
           });
         }
         {
        	 listaAtributos=new JList<String>();
        	 listaAtributos.setLocation(758, 98);
        	 listaAtributos.setVisible(true);
        	 listaAtributos.setBorder(BorderFactory.createLineBorder(Color.BLACK));
         }
         {
           getContentPane().add(tabla, BorderLayout.CENTER);
           getContentPane().add(listaTablas);
           getContentPane().add(listaAtributos);
           tabla.setBounds(10, 210, 480,280);
           listaTablas.setBounds(500,210,140,280);
           listaAtributos.setBounds(650,210,140,280);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   
   
   private void thisComponentHidden(ComponentEvent evt) 
   {
	  
      this.desconectarBD();
      
   }

   private void btnEjecutarActionPerformed(ActionEvent evt) 
   {
	  String frase= this.txtConsulta.getText().trim().toLowerCase();
	  String[] partes = frase.split(" ",2);
	  if(partes[0].equals("select")) this.refrescarTabla();
	  else this.modificarTabla(partes[0],frase);      
   }
   

   private void desconectarBD()
   {
         try
         {
            tabla.close();            
         }
         catch (SQLException ex)
         {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
         }      
   }

   private void refrescarTabla()
   {
      try
      {    
    	  tabla.setSelectSql(this.txtConsulta.getText().trim());

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
   
   private void modificarTabla( String palabra,String frase) {
	   	
	   	try
	      {
	   		Connection conexionBD=tabla.getConnection();
	         Statement stmt = conexionBD.createStatement();
	         stmt.execute("show tables");
	      
	         if (palabra.equals("update")) stmt.executeUpdate(frase);
	         else stmt.execute(frase);
	         
	   		stmt.close();
	      
	      }
	      catch (SQLException ex)
	      {
	    	  JOptionPane.showMessageDialog(this,
                      "Se produjo un error a la hora de realizar el "+palabra+"\n"
                       + ex.getMessage(),
                       "Error",
                       JOptionPane.ERROR_MESSAGE);
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      }

	   }
   
   private void cambioSeleccionado(ListSelectionEvent evt) {
	  
	   try {
		stmt=c.createStatement();
		stmt.execute("describe "+listaTablas.getSelectedValue());
		ResultSet rs= stmt.getResultSet();
		listmodelAtributos=new DefaultListModel<String>();
		while(rs.next())
			listmodelAtributos.addElement(rs.getString(1));
	
		listaAtributos.setModel(listmodelAtributos);
		listaAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		rs.close();
		
		stmt.close();
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	   
   }
	   
   	   
   }

   

