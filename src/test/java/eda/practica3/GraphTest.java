package eda.practica3;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.LectorPeliculasActores;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;
import eda.practica3.procesador.Utils;

public class GraphTest {

    @Test
    void compruebaConexionesDelGrafo() throws IOException {

        OrderedDoubleLinkedList<Actor> actores = new OrderedDoubleLinkedList<>();
        OrderedDoubleLinkedList<Pelicula> peliculas = new OrderedDoubleLinkedList<>();

        String ruta = Utils.cargarPropiedad("datos.ruta");

        LectorPeliculasActores lector = new LectorPeliculasActores();
        lector.leerCarpeta(ruta, actores, peliculas, 128);

        Graph grafo = new Graph();
        
        grafo.crearGrafo(peliculas);

        assertTrue(grafo.estanConectadosBoolean("Karra Elejalde", "Charlize Theron"));
        assertTrue(grafo.estanConectadosBoolean("Karra Elejalde", "Florentino Fernández"));
        assertTrue(grafo.estanConectadosBoolean("Florentino Fernández", "Charlize Theron"));
        assertTrue(grafo.estanConectadosBoolean("Charlize Theron", "Karra Elejalde"));
        assertFalse(grafo.estanConectadosBoolean("An So-yo", "Charlize Theron"));

        // El camino empieza y termina en los actores pedidos.
        ArrayList<String> camino = grafo.estanConectados("Karra Elejalde", "Charlize Theron");
        assertEquals("Karra Elejalde", camino.get(0));
        assertEquals("Charlize Theron", camino.get(camino.size() - 1));
        assertTrue(grafo.estanConectados("An So-yo", "Charlize Theron").isEmpty());
        
        grafo.print("Florentino Fernández","Karra Elejalde", 5);
        grafo.print("Karra Elejalde","Javier Bardem", 5);
        grafo.print("Florentino Fernández","Javier Bardem", 5);

        grafo.print("Javier Bardem","Charlize Theron", 5);
        grafo.print("Karra Elejalde","Charlize Theron", 5);

    }


}
