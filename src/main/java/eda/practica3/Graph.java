package eda.practica3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;

public class Graph {

    // Aqui guardamos el nombre del actor y el numero que le hemos dado.
    HashMap<String, Integer> th;
    // Aqui hacemos lo contrario, con el numero buscamos el nombre.
    String[] keys;
    // Para cada actor guardamos los numeros de los actores con los que ha hecho
    // alguna pelicula.
    // Es como la practica de los muelles que hicimos en clase, cada muelle es un
    // actor y cada contenedor son id's de actores que estan adyacentes.
    // Es decir tenemos actores como muelles y actores como contenedores
    ArrayList<Integer>[] adjList;

    public void crearGrafo(OrderedDoubleLinkedList<Pelicula> peliculas) {
        // Post: crea el grafo desde la lista de peliculas
        // Los nodos son nombres de actores/actrices

        // Paso 1: llenar “th”
        // Si sale en mas peliculas no le damos otro numero.
        th = new HashMap<>();

        // Recorremos todo n*m por cada pelicula todos los actores
        for (Pelicula pelicula : peliculas) {
            for (Actor actor : pelicula.getActorLinkedList()) {
                String nombreActor = actor.getName();

                if (!th.containsKey(nombreActor)) {
                    // El numero nuevo es la cantidad de actores que ya tenemos.
                    th.put(nombreActor, th.size());
                }
            }
        }

        // Paso 2: llenar “keys”
        keys = new String[th.size()];
        for (String k : th.keySet()) {
            keys[th.get(k)] = k;
        }

        // Paso 3: llenar “adjList”
        // Os acordais cuando arriba he hablado de muelles, pues aqui estamos indicando
        // el numero de actores(cuando funcionan como muelles)
        adjList = new ArrayList[th.size()];
        for (int i = 0; i < adjList.length; i++) {
            // Ahora al actor(muelle) creamos lo que va a ser el muelle en si, donde van a
            // alojarse los actores(contenedores)
            adjList[i] = new ArrayList<>();
        }

        // Ahora conectamos a los actores que salen en la misma pelicula.
        for (Pelicula pelicula : peliculas) {
            for (Actor actorA : pelicula.getActorLinkedList()) {
                // Guardamos el numero de este actor.
                int a = th.get(actorA.getName());

                for (Actor actorB : pelicula.getActorLinkedList()) {
                    // Lo comparamos con los otros actores de la pelicula.
                    int b = th.get(actorB.getName());

                    // Como los numeros son distintos, uno sera menor que el otro.
                    // Asi no repetimos la misma pareja.
                    if (a < b) {
                        if (!adjList[a].contains(b)) {
                            // Si no se conocian los presentamos
                            adjList[a].add(b);
                            adjList[b].add(a);
                        }
                    }
                }
            }
        }
    }

    public void print() {
        // Mostramos cada actor y con quien esta conectado.
        for (int i = 0; i < adjList.length; i++) {
            System.out.print("Element: " + i + " " + keys[i] + " --> ");
            for (int k : adjList[i])
                System.out.print(keys[k] + " ### ");

            System.out.println();
        }
    }

    public void print(String nombre, String destino, int profundidad) {
        Integer inicio = th.get(nombre);

        if (inicio == null) {
            System.out.println("No se encuentra el actor: " + nombre);
            return;
        }
        if (profundidad < 0) {
            profundidad = 0;
        }

        boolean[] examinados = new boolean[th.size()];
        int[] distancia = new int[th.size()];
        Queue<Integer> porExaminar = new LinkedList<>();

        examinados[inicio] = true;
        porExaminar.add(inicio);

        while (!porExaminar.isEmpty()) {
            int actual = porExaminar.remove();
            if (destino.equals(keys[actual])) {
                System.out.println(nombre + " esta a una distancia " + (distancia[actual]-1) + " de " + keys[actual]);
            }
            if (distancia[actual] < profundidad) {
                for (int vecino : adjList[actual]) {
                    if (!examinados[vecino]) {
                        examinados[vecino] = true;
                        distancia[vecino] = distancia[actual] + 1;
                        porExaminar.add(vecino);
                    }
                }
            }
        }
    }

    public boolean estanConectados(String a1, String a2) {
        // Usamos una cola para ir revisando los actores pendientes.
        Queue<Integer> porExaminar = new LinkedList<>();

        // Buscamos los numeros que tienen los dos actores.
        int pos1 = th.get(a1);
        int pos2 = th.get(a2);
        boolean enc;
        // Aqui marcamos los actores que ya hemos revisado.
        boolean[] examinados = new boolean[th.size()];

        // Si es el mismo actor, ya sabemos que estan conectados.
        enc = pos1 == pos2;
        // Empezamos por el primer actor.
        examinados[pos1] = true;
        porExaminar.add(pos1);

        // Seguimos mientras queden actores en la cola y no encontremos al otro.
        while (!porExaminar.isEmpty() && !enc) {
            int actual = porExaminar.remove();

            // Miramos con que actores ha compartido pelicula este.
            for (int vecino : adjList[actual]) {
                if (vecino == pos2) {
                    enc = true;
                    break;
                }

                if (!examinados[vecino]) {
                    // Lo marcamos para no volver a revisarlo y lo ponemos en la cola.
                    examinados[vecino] = true;
                    porExaminar.add(vecino);
                }
            }
        }

        // Devolvemos si hemos encontrado un camino entre los dos.
        return enc;

    }
}