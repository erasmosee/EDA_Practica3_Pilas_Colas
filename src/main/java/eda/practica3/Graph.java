package eda.practica3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eda.practica3.procesador.Actor;
import eda.practica3.procesador.OrderedDoubleLinkedList;
import eda.practica3.procesador.Pelicula;

/**
 * Grafo no dirigido de actores/actrices: dos actores estan unidos si han
 * participado juntos en alguna pelicula.
 */
public class Graph {

    // Nombre del actor -> identificador numerico.
    HashMap<String, Integer> th;
    // Identificador numerico -> nombre del actor.
    String[] keys;
    // Para cada identificador, los identificadores de los actores con los que
    // ha compartido alguna pelicula (sin repetidos).
    ArrayList<Integer>[] adjList;

    /**
     * <b>O(sum(k_p^2) * g)</b>: k_p actores de la pelicula p y g el grado
     * maximo del grafo (coste de contains).
     *
     * Pre: peliculas no es null y cada pelicula tiene su lista de actores.
     * Post: th, keys y adjList describen el grafo; adjList[i] no tiene
     * repetidos ni contiene a i, y la relacion es simetrica.
     *
     * @param peliculas peliculas de las que se extraen las relaciones
     */
    public void crearGrafo(OrderedDoubleLinkedList<Pelicula> peliculas) {

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

        // Paso 3: llenar “adjList” con una lista vacia por actor.
        adjList = new ArrayList[th.size()];
        for (int i = 0; i < adjList.length; i++) {
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
                            adjList[a].add(b);
                            adjList[b].add(a);
                        }
                    }
                }
            }
        }
    }

    /**
     * <b>O(V + E)</b>: muestra cada actor con los actores con los que ha
     * compartido pelicula.
     */
    public void print() {
        // Mostramos cada actor y con quien esta conectado.
        for (int i = 0; i < adjList.length; i++) {
            System.out.print("Element: " + i + " " + keys[i] + " --> ");
            for (int k : adjList[i])
                System.out.print(keys[k] + " ### ");

            System.out.println();
        }
    }

    /**
     * <b>O(V + E)</b>: muestra el numero de relaciones que separan a nombre de
     * destino si es menor o igual que profundidad.
     *
     * Pre: el grafo esta creado.
     * Post: muestra la distancia (0 si son el mismo actor); si no se alcanza
     * con esa profundidad o algun actor no existe, lo indica.
     */
    public void print(String nombre, String destino, int profundidad) {
        Integer inicio = th.get(nombre);
        Integer fin = th.get(destino);

        if (inicio == null || fin == null) {
            System.out.println("No se encuentra el actor: " + (inicio == null ? nombre : destino));
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
            if (actual == fin) {
                System.out.println(nombre + " esta a una distancia " + distancia[actual] + " de " + destino);
                return;
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
        System.out.println(nombre + " no esta a una distancia <= " + profundidad + " de " + destino);
    }

    /**
     * <b>O(1)</b> en media: identificador numerico de un actor.
     *
     * @throws IllegalArgumentException si el actor no esta en el grafo
     */
    private int identificador(String nombre) {
        Integer id = th.get(nombre);
        if (id == null) {
            throw new IllegalArgumentException("No se encuentra el actor: " + nombre);
        }
        return id;
    }

    /**
     * <b>O(V + E)</b> (parte opcional): recorrido en anchura que devuelve la
     * cadena de relaciones entre a1 y a2.
     *
     * Pre: el grafo esta creado.
     * Post: lista a1, ..., a2 de longitud minima en la que cada par consecutivo
     * ha compartido pelicula; vacia si no hay conexion; un solo elemento si
     * a1 es a2.
     *
     * @throws IllegalArgumentException si algun actor no esta en el grafo
     */
    public ArrayList<String> estanConectados(String a1, String a2) {
        // Usamos una cola para ir revisando los actores pendientes.
        Queue<Integer> porExaminar = new LinkedList<>();
        int[] padre = new int[th.size()];
        // Buscamos los numeros que tienen los dos actores.
        int pos1 = identificador(a1);
        int pos2 = identificador(a2);
        ArrayList<String> camino = new ArrayList<>();
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
                    padre[vecino] = actual;
                    break;
                }

                if (!examinados[vecino]) {
                    // Lo marcamos para no volver a revisarlo y lo ponemos en la cola.
                    examinados[vecino] = true;
                    porExaminar.add(vecino);
                    padre[vecino] = actual;
                }
            }
        }
        if (enc) {
            // Reconstruimos el camino desde el destino siguiendo padre.
            int paso = pos2;
            camino.add(keys[paso]);
            while (paso != pos1) {
                paso = padre[paso];
                camino.add(keys[paso]);
            }
            Collections.reverse(camino);
        }
        return camino;

    }

    /**
     * <b>O(V + E)</b>: recorrido en anchura que indica si hay una cadena de
     * relaciones entre a1 y a2.
     *
     * Pre: el grafo esta creado.
     * Post: true si y solo si hay camino; un actor esta conectado consigo
     * mismo.
     *
     * @throws IllegalArgumentException si algun actor no esta en el grafo
     */
    public boolean estanConectadosBoolean(String a1, String a2) {
        // Usamos una cola para ir revisando los actores pendientes.
        Queue<Integer> porExaminar = new LinkedList<>();

        // Buscamos los numeros que tienen los dos actores.
        int pos1 = identificador(a1);
        int pos2 = identificador(a2);
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