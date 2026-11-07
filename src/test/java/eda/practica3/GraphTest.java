package eda.practica3;

import java.io.IOException;

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

        assertTrue(grafo.estanConectados("Dani Rovira", "Charlize Theron"));
        assertTrue(grafo.estanConectados("Dani Rovira", "Florentino Fernández"));
        assertTrue(grafo.estanConectados("Florentino Fernández", "Charlize Theron"));
        assertTrue(grafo.estanConectados("Florentino Fernández", "Charlize Theron"));
        assertFalse(grafo.estanConectados("An So-yo", "Charlize Theron"));
        
        grafo.print("Dani Rovira","Karra Elejalde", 5);
        grafo.print("Karra Elejalde","Javier Bardem", 5);
        grafo.print("Dani Rovira","Javier Bardem", 5);

        grafo.print("Javier Bardem","Charlize Theron", 5);
        grafo.print("Dani Rovira","Charlize Theron", 5);

    }


}
