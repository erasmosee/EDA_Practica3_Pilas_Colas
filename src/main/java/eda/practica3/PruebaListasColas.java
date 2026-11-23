package eda.practica3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.LectorPeliculasActores;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;
import eda.practica3.procesador.Utils;

public class PruebaListasColas {

    // Semilla fija: las dos mediciones usan las mismas parejas de actores.
    private static final long SEMILLA = 42L;

    /**
     * Uso: PruebaListasColas [rutaDatos] [segundosPorMedicion].
     * Por defecto usa datos.ruta de config.properties y 60 segundos.
     */
    public static void main(String[] args) throws IOException {
        Utils.traza("programa", "iniciando programa");
        long tiempoPrograma = Utils.iniciarCronometro();
        OrderedDoubleLinkedList<Actor> actores = new OrderedDoubleLinkedList<>();
        OrderedDoubleLinkedList<Pelicula> peliculas = new OrderedDoubleLinkedList<>();

        String ruta = Utils.cargarPropiedad("datos.ruta");
        if (args.length > 0) {
            ruta = args[0];
        }
        int segundos = 60;
        if (args.length > 1) {
            segundos = Integer.parseInt(args[1]);
        }

        long inicioLectura = System.currentTimeMillis();
        ejecutarPrueba(ruta, actores, peliculas);
        long tiempoLectura = System.currentTimeMillis() - inicioLectura;

        Graph grafo = new Graph();
        long inicioGrafo = System.currentTimeMillis();
        grafo.crearGrafo(peliculas);
        long tiempoGrafo = System.currentTimeMillis() - inicioGrafo;

        System.out.println("Peliculas: " + peliculas.size());
        System.out.println("Actores (V): " + grafo.keys.length);
        System.out.println("Relaciones (E): " + contarRelaciones(grafo));
        System.out.println("Tiempo de lectura (ms): " + tiempoLectura);
        System.out.println("Tiempo de crearGrafo (ms): " + tiempoGrafo);

        ArrayList<String> ejemplo = grafo.estanConectados(grafo.keys[0], grafo.keys[grafo.keys.length - 1]);
        System.out.println("Ejemplo de camino: " + (ejemplo.isEmpty() ? "sin conexion" : ejemplo));

        medirLlamadas(grafo, segundos, false);
        medirLlamadas(grafo, segundos, true);

        Utils.mostrarCronometro("programa", tiempoPrograma);
        Utils.traza("programa", "fin programa");
    }

    private static void ejecutarPrueba(String ruta, OrderedDoubleLinkedList<Actor> actores,
            OrderedDoubleLinkedList<Pelicula> peliculas) throws IOException {

        int tamanioLote = 128;
        LectorPeliculasActores lector = new LectorPeliculasActores();
        lector.leerCarpeta(ruta, actores, peliculas, tamanioLote);
    }

    // Cada relacion aparece en la lista de sus dos actores.
    private static long contarRelaciones(Graph grafo) {
        long suma = 0;
        for (ArrayList<Integer> vecinos : grafo.adjList) {
            suma += vecinos.size();
        }
        return suma / 2;
    }

    /**
     * Llama durante el tiempo indicado con parejas de actores aleatorias y
     * muestra cuantas llamadas se han completado y su equivalente por minuto.
     */
    private static void medirLlamadas(Graph grafo, int segundos, boolean conCamino) {
        Random azar = new Random(SEMILLA);
        int total = grafo.keys.length;
        long llamadas = 0;
        long conectados = 0;
        long inicio = System.nanoTime();
        long limite = inicio + segundos * 1_000_000_000L;

        while (System.nanoTime() < limite) {
            String a1 = grafo.keys[azar.nextInt(total)];
            String a2 = grafo.keys[azar.nextInt(total)];
            boolean enc = conCamino ? !grafo.estanConectados(a1, a2).isEmpty()
                    : grafo.estanConectadosBoolean(a1, a2);
            if (enc) {
                conectados++;
            }
            llamadas++;
        }

        double transcurridos = (System.nanoTime() - inicio) / 1e9;
        String metodo = conCamino ? "estanConectados (camino)" : "estanConectadosBoolean";
        System.out.printf("%s: %d llamadas en %.1f s, %d conectadas, %.0f llamadas por minuto%n",
                metodo, llamadas, transcurridos, conectados, llamadas / transcurridos * 60);
    }
}
