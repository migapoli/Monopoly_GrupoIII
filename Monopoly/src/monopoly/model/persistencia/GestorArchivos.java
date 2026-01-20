package monopoly.model.persistencia;

import monopoly.model.tablero.Partida;
import monopoly.model.jugador.IJugador;
import monopoly.model.jugador.Jugador;
import monopoly.model.casilla.Propiedad;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de archivos para guardar y cargar partidas de Monopoly.
 * Implementa serialización en formato de texto para mayor legibilidad.
 */
public class GestorArchivos {

    private static final String EXTENSION = ".monopoly";
    private static final String DIRECTORIO_GUARDADOS = "saves";
    private static final String SEPARADOR = "|||";

    /**
     * Guarda el estado actual de la partida en un archivo.
     * 
     * @param partida       La partida a guardar
     * @param nombreArchivo Nombre del archivo (sin extensión)
     * @return true si se guardó correctamente
     */
    public static boolean guardar(Partida partida, String nombreArchivo) {
        try {
            // Crear directorio si no existe
            Path directorioPath = Paths.get(DIRECTORIO_GUARDADOS);
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            String rutaCompleta = DIRECTORIO_GUARDADOS + File.separator + nombreArchivo + EXTENSION;

            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
                // Cabecera
                writer.println("# Partida de Monopoly guardada");
                writer.println(
                        "# Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                writer.println();

                // Estado de la partida
                writer.println("[PARTIDA]");
                writer.println("turnoActual=" + partida.getTurnoActual());
                writer.println("juegoTerminado=" + partida.isJuegoTerminado());
                writer.println();

                // Jugadores
                writer.println("[JUGADORES]");
                writer.println("cantidad=" + partida.getJugadores().size());

                for (int i = 0; i < partida.getJugadores().size(); i++) {
                    IJugador jugador = partida.getJugadores().get(i);
                    writer.println("jugador" + i + ".nombre=" + jugador.getNombre());
                    writer.println("jugador" + i + ".dinero=" + jugador.getDinero());
                    writer.println("jugador" + i + ".posicion=" + jugador.getPosicion());
                    writer.println("jugador" + i + ".color=" + jugador.getColor().getRGB());
                    writer.println("jugador" + i + ".enCarcel=" + jugador.isEnCarcel());
                    writer.println("jugador" + i + ".turnosEnCarcel=" + jugador.getTurnosEnCarcel());

                    // Propiedades del jugador
                    List<Propiedad> propiedades = jugador.getPropiedades();
                    StringBuilder propiedadesStr = new StringBuilder();
                    for (int j = 0; j < propiedades.size(); j++) {
                        if (j > 0)
                            propiedadesStr.append(",");
                        propiedadesStr.append(propiedades.get(j).getPosicion());
                    }
                    writer.println("jugador" + i + ".propiedades=" + propiedadesStr.toString());
                }
                writer.println();

                // Propiedades (estado de casas/hoteles)
                writer.println("[PROPIEDADES]");
                for (var casilla : partida.getTablero().getCasillas()) {
                    if (casilla instanceof Propiedad) {
                        Propiedad prop = (Propiedad) casilla;
                        if (prop.tienePropietario() || prop.getCasas() > 0 || prop.tieneHotel()) {
                            writer.println("propiedad" + prop.getPosicion() + ".casas=" + prop.getCasas());
                            writer.println("propiedad" + prop.getPosicion() + ".hotel=" + prop.tieneHotel());
                            writer.println("propiedad" + prop.getPosicion() + ".propietario=" +
                                    (prop.getPropietario() != null ? prop.getPropietario().getNombre() : "null"));
                        }
                    }
                }
                writer.println();

                // Historial
                writer.println("[HISTORIAL]");
                for (String evento : partida.getHistorial()) {
                    writer.println("evento=" + evento.replace("\n", "\\n"));
                }

                writer.flush();
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga una partida desde un archivo.
     * 
     * @param nombreArchivo Nombre del archivo (sin extensión)
     * @return La partida cargada, o null si hubo error
     */
    public static DatosPartidaGuardada cargar(String nombreArchivo) {
        String rutaCompleta = DIRECTORIO_GUARDADOS + File.separator + nombreArchivo + EXTENSION;

        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaCompleta));
            DatosPartidaGuardada datos = new DatosPartidaGuardada();

            String seccionActual = "";

            for (String linea : lineas) {
                linea = linea.trim();

                // Ignorar comentarios y líneas vacías
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Detectar sección
                if (linea.startsWith("[") && linea.endsWith("]")) {
                    seccionActual = linea.substring(1, linea.length() - 1);
                    continue;
                }

                // Parsear línea clave=valor
                int separadorPos = linea.indexOf("=");
                if (separadorPos == -1)
                    continue;

                String clave = linea.substring(0, separadorPos);
                String valor = linea.substring(separadorPos + 1);

                switch (seccionActual) {
                    case "PARTIDA":
                        if (clave.equals("turnoActual")) {
                            datos.turnoActual = Integer.parseInt(valor);
                        } else if (clave.equals("juegoTerminado")) {
                            datos.juegoTerminado = Boolean.parseBoolean(valor);
                        }
                        break;

                    case "JUGADORES":
                        if (clave.equals("cantidad")) {
                            datos.cantidadJugadores = Integer.parseInt(valor);
                            datos.datosJugadores = new DatosJugador[datos.cantidadJugadores];
                            for (int i = 0; i < datos.cantidadJugadores; i++) {
                                datos.datosJugadores[i] = new DatosJugador();
                            }
                        } else if (clave.contains(".")) {
                            String[] partes = clave.split("\\.");
                            int indice = Integer.parseInt(partes[0].replace("jugador", ""));
                            String campo = partes[1];

                            if (indice < datos.cantidadJugadores) {
                                switch (campo) {
                                    case "nombre":
                                        datos.datosJugadores[indice].nombre = valor;
                                        break;
                                    case "dinero":
                                        datos.datosJugadores[indice].dinero = Integer.parseInt(valor);
                                        break;
                                    case "posicion":
                                        datos.datosJugadores[indice].posicion = Integer.parseInt(valor);
                                        break;
                                    case "color":
                                        datos.datosJugadores[indice].colorRGB = Integer.parseInt(valor);
                                        break;
                                    case "enCarcel":
                                        datos.datosJugadores[indice].enCarcel = Boolean.parseBoolean(valor);
                                        break;
                                    case "turnosEnCarcel":
                                        datos.datosJugadores[indice].turnosEnCarcel = Integer.parseInt(valor);
                                        break;
                                    case "propiedades":
                                        if (!valor.isEmpty()) {
                                            String[] propIds = valor.split(",");
                                            datos.datosJugadores[indice].propiedadesPosiciones = new int[propIds.length];
                                            for (int j = 0; j < propIds.length; j++) {
                                                datos.datosJugadores[indice].propiedadesPosiciones[j] = Integer
                                                        .parseInt(propIds[j]);
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                        break;

                    case "PROPIEDADES":
                        if (clave.contains(".")) {
                            String[] partes = clave.split("\\.");
                            int posicion = Integer.parseInt(partes[0].replace("propiedad", ""));
                            String campo = partes[1];

                            DatosPropiedad datoProp = datos.propiedades.stream()
                                    .filter(p -> p.posicion == posicion)
                                    .findFirst()
                                    .orElseGet(() -> {
                                        DatosPropiedad nuevo = new DatosPropiedad();
                                        nuevo.posicion = posicion;
                                        datos.propiedades.add(nuevo);
                                        return nuevo;
                                    });

                            switch (campo) {
                                case "casas":
                                    datoProp.casas = Integer.parseInt(valor);
                                    break;
                                case "hotel":
                                    datoProp.tieneHotel = Boolean.parseBoolean(valor);
                                    break;
                                case "propietario":
                                    datoProp.nombrePropietario = valor.equals("null") ? null : valor;
                                    break;
                            }
                        }
                        break;

                    case "HISTORIAL":
                        if (clave.equals("evento")) {
                            datos.historial.add(valor.replace("\\n", "\n"));
                        }
                        break;
                }
            }

            return datos;

        } catch (IOException e) {
            System.err.println("Error al cargar la partida: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista los archivos de partidas guardadas.
     * 
     * @return Lista de nombres de archivos (sin extensión)
     */
    public static List<String> listarPartidasGuardadas() {
        List<String> partidas = new ArrayList<>();

        try {
            Path directorioPath = Paths.get(DIRECTORIO_GUARDADOS);
            if (!Files.exists(directorioPath)) {
                return partidas;
            }

            Files.list(directorioPath)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        String nombre = path.getFileName().toString();
                        nombre = nombre.substring(0, nombre.length() - EXTENSION.length());
                        partidas.add(nombre);
                    });

        } catch (IOException e) {
            System.err.println("Error al listar partidas: " + e.getMessage());
        }

        return partidas;
    }

    /**
     * Elimina una partida guardada.
     * 
     * @param nombreArchivo Nombre del archivo (sin extensión)
     * @return true si se eliminó correctamente
     */
    public static boolean eliminar(String nombreArchivo) {
        try {
            String rutaCompleta = DIRECTORIO_GUARDADOS + File.separator + nombreArchivo + EXTENSION;
            return Files.deleteIfExists(Paths.get(rutaCompleta));
        } catch (IOException e) {
            System.err.println("Error al eliminar la partida: " + e.getMessage());
            return false;
        }
    }

    // ===== CLASES DE DATOS AUXILIARES =====

    public static class DatosPartidaGuardada {
        public int turnoActual;
        public boolean juegoTerminado;
        public int cantidadJugadores;
        public DatosJugador[] datosJugadores;
        public List<DatosPropiedad> propiedades = new ArrayList<>();
        public List<String> historial = new ArrayList<>();
    }

    public static class DatosJugador {
        public String nombre;
        public int dinero;
        public int posicion;
        public int colorRGB;
        public boolean enCarcel;
        public int turnosEnCarcel;
        public int[] propiedadesPosiciones;
    }

    public static class DatosPropiedad {
        public int posicion;
        public int casas;
        public boolean tieneHotel;
        public String nombrePropietario;
    }
}
