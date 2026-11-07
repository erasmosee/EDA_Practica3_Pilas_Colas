package eda.practica3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;

public class Graph {

    HashMap<String, Integer> th;
    String[] keys;
    ArrayList<Integer>[] adjList;

    public void crearGrafo(OrderedDoubleLinkedList<Pelicula> peliculas) {
        // Post: crea el grafo desde la lista de peliculas
        // Los nodos son nombres de actores/actrices

        // Paso 1: llenar “th”
        th = new HashMap<>();

        Iterator<Pelicula> itPeliculas = peliculas.iterator();
        while (itPeliculas.hasNext()) {
            Pelicula pelicula = itPeliculas.next();

            Iterator<Actor> itActores = pelicula.getActorLinkedList().iterator();
            while (itActores.hasNext()) {
                Actor actor = itActores.next();
                String id = actor.getId();

                if (!th.containsKey(id)) {
                    th.put(id, th.size());
                }
            }
        }

        // Paso 2: llenar “keys”
        keys = new String[th.size()];
        for (String k : th.keySet())
            keys[th.get(k)] = k;

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