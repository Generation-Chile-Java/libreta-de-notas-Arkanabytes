import java.util.*;
// Clase principal del programa
public class Main {
    public static void main(String[] args) {
        // Crea una instancia para manejar los estudiantes
        GestorEstudiantes gestor = new GestorEstudiantes();
        // Crea una instancia para manejar la entrada del usuario
        EntradaUsuario entrada = new EntradaUsuario();

        // Solicita cantidad de alumnos y notas
        System.out.println("----------------------------------------------------------------------");
        System.out.println("                Bienvenidos a la Libreta de notas Generation                 ");
        System.out.println("----------------------------------------------------------------------");
        int cantidad = entrada.pedirEntero("\nIngrese la cantidad de alumnos: ", 1, Integer.MAX_VALUE);
        int cantidadNotas = entrada.pedirEntero("Ingrese la cantidad de notas por alumno: ", 1, Integer.MAX_VALUE);

        // Itera sobre cada alumno
        for (int i = 0; i < cantidad; i++) {
            String nombre = entrada.pedirTexto("\nNombre del alumno " + (i + 1) + ": ");
            Estudiante estudiante = new Estudiante(nombre);

            // Agrega las notas del estudiante
            for (int j = 0; j < cantidadNotas; j++) {
                double nota = entrada.pedirDecimal("Ingrese nota del 1.0 al 7.0: #" + (j + 1) + ": ", 0, 7.0);
                estudiante.agregarNota(nota);
            }
            // Añade el estudiante al gestor
            gestor.agregarEstudiante(estudiante);
        }
        // Muestra el menú principal
        Menu menu = new Menu(gestor, entrada);
        menu.mostrar();
    }
}

// Clase que representa un estudiante y sus notas
class Estudiante {
    private String nombre;
    private ArrayList<Double> notas;
    public Estudiante(String nombre) {
        this.nombre = nombre;
        this.notas = new ArrayList<>();
    }
    public void agregarNota(double nota) {
        notas.add(nota);
    }
    public String getNombre() {
        return nombre;
    }
    public ArrayList<Double> getNotas() {
        return notas;
    }
}

// Clase que gestiona una colección de estudiantes
class GestorEstudiantes {
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();
    public void agregarEstudiante(Estudiante alumnos) {
        estudiantes.put(alumnos.getNombre(), alumnos);
    }
    public Estudiante buscarEstudiante(String nombre) {
        return estudiantes.get(nombre);
    }
    public Collection<Estudiante> obtenerTodos() {
        return estudiantes.values();
    }
    // Calcula el promedio de notas del curso completo
    public double promedioCurso() {
        double suma = 0;
        int cantidad = 0;
        for (Estudiante alumnos : estudiantes.values()) {
            for (double nota : alumnos.getNotas()) {
                suma += nota;
                cantidad++;
            }
        }
        return cantidad == 0 ? 0 : suma / cantidad;
    }
}

// Clase utilitaria para evaluar notas
class  LibretaDeNotas {
    public static double promedio(ArrayList<Double> notas) {
        return notas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
    public static double maximo(ArrayList<Double> notas) {
        return notas.stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }
    public static double minimo(ArrayList<Double> notas) {
        return notas.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }
    public static boolean esAprobatoria(double nota, double minimoAprobacion) {
        return nota >= minimoAprobacion;
    }
    public static String compararNotaConPromedio(double nota, double promedioCurso) {
        if (nota > promedioCurso) return "Sobre el promedio";
        else if (nota < promedioCurso) return "Debajo del promedio";
        else return "Igual al promedio";
    }
}

// Clase que maneja todas las entradas del usuario con validaciones
class EntradaUsuario {
    private Scanner scanner = new Scanner(System.in);
    public int pedirEntero(String mensaje, int min, int max) {
        int valor;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) return valor;
            } catch (Exception ignored) {}
            System.out.println("Entrada inválida. Intente nuevamente.");
        }
    }

    public double pedirDecimal(String mensaje, double min, double max) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(scanner.nextLine());
                if (valor >= min && valor <= max) return valor;
            } catch (Exception ignored) {}
            System.out.println("Entrada inválida. Intente nuevamente.");
        }
    }
    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}

// Clase que representa el menú interactivo con opciones para el usuario
class Menu {
    private GestorEstudiantes gestor;
    private EntradaUsuario entrada;
    private final double APROBACION = 4.0;

    public Menu(GestorEstudiantes gestor, EntradaUsuario entrada) {
        this.gestor = gestor;
        this.entrada = entrada;
    }

    // Muestra el menú principal y ejecuta opciones
    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n---------------------------------------------------");
            System.out.println("                  MENÚ PRINCIPAL                   ");
            System.out.println("---------------------------------------------------");
            System.out.println("1.-Mostrar Promedio, Máxima y Mínima por Estudiante");
            System.out.println("2.-Verificar si con la nota aprobo");
            System.out.println("3.-Comparar una nota con el Promedio del Curso");
            System.out.println("0.-Salir");
            opcion = entrada.pedirEntero("Seleccione opción: ", 0, 3);

            switch (opcion) {
                case 1 -> mostrarResumenPorEstudiante();
                case 2 -> verificarAprobacion();
                case 3 -> compararConPromedioCurso();
                case 0 -> System.out.println("¡Hasta pronto!");
            }
        } while (opcion != 0);
    }

    // Muestra para cada estudiante su promedio, nota máxima y mínima
    private void mostrarResumenPorEstudiante() {
        for (Estudiante alumnos : gestor.obtenerTodos()) {
            System.out.printf("%s => Promedio: %.2f | Máx: %.2f | Mín: %.2f\n",
                    alumnos.getNombre(),
                    LibretaDeNotas.promedio(alumnos.getNotas()),
                    LibretaDeNotas.maximo(alumnos.getNotas()),
                    LibretaDeNotas.minimo(alumnos.getNotas()));
        }
    }

    // Permite ingresar una nota y verificar si es aprobatoria
    private void verificarAprobacion() {
        String nombre = entrada.pedirTexto("Nombre del estudiante: ");
        Estudiante alumnos = gestor.buscarEstudiante(nombre);
        if (alumnos == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }
        double nota = entrada.pedirDecimal("Nota a verificar: ", 0, 7);
        boolean aprobada = LibretaDeNotas.esAprobatoria(nota, APROBACION);
        System.out.println(aprobada ? "Aprobaste" : "Reprobaste");
    }

    // Compara una nota ingresada con el promedio general del curso
    private void compararConPromedioCurso() {
        String nombre = entrada.pedirTexto("Nombre del estudiante: ");
        Estudiante alumnos = gestor.buscarEstudiante(nombre);
        if (alumnos == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }
        double nota = entrada.pedirDecimal("Nota a comparar: ", 0, 7);
        double promedio = gestor.promedioCurso();
        System.out.printf("Promedio del curso: %.2f\n", promedio);
        System.out.println("Resultado: " + LibretaDeNotas.compararNotaConPromedio(nota, promedio));
    }
}