# Prueba Técnica: API de Gestión de Franquicias

## Descripción del Proyecto

Este proyecto es una API RESTful desarrollada con **Spring Boot Webflux** que gestiona información sobre franquicias, sucursales y productos. La solución se basa en una arquitectura hexagonal para separar la lógica de negocio de la infraestructura, lo que facilita la mantenibilidad y la adaptabilidad.

La aplicación utiliza un enfoque reactivo en toda la pila, desde el controlador hasta la capa de persistencia, para manejar un gran número de solicitudes concurrentes de manera eficiente.

## Criterios de Aceptación (Arquitectura)

* **Arquitectura Hexagonal**: La estructura del proyecto está dividida en capas de dominio, aplicación e infraestructura para desacoplar las dependencias tecnológicas del núcleo de la lógica de negocio.
* **Programación Reactiva**: Se utiliza **Spring Boot Webflux** y **Project Reactor** para la gestión de flujos de datos asíncronos y no bloqueantes.
* **Persistencia de Datos**: La persistencia se implementa con **MySQL** utilizando **R2DBC** (Reactive Relational Database Connectivity). La base de datos está alojada en la nube usando **Aiven Cloud**, lo que permite que la capa de base de datos sea completamente reactiva y escalable.
* **Pruebas Unitarias**: Se han incluido 22 pruebas unitarias (12 para la capa de servicio y 10 para la capa de controlador) con una cobertura del 76% de instrucciones y 66% de ramas, superando el requisito del 60%.
* **Docker**: La aplicación está completamente contenerizada con Docker y Docker Compose para facilitar el despliegue.
* **Infrastructure as Code (IaC)**: Se incluye configuración de Terraform para provisionar la infraestructura de base de datos en Aiven Cloud.
* **Logging**: Se implementa un sistema de logging (`slf4j`) para el seguimiento del flujo de la aplicación.
* **RESTful API**: La exposición de los endpoints se realiza siguiendo los principios de una API RESTful, utilizando métodos HTTP y rutas claras.
* **Documentación API**: Se incluye documentación automática con **Swagger/OpenAPI** accesible en `/swagger-ui.html`.

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
* **Infrastructure as Code**: Terraform para provisionar y gestionar la infraestructura de forma declarativa

## Despliegue

### Opción 1: Despliegue con Docker (Recomendado)

**Requisitos**: Docker y Docker Compose instalados

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/svelacai/prueba-franquicia.git
   cd franquicia-hexa
   ```

2. **Construir y ejecutar con Docker Compose:**
   ```bash
   docker-compose up --build
   ```

3. **La aplicación estará disponible en:** `http://localhost:8080`

4. **Detener la aplicación:**
   ```bash
   docker-compose down
   ```

### Opción 2: Despliegue Local (Sin Docker)

**Requisitos**: Java 8+ y Maven instalados

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/svelacai/prueba-franquicia.git
   cd franquicia-hexa
   ```

2. **Compilar y ejecutar:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **La aplicación estará disponible en:** `http://localhost:8080`

### Configuración de Base de Datos

La aplicación está configurada para conectarse a MySQL en Aiven Cloud:
- **Host**: mysql-franquicia-santiago-4acf.b.aivencloud.com
- **Puerto**: 27518
- **Base de Datos**: defaultdb

```properties
spring.r2dbc.url=r2dbc:mysql://mysql-franquicia-santiago-4acf.b.aivencloud.com:27518/defaultdb
spring.r2dbc.username=avnadmin
spring.r2dbc.password=AVNS_trd2DWx_sOlG0xbcMDi
```

### Documentación de la API

* **Swagger UI**: `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Pruebas y Cobertura

Para ejecutar las pruebas y generar el reporte de cobertura:

```bash
mvn clean test
```

El reporte de cobertura JaCoCo se genera en: `target/site/jacoco/index.html`

**Cobertura Actual:**
- 22 pruebas unitarias (100% exitosas)
- 76% de cobertura de instrucciones
- 66% de cobertura de ramas
- Cobertura por capa:
  - Servicio (application): 86%
  - Controlador (web): 85%
  - Modelos (domain): 100%

## Infrastructure as Code (Terraform)

El proyecto incluye configuración de Terraform para provisionar la infraestructura de base de datos en Aiven Cloud.

### Requisitos

- Terraform >= 1.0
- Cuenta en Aiven Cloud
- API Token de Aiven

### Uso de Terraform

1. **Navegar al directorio de Terraform:**
   ```bash
   cd terraform
   ```

2. **Configurar variables:**
   ```bash
   cp terraform.tfvars.example terraform.tfvars
   # Editar terraform.tfvars con tu API token de Aiven
   ```

3. **Inicializar Terraform:**
   ```bash
   terraform init
   ```

4. **Planificar la infraestructura:**
   ```bash
   terraform plan
   ```

5. **Aplicar la configuración:**
   ```bash
   terraform apply
   ```

6. **Ver outputs (credenciales de conexión):**
   ```bash
   terraform output
   ```

7. **Destruir la infraestructura (cuando ya no se necesite):**
   ```bash
   terraform destroy
   ```

### Recursos Provisionados

- **MySQL Service**: Instancia de MySQL 8 en Google Cloud (us-east1)
- **Database**: Base de datos `defaultdb`
- **Plan**: startup-4 (escalable según necesidades)
- **Acceso Público**: Habilitado para conexiones externas

## Tecnologías Utilizadas

- **Backend**: Spring Boot 2.7.18 + Webflux
- **Programación Reactiva**: Project Reactor
- **Base de Datos**: MySQL 8 + R2DBC
- **Cloud Provider**: Aiven Cloud (Google Cloud Platform)
- **Contenerización**: Docker + Docker Compose
- **IaC**: Terraform
- **Testing**: JUnit 5 + Mockito + Reactor Test
- **Cobertura**: JaCoCo
- **Documentación**: Swagger/OpenAPI
- **Build Tool**: Maven
