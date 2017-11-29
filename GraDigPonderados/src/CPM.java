
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String fileName= "/home/tineo/ProyectoED2/GraDigPonderados/jobsPC.txt";


        /*StringBuilder sb = new StringBuilder();
        while(in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        String outString = sb.toString();

        System.out.println(outString);*/

        int N = 10;
        // source and sink
        int inicio = 2*N;
        int fin   = 2*N + 1;

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(System.out::println);
        }
        // build network
        DigrafoAristaPonderada G = new DigrafoAristaPonderada(2*N + 2);

        //int N = StdIn.readInt();
         //       int inicio = 2*N;
         //       int fin   = 2*N + 1;
         //       DigrafoAristaPonderada G = new DigrafoAristaPonderada(2*N + 2);
          for (int i = 0; i < N; i++) {
              double duracion = StdIn.readDouble();
              G.agregarArista(new AristaDirigida(inicio, i, 0.0));
              G.agregarArista(new AristaDirigida(i + N, fin, 0.0));
              G.agregarArista(new AristaDirigida(i, i + N, duracion));

              // precedence constraints
              int M = StdIn.readInt();
              for (int j = 0; j < M; j++) {
                  int precedente = StdIn.readInt();
                  G.agregarArista(new AristaDirigida(N + i, precedente, 0.0));
              }

        /*
           double duracion = 41.0;
            G.agregarArista(new AristaDirigida(inicio, 0, 0.0));
            G.agregarArista(new AristaDirigida(10, fin, 0.0));
            G.agregarArista(new AristaDirigida(0,10,duracion));
            //3 datos
                G.agregarArista(new AristaDirigida(10,1, 0.0));
                G.agregarArista(new AristaDirigida(10, 7, 0.0));
                G.agregarArista(new AristaDirigida(10, 9, 0.0));
       double duracion2 = 51.0;
            G.agregarArista(new AristaDirigida(inicio, 1, 0.0));
            G.agregarArista(new AristaDirigida(11, fin, 0.0));
            G.agregarArista(new AristaDirigida(1,11,duracion2));
               //1 dato
                G.agregarArista(new AristaDirigida(11,2, 0.0));
                
        double duracion3 = 50.0;
            G.agregarArista(new AristaDirigida(inicio, 2, 0.0));
            G.agregarArista(new AristaDirigida(12, fin, 0.0));
            G.agregarArista(new AristaDirigida(2,12,duracion3));
          
        double duracion4 = 36.0;
            G.agregarArista(new AristaDirigida(inicio, 3, 0.0));
            G.agregarArista(new AristaDirigida(13, fin, 0.0));
            G.agregarArista(new AristaDirigida(3,13,duracion4));
        double duracion5 = 38.0;
            G.agregarArista(new AristaDirigida(inicio, 4, 0.0));
            G.agregarArista(new AristaDirigida(14, fin, 0.0));
            G.agregarArista(new AristaDirigida(4,14,    duracion5));
        double duracion6 = 45.0;
            G.agregarArista(new AristaDirigida(inicio, 5, 0.0));
            G.agregarArista(new AristaDirigida(15, fin, 0.0));
            G.agregarArista(new AristaDirigida(5,15,    duracion6));
            
        double duracion7 = 21.0;
            G.agregarArista(new AristaDirigida(inicio, 6, 0.0));
            G.agregarArista(new AristaDirigida(16, fin, 0.0));
            G.agregarArista(new AristaDirigida(6,16,    duracion7));
            // 2 datos
               G.agregarArista(new AristaDirigida(16,3, 0.0));
                G.agregarArista(new AristaDirigida(16, 8, 0.0));
        double duracion8 = 32.0;
            G.agregarArista(new AristaDirigida(inicio, 7, 0.0));
            G.agregarArista(new AristaDirigida(17, fin, 0.0));
            G.agregarArista(new AristaDirigida(7,17,    duracion8));
            // 2 datos
               G.agregarArista(new AristaDirigida(17,3, 0.0));
                G.agregarArista(new AristaDirigida(17, 8, 0.0));
        double duracion9 = 32.0;
            G.agregarArista(new AristaDirigida(inicio, 8, 0.0));
            G.agregarArista(new AristaDirigida(18, fin, 0.0));
            G.agregarArista(new AristaDirigida(8,18,    duracion9));
            // 1 dato
               G.agregarArista(new AristaDirigida(18,2, 0.0));
        double duracion10 = 29.0;
            G.agregarArista(new AristaDirigida(inicio, 9, 0.0));
            G.agregarArista(new AristaDirigida(19, fin, 0.0));
            G.agregarArista(new AristaDirigida(9,19,    duracion8));
            // 2 datos
               G.agregarArista(new AristaDirigida(19,4, 0.0));
                G.agregarArista(new AristaDirigida(19,6, 0.0));
            */
              // compute longest path
              LPAciclico cml = new LPAciclico(G, inicio);

              // print results
              StdOut.println(" Tra   inicio  fin");
              StdOut.println("--------------------");
              for (int m = 0; m < N; m++) {
                  StdOut.printf("%4d %7.1f %7.1f\n", m, cml.distanciaHacia(m),
                          cml.distanciaHacia(m + N));
              }
              StdOut.printf("Tiempo de terminación: %7.1f\n", cml.distanciaHacia(fin));
          }
    }
}
