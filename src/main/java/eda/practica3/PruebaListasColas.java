package eda.practica3;

import java.io.IOException;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.LectorPeliculasActores;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;
import eda.practica3.procesador.Utils;

public class PruebaListasColas {

    public static void main(String[] args) throws IOException {
        Utils.traza("programa", "iniciando programa");
        long tiempoPrograma = Utils.iniciarCronometro();
        OrderedDoubleLinkedList<Actor> actores = new OrderedDoubleLinkedList<>();
        OrderedDoubleLinkedList<Pelicula> peliculas = new OrderedDoubleLinkedList<>();

        String ruta = Utils.cargarPropiedad("datos.ruta");
        if (args.length > 0) {
            ruta = args[0];
        }

        ejecutarPrueba(ruta, actores, peliculas);
        Utils.mostrarCronometro("programa", tiempoPrograma);
        Utils.traza("programa", "fin programa");
    }

    private static void ejecutarPrueba(String ruta, OrderedDoubleLinkedList<Actor> actores,
            OrderedDoubleLinkedList<Pelicula> peliculas) throws IOException {

        int tamanioLote = 128;
        LectorPeliculasActores lector = new LectorPeliculasActores();
        lector.leerCarpeta(ruta, actores, peliculas, tamanioLote);

    }
}
