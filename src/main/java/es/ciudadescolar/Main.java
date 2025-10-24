package es.ciudadescolar;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.util.JsonManager;
import java.util.*;
import es.ciudadescolar.instituto.*;

public class Main {
    
    public static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static File ficheroJsonAlumnos = new File("alumnos.json");
    public static File out = new File("out_alumnos.json");
    public static File out2 = new File("out_alumnos2.json");

    public static void main(String[] args) {
        List<Alumno> alumnosJson = JsonManager.parsearJsonAlumnos(ficheroJsonAlumnos);
        
        if (!alumnosJson.isEmpty()){
            JsonManager.crearJsonAlumnos(alumnosJson, out,false);
            JsonManager.crearJsonAlumnos(alumnosJson, out2,true);
        }
        else{
            LOG.warn("La lista esta vacia, parseada de este fichero: "+ficheroJsonAlumnos.getAbsolutePath());
        }
    }
}