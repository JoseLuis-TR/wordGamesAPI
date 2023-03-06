# 🔡 WordGames-API 🔡

## 📝 Descripción

Este proyecto consiste en una API para una aplicación de juegos de palabras.
La API está desarrollada usando Spring Boot y haciendo uso de MYSQL como base de datos.

## 💼 Base de datos

Se adjunta en la carpeta resources el script con el estado actual de la base de datos.

## 👨‍🔬 Pruebas de requests

Se adjunta en la carpeta resources el archivo JSON con la exportación de las pruebas de requests de Insomnia.

## ⛓ Ramas

``Esta sección se irá actualizando con la adición de nuevas ramas.``

- **master**: Rama inicial del proyecto.
    - Preparación del proyecto y conexión con la base de datos.
- **1-EntidadesJugadorEquipo**: Entidades Jugador y equipo
    - Creación de las entidades Jugador y Equipo.
    - CRUD de Jugador y Equipo.
    - Relación entre Jugador y Equipo.
    - DTO de Jugador.
    - Control de errores.
    - **A considerar** : Buscar manera de indicar nulo en la columna de equipo de jugador cuando se realice un DELETE 
      del equipo en el que se encuentra (Actualmente solo se puede realizar desde la base de datos con un ON DELETE).
- **2-EntidadesPartida**: Entidad Partida y Juego
    - Creación de la entidad Partida y Juego.
    - CRUD de Partida y Juego.
    - Trigger en la base de datos para actualizar los puntos de la tabla jugador y equipo cada vez que se añade una nueva partida.


## 📚 Información de interés

``Esta sección se irá actualizando con la adición de nuevas ramas.``

- Versión de Java: 11