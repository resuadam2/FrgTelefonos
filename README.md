# FrgTelefonos
Proyecto de ejemplo Android + Java que ejemplifica el uso de un Fragment tratado como un componente independiente, reutilizable y que puede ser utilizado en diferentes actividades.

## Objetivo

El objetivo de este proyecto es ejemplificar el uso de un Fragment como un componente independiente, reutilizable y que puede ser utilizado en diferentes actividades.

## Descripción

El proyecto consiste en una aplicación que muestra una lista de teléfonos fijos (4 en este caso).

Cada teléfono es un fragmento independiente pero desde la actividad principal podemos tener acceso y visibilidad de todos ellos para que puedan interactuar entre sí.

Los fragmentos de los telefonos están compuestos por un número de teléfono, un edittext para introducir el número al que se quiere llamar y un botón para realizar la llamada.

Los fragmentos pueden llamar y colgar a otros fragmentos, y a otros números de teléfono que no existan en la aplicación.

Existe una actividad secundaria con información de las llamadas de un teléfono en concreto a la cuál se accede seleccionando dicho teléfono en un spinner desde la actividad principal.

Temas tratados en el ejemplo:
- Uso de Fragments como componentes independientes
- Uso de interfaces para comunicar fragmentos con la actividad principal
- Uso de interfaces para comunicar fragmentos entre sí
- Uso de interfaces para comunicar fragmentos con otros fragmentos y con la actividad principal
- Listeners
- Uso de Toasts
- Uso de Intents
- Base de datos SQLite