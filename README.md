# Prueba Técnica: API de Gestión de Franquicias

## Descripción del Proyecto

Este proyecto es una API RESTful desarrollada con **Spring Boot Webflux** que gestiona información sobre franquicias, sucursales y productos. La solución se basa en una arquitectura hexagonal para separar la lógica de negocio de la infraestructura, lo que facilita la mantenibilidad y la adaptabilidad.

La aplicación utiliza un enfoque reactivo en toda la pila, desde el controlador hasta la capa de persistencia, para manejar un gran número de solicitudes concurrentes de manera eficiente.

## Criterios de Aceptación (Arquitectura)

* **Arquitectura Hexagonal**: La estructura del proyecto está dividida en capas de dominio, aplicación e infraestructura para desacoplar las dependencias tecnológicas del núcleo de la lógica de negocio.
* **Programación Reactiva**: Se utiliza **Spring Boot Webflux** y **Project Reactor** para la gestión de flujos de datos asíncronos y no bloqueantes.
* **Persistencia de Datos**: La persistencia se implementa con **MySQL** utilizando **R2DBC** (Reactive Relational Database Connectivity). La base de datos está alojada en la nube usando **Aiven Cloud**, lo que permite que la capa de base de datos sea completamente reactiva y escalable.
* **Pruebas Unitarias**: Se han incluido pruebas unitarias para la capa de servicio y de integración para la capa de controlador con una cobertura superior al 60%.
* **Logging**: Se implementa un sistema de logging (`slf4j`) para el seguimiento del flujo de la aplicación.
* **RESTful API**: La exposición de los endpoints se realiza siguiendo los principios de una API RESTful, utilizando métodos HTTP y rutas claras.

## Mensajería (Endpoints)

La API expone los siguientes endpoints para las funcionalidades solicitadas:

### Funcionalidad Básica

* `POST /api/franquicias`: Agrega una nueva franquicia.
* `POST /api/franquicias/{franquiciaId}/sucursales`: Agrega una sucursal a una franquicia existente.
* `POST /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos`: Agrega un producto a una sucursal.
* `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock`: Modifica el stock de un producto.
* `DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}`: Elimina un producto de una sucursal.
* `GET /api/franquicias/{franquiciaId}/productos-mas-stock`: Muestra el producto con más stock por cada sucursal de una franquicia.
* `PUT /api/franquicias/{franquiciaId}/nombre`: Actualiza el nombre de una franquicia.
* `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/nombre`: Actualiza el nombre de una sucursal.
* `PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/nombre`: Actualiza el nombre de un producto.

## Consideraciones de Diseño

La arquitectura hexagonal se eligió para garantizar que la lógica de negocio, representada por el servicio de `Franquicia`, sea independiente de la tecnología subyacente de persistencia. Esto nos permite cambiar fácilmente de MySQL a otra base de datos (por ejemplo, MongoDB o Redis) sin modificar el código del servicio.

La adopción de un enfoque reactivo con Webflux es ideal para escenarios donde se espera un alto volumen de solicitudes y la mayoría de las operaciones son de I/O (entrada/salida), como las interacciones con la base de datos. Esto maximiza la utilización de recursos y mejora el rendimiento bajo carga.

## Infraestructura en la Nube

* **Base de Datos**: MySQL alojada en **Aiven Cloud** con conexión reactiva R2DBC
* **Escalabilidad**: La infraestructura en la nube permite escalamiento automático según la demanda
* **Disponibilidad**: Base de datos con alta disponibilidad y respaldo automático

## Despliegue Local

Para ejecutar la aplicación localmente, asegúrate de tener instalado Java 8+ y Maven.

1.  **Clonar el repositorio:** `git clone <https://github.com/svelacai/prueba-franquicia.git>`
2.  **Base de Datos en la Nube**:
    * La aplicación está configurada para conectarse automáticamente a la base de datos MySQL en Aiven Cloud:
    * **Host**: mysql-franquicia-santiago-4acf.b.aivencloud.com
    * **Puerto**: 27518
    * **Base de Datos**: defaultdb
     ```properties
        spring.application.name=prueba-franquicia

		# ===============================
		#  Configuración de MySQL (Aiven) con R2DBC
		# ===============================
		spring.r2dbc.url=r2dbc:mysql://mysql-franquicia-santiago-4acf.b.aivencloud.com:27518/defaultdb
		spring.r2dbc.username=avnadmin
		spring.r2dbc.password=AVNS_trd2DWx_sOlG0xbcMDi

		spring.sql.init.mode=always
		logging.level.prototipe.franquicia=INFO
		```


La aplicación se iniciará en `http://localhost:8080`.
