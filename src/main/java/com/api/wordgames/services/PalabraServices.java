package com.api.wordgames.services;

import com.api.wordgames.model.Palabra;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PalabraServices {

    /**
     * Método que lee un archivo de texto y devuelve una lista de palabras
     *
     * @param archivo Nombre del archivo a leer
     * @return Lista de palabras
     * @throws Exception
     */
    public List<Palabra> lecturaArchivo(String archivo) throws IOException {
        List<Palabra> listaPalabras = new ArrayList<>();
        File archivoPalabras = ResourceUtils.getFile("classpath:" + archivo);
        BufferedReader reader = new BufferedReader(new FileReader(archivoPalabras));
        String linea;
        while ((linea = reader.readLine()) != null){
            Palabra palabra  = new Palabra(linea.trim());
            listaPalabras.add(palabra);
        }
        reader.close();
        return listaPalabras;
    }

    /**
     * Metodo que devuelve una lista de palabras sacadas del archivo
     *
     * @return Lista de palabras
     */
    public List<String> cargaPalabras() throws IOException {
        List<Palabra> archivoLeido = lecturaArchivo("palabras.txt");
        List<String> listaPalabras = new ArrayList<>();
        for(int i = 0; i < archivoLeido.size();i++){
            listaPalabras.add(archivoLeido.get(i).getValor());
        }
        return listaPalabras;
    }

    /**
     * Método que devuelve una lista de palabras aleatorias
     *
     * @param cantidad Cantidad de palabras a devolver
     * @return Lista de palabras
     */
    public List<String> palabrasRandom(Long cantidad) throws IOException {
        List<String> listaPalabras = cargaPalabras();
        List<String> listaPalabrasRandom = new ArrayList<>();
        if(cantidad > listaPalabras.size()){
            cantidad = (long) listaPalabras.size();
        }
        for(int i = 0; i < cantidad; i++){
            int random = (int) (Math.random() * listaPalabras.size());
            listaPalabrasRandom.add(listaPalabras.get(random));
            listaPalabras.remove(random);
        }
        return listaPalabrasRandom;
    }

    /**
     * Método que devuelve una lista de palabras que contienen una cadena
     *
     * @param contieneCadena Cadena a buscar
     * @param cantidad Cantidad de palabras a devolver
     * @return Lista de palabras
     */
    public List<String> palabrasContieneCadena(String contieneCadena, Long cantidad) throws IOException {
        List<String> listaPalabras = cargaPalabras();
        List<String> listaPalabrasContieneCadena = new ArrayList<>();
        for(int i = 0; i < listaPalabras.size(); i++){
            if(listaPalabras.get(i).contains(contieneCadena)){
                listaPalabrasContieneCadena.add(listaPalabras.get(i));
            }
            if(listaPalabrasContieneCadena.size() == cantidad){
                return listaPalabrasContieneCadena;
            }
        }
        return listaPalabrasContieneCadena;
    }

    /**
     * Método que devuelve una lista de palabras que empiezan por una cadena
     *
     * @param empiezaCadena Cadena a buscar
     * @param cantidad Cantidad de palabras a devolver
     * @return Lista de palabras
     */
    public List<String> palabrasEmpiezaCadena(String empiezaCadena, Long cantidad) throws IOException{
        List<String> listaPalabras = cargaPalabras();
        List<String> listaPalabrasEmpiezaCadena = new ArrayList<>();
        for(int i = 0; i < listaPalabras.size(); i++){
            if(listaPalabras.get(i).startsWith(empiezaCadena)){
                listaPalabrasEmpiezaCadena.add(listaPalabras.get(i));
            }
            if(listaPalabrasEmpiezaCadena.size() == cantidad){
                return listaPalabrasEmpiezaCadena;
            }
        }
        return listaPalabrasEmpiezaCadena;
    }

    /**
     * Método que devuelve una lista de palabras que terminan por una cadena
     *
     * @param terminaCadena Cadena a buscar
     * @param cantidad Cantidad de palabras a devolver
     * @return Lista de palabras
     */
    public List<String> palabrasTerminaCadena(String terminaCadena, Long cantidad) throws IOException{
        List<String> listaPalabras = cargaPalabras();
        List<String> listaPalabrasTerminaCadena = new ArrayList<>();
        for(int i = 0; i < listaPalabras.size(); i++){
            if(listaPalabras.get(i).endsWith(terminaCadena)){
                listaPalabrasTerminaCadena.add(listaPalabras.get(i));
            }
            if(listaPalabrasTerminaCadena.size() == cantidad){
                return listaPalabrasTerminaCadena;
            }
        }
        return listaPalabrasTerminaCadena;
    }

    /**
     * Metodo que devuelve una palabra aleatoria de un tamaño determinado
     *
     * @param tamanio Tamaño de la palabra
     * @return Palabra aleatoria
     */
    public String palabraAleatoriaLongitud(Long tamanio) throws IOException {
        List<String> listaPalabras = cargaPalabras();
        List<String> listaPalabrasLongitud = new ArrayList<>();
        for(int i = 0; i < listaPalabras.size(); i++){
            if(listaPalabras.get(i).length() == tamanio){
                listaPalabrasLongitud.add(listaPalabras.get(i));
            }
        }
        int random = (int) (Math.random() * listaPalabrasLongitud.size());
        return listaPalabrasLongitud.get(random);
    }
}
