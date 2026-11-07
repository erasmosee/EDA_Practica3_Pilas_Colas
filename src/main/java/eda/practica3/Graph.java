package eda.practica3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;

public class Graph {

    HashMap<String, Integer> th;
    HashMap<String, Integer> thId;
    String[] keys;
    String[] keysId;
    ArrayList<Integer>[] adjList;

    public void crearGrafo(OrderedDoubleLinkedList<Pelicula> peliculas) {
        // Post: crea el grafo desde la lista de peliculas
        // Los nodos son nombres de actores/actrices

        // Paso 1: llenar “th”
        th = new HashMap<>(); // No se porque le llaman th pero vamos a duplicarlo porque para ir probando
                              // todavia no se que es mejor si darles nuevos "id" del 0 a n o usar sus propios
                              // id.
        thId = new HashMap<>(); // No se porque le llaman th pero vamos a duplicarlo porque para ir probando
                                // todavia no se que es mejor si darles nuevos "id" del 0 a n o usar sus propios
                                // id.

        for (Pelicula pelicula : peliculas) {
            for (Actor actor : pelicula.getActorLinkedList()) {

                Integer idActor = Integer.valueOf(actor.getId().replaceAll("\\D", "")); // Limpiamos la cadena por si
                                                                                        // nos viene con la Q
                String nombreActor = actor.getName();

                if (!th.containsKey(nombreActor)) {
                    th.put(nombreActor, th.size()); // podemos guardar los actores con valores del 0 a n
                    thId.put(nombreActor, idActor); // podemos guardar los actores con su id en formato numerico
                }
            }
        }

        // Paso 2: llenar “keys”
        keys = new String[th.size()];
        for (String k : th.keySet()) {
            keys[th.get(k)] = k;
        }

        keysId = new String[thId.size()];
        for (String k : thId.keySet()) {
            keysId[th.get(k)] = k;
        }
        // Paso 3: llenar “adjList”
        // COMPLETAR CÓDIGO
    }

    public void print() {
        for (int i = 0; i < adjList.length; i++) {
            System.out.print("Element: " + i + " " + keys[i] + " --> ");
            for (int k : adjList[i])
                System.out.print(keys[k] + " ### ");

            System.out.println();
        }
    }

    public boolean estanConectados(String a1, String a2) {
        Queue<Integer> porExaminar = new LinkedList<Integer>();

        int pos1 = th.get(a1);
        int pos2 = th.get(a2);
        boolean enc = false;
        boolean[] examinados = new boolean[th.size()];

        // COMPLETAR CÓDIGO

        return enc;

    }
}