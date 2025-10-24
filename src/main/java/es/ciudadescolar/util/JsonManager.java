package es.ciudadescolar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.ciudadescolar.instituto.*;
import java.io.File;
import java.io.IOException;

public class JsonManager {
    private static final Logger LOG = LoggerFactory.getLogger(JsonManager.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<Alumno> parsearJsonAlumnos(File ficheroJson) {
        List<Alumno> listaAlumnos = new ArrayList<>();

        validarFichero(ficheroJson);

        JsonNode root = null;
        JsonNode alumnos = null;
        Alumno a = null;
        try {
            root = mapper.readTree(ficheroJson);
            if (root.isObject()) {
                LOG.info("centro: " + root.get("centro").asText());
                LOG.info("codigo: " + root.get("codigo").asInt());
                LOG.info("codigo: " + root.get("curso").asInt());
                alumnos = root.get("alumnos");

                if (alumnos.isArray()) {
                    for (int i = 0; i < alumnos.size(); i++) {
                        a = new Alumno(
                                alumnos.get(i).get("expediente").asText(),
                                alumnos.get(i).get("nombre").asText(),
                                alumnos.get(i).get("edad").asInt());
                        listaAlumnos.add(a);

                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Hubo un error en el parseado del teclado" + e.getMessage());
        }

        return listaAlumnos;
    }

    public static void validarFichero(File ficheroJson) {
        if (!ficheroJson.exists()) {
            LOG.warn("Fichero json no existe: " + ficheroJson.getAbsolutePath());
        }

        if (!ficheroJson.canRead()) {
            LOG.warn("El fichero no puede ser leido por falta de privilegios");
        }
    }

    public static void crearJsonAlumnos(List<Alumno> a, File out, boolean serialize) {
        // Opcion 1: Creacion dinamica del fichero
        ArrayNode arrayAlumnos = mapper.createArrayNode();
        ObjectNode nodoAlumno = null;
        if (!serialize) {
            if (a != null && !a.isEmpty()) {
                for (Alumno i : a) {
                    nodoAlumno = mapper.createObjectNode();
                    nodoAlumno.put("nombre", i.getNombre());
                    nodoAlumno.put("edad", i.getEdad());
                    nodoAlumno.put("expediente", i.getExpediente());
                    arrayAlumnos.add(nodoAlumno);
                }
                try {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(out, arrayAlumnos);
                } catch (IOException e) {
                    LOG.error("Error al intentar escribir el fichero" + e.getMessage());
                }
            }
        }
        // Opcion 2: Serializacion jackson
        else {
              try {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(out, a);
                } catch (IOException e) {
                    LOG.error("Error al intentar escribir el fichero" + e.getMessage());
            }
        }

    }
}
