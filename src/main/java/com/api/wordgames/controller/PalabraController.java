package com.api.wordgames.controller;

import com.api.wordgames.response.JsonResponse;
import com.api.wordgames.services.PalabraServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class PalabraController {

    private PalabraServices palabraServices;

    /**
     * Obtenemos todas las palabras
     *
     * @return lista de palabras
     */
    @GetMapping("/palabras")
    public ResponseEntity<Object> getAllPalabras() throws Exception {
        List<String> listaPalabras = palabraServices.cargaPalabras();
        if(listaPalabras.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(listaPalabras);
        }
    }

    /**
     * Mostramos si existe una palabra en la lista
     *
     * @param palabraBuscada Palabra a buscar
     * @return Palabra encontrada
     */
    @GetMapping("/palabra")
    public ResponseEntity<Object> getPalabra(@RequestParam("busqueda") String palabraBuscada) throws Exception {
        List<String> listaPalabras = palabraServices.cargaPalabras();
        if(listaPalabras.contains(palabraBuscada.toLowerCase())){
            return ResponseEntity.ok(palabraBuscada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtenemos un número N de palabras aleatorias
     *
     * @param cantidad Cantidad de palabras a devolver si es mayor que el tamaño de la lista se devolverán todas
     * @return Lista de palabras
     */
    @GetMapping("/palabras/random/{cantidad}")
    public ResponseEntity<Object> getPalabrasRandom(@PathVariable Long cantidad) throws Exception {
        if(cantidad < 1){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.BAD_REQUEST,"La cantidad debe ser mayor que 0", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<String> listaPalabras = palabraServices.palabrasRandom(cantidad);
        return ResponseEntity.ok(listaPalabras);
    }

    /**
     * Obtenemos un número N de palabras aleatorias que empiecen por una cadena concreta
     *
     * @param cantidad Cantidad de palabras a devolver
     * @param cadenaBuscada Cadena de busqueda
     * @return Lista de palabras
     */
    @GetMapping("/palabra/contiene/{cadenaBuscada}")
    public ResponseEntity<Object> getPalabrasContiene(@PathVariable String cadenaBuscada, @RequestParam("cantidad") Long cantidad) throws IOException {
        if(cantidad < 1){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.BAD_REQUEST,"La cantidad debe ser mayor que 0", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<String> listaPalabras = palabraServices.palabrasContieneCadena(cadenaBuscada, cantidad);
        if(listaPalabras.isEmpty()){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.NOT_FOUND,"No se han encontrado palabras que contengan " + cadenaBuscada, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.ok(listaPalabras);
        }
    }

    /**
     * Obtenemos un número N de palabras aleatorias que empiecen por una cadena concreta
     *
     * @param cantidad Cantidad de palabras a devolver
     * @param cadenaBuscada Cadena de busqueda
     * @return Lista de palabras
     */
    @GetMapping("/palabra/empieza/{cadenaBuscada}")
    public ResponseEntity<Object> getPalabrasEmpieza(@PathVariable String cadenaBuscada, @RequestParam("cantidad") Long cantidad) throws IOException {
        if(cantidad < 1){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.BAD_REQUEST,"La cantidad debe ser mayor que 0", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<String> listaPalabras = palabraServices.palabrasEmpiezaCadena(cadenaBuscada, cantidad);
        if(listaPalabras.isEmpty()){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.NOT_FOUND,"No se han encontrado palabras que empiecen por " + cadenaBuscada, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.ok(listaPalabras);
        }
    }

    /**
     * Obtenemos un número N de palabras aleatorias que empiecen por una cadena concreta
     *
     * @param cantidad Cantidad de palabras a devolver
     * @param cadenaBuscada Cadena de busqueda
     * @return Lista de palabras
     */
    @GetMapping("/palabra/termina/{cadenaBuscada}")
    public ResponseEntity<Object> getPalabrasTermina(@PathVariable String cadenaBuscada, @RequestParam("cantidad") Long cantidad) throws IOException {
        if(cantidad < 1){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.BAD_REQUEST,"La cantidad debe ser mayor que 0", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<String> listaPalabras = palabraServices.palabrasTerminaCadena(cadenaBuscada, cantidad);
        if(listaPalabras.isEmpty()){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.NOT_FOUND,"No se han encontrado palabras que terminen por " + cadenaBuscada, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.ok(listaPalabras);
        }
    }

    /**
     * Obtenemos una palabra aleatoria de una longitud concreta
     *
     * @param longitud Longitud de la palabra
     * @return Palabra aleatoria
     */
    @GetMapping("/palabra/longitud/{longitud}")
    public ResponseEntity<Object> getPalabraLongitud(@PathVariable Long longitud) throws IOException {
        if(longitud < 1){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.BAD_REQUEST,"La longitud debe ser mayor que 0", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String palabra = palabraServices.palabraAleatoriaLongitud(longitud);
        if(palabra == null){
            JsonResponse<Object> response = new JsonResponse<>(HttpStatus.NOT_FOUND,"No se han encontrado palabras de longitud " + longitud, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.ok(palabra);
        }
    }
}
