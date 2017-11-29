
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/******************************************************************************
 *  Compilation:  javac CPM.java
 *  Execution:    java CPM < input.txt
 *  Dependencies: EdgeWeightedDigraph.java AcyclicLP.java StdOut.java
 *  Data files:   jobsPC.txt
 *
 *  Critical path method.
 *
 *  % java CPM < jobsPC.txt
 *   job   start  finish
 *  --------------------
 *     0     0.0    41.0
 *     1    41.0    92.0
 *     2   123.0   173.0
 *     3    91.0   127.0
 *     4    70.0   108.0
 *     5     0.0    45.0
 *     6    70.0    91.0
 *     7    41.0    73.0
 *     8    91.0   123.0
 *     9    41.0    70.0
 *  Finish time:   173.0
 *
 ******************************************************************************/



/**
 *  The <tt>CPM</tt> class provides a client that solves the
 *  parallel precedence-constrained job scheduling problem
 *  via the <em>critical path method</em>. It reduces the problem
  hacia the longest-paths problem in edge-weighted DAGs.
  It builds an edge-weighted digraph (which must be a DAG)
  desde the job-scheduling problem specification,
  finds the longest-paths tree, and computes the longest-paths
  lengths (which are precisely the start times for each job).
  <p>
 *  This implementation uses {@link LPAciclico} hacia find a longest
  path in a DAG.
  The running time is proportional hacia <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of jobs and <em>E</em> is the
 *  number of precedence constraints.
 *  <p>
 */
public class CPM {

    // this class cannot be instantiated
    private CPM() { }

    /**
     *  Reads the precedence constraints desde standard input
  and prints a feasible schedule hacia standard output.
     */
    public static void main(String[] args) throws IOException {

        // number of jobs
        //String fileName= "/home/tineo/ProyectoED2/GraDigPonderados/jobsPC.txt";

        CPMWindow win =  new CPMWindow();
        win.jTable1.setModel(new DefaultTableModel() { });

        win.jButton1.addActionListener(e -> {

            String aux="", texto="";
            try
            {
                /**llamamos el metodo que permite cargar la ventana*/
                JFileChooser file=new JFileChooser();
                file.showOpenDialog(win);
                /**abrimos el archivo seleccionado*/
                File abre = file.getSelectedFile();

                win.jTextField1.setText(abre.getAbsolutePath());
                /**recorremos el archivo, lo leemos para plasmarlo
                 *en el area de texto*/
                if(abre!=null)
                {
                    FileReader archivo=new FileReader(abre);

                    int N = 10;
                    // source and sink
                    int inicio = 2*N;
                    int fin   = 2*N + 1;
                    int linea = -1;

                    DigrafoAristaPonderada G = new DigrafoAristaPonderada(2*N + 2);

                    try (Stream<String> stream = Files.lines(Paths.get(abre.getAbsolutePath()))) {

                        for( String line : (Iterable<String>) stream::iterator ) {
                            if (linea < 0) {
                                N = Integer.parseInt(line);
                                inicio = 2*N;
                                fin   = 2*N + 1;
                                G = new DigrafoAristaPonderada(2*N + 2);
                            }else{
                                System.out.println(line);
                                List<String> items = Arrays.asList(line.split("\\s*  \\s*"));

                                G.agregarArista(new AristaDirigida(inicio, linea, 0.0));
                                G.agregarArista(new AristaDirigida(linea + N, fin, 0.0));
                                G.agregarArista(new AristaDirigida(linea,N + linea,Double.parseDouble(items.get(0))));

                                if(Integer.parseInt((items.get(1)))>0) {
                                    List<String> nums = Arrays.asList(items.get(2).split("\\s* \\s*"));
                                    for (String num :   nums) {
                                        G.agregarArista(new AristaDirigida(N + linea,Integer.parseInt(num), 0.0));
                                    }
                                }
                            }
                            linea++;
                        }
                        LPAciclico cml = new LPAciclico(G, inicio);
                        // print results
                        StdOut.println(" Tra   inicio  fin");
                        StdOut.println("--------------------");
                        Object[] header = new Object[]{"Tarea", "Inicio", "Fin"};
                        DefaultTableModel model = new DefaultTableModel(header, 0);
                        for (int m = 0; m < N; m++) {
                            StdOut.printf("%4d %7.1f %7.1f\n", m, cml.distanciaHacia(m),
                                   cml.distanciaHacia(m + N));
                            model.addRow(new Object[]{m, cml.distanciaHacia(m), cml.distanciaHacia(m + N)});
                        }
                        win.jTable1.setModel(model);
                        win.jTextField2.setText(String.valueOf(cml.distanciaHacia(fin)));

                        StdOut.printf("Tiempo de terminación: %7.1f\n", cml.distanciaHacia(fin));
                    }
                }
            }
            catch(IOException ex)
            {
                JOptionPane.showMessageDialog(null,ex+"" +
                                "\nNo se ha encontrado el archivo",
                        "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
            }
        });
        win.setVisible(true);
    }
}
