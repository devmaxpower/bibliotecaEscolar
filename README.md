# 📚 Biblioteca Escolar

Sistema de gestión de biblioteca escolar desarrollado en Java, utilizando programación orientada a objetos, arquitectura MVC, patrón DAO y conexión a una base de datos MySQL mediante JDBC.

El proyecto permite administrar libros, estudiantes y categorías, gestionar préstamos y devoluciones, controlar la disponibilidad de ejemplares y generar reportes.

## 🛠️ Tecnologías utilizadas

- Java JDK 26
- Java Swing
- Maven
- MySQL
- JDBC
- MySQL Connector/J 8.3.0
- MAMP
- IntelliJ IDEA
- Git y GitHub

## 🎯 Objetivo del proyecto

Desarrollar una aplicación de escritorio que permita gestionar los recursos de una biblioteca escolar, aplicando principios de programación orientada a objetos, patrones de diseño, persistencia de datos y programación concurrente.

El sistema contempla dos tipos de usuarios:

- **Bibliotecario:** administra los recursos de la biblioteca.
- **Estudiante:** consulta los libros disponibles.

## ✨ Funcionalidades

### Autenticación de usuarios

- Inicio de sesión mediante RUT y contraseña.
- Identificación del rol del usuario.
- Menú principal adaptado según el rol.
- Cierre de sesión.

### Gestión de libros

- Registrar nuevos libros.
- Consultar libros existentes.
- Modificar información.
- Eliminar registros.
- Administrar stock.
- Asociar libros a categorías.

### Gestión de estudiantes

- Registrar estudiantes.
- Consultar información.
- Actualizar datos.
- Eliminar estudiantes.

### Gestión de categorías

- Registrar categorías.
- Listar categorías.
- Modificar categorías.
- Eliminar categorías.

### Préstamos y devoluciones

- Registrar préstamos de libros.
- Asociar préstamos a estudiantes.
- Establecer fechas de vencimiento.
- Descontar automáticamente el stock.
- Registrar devoluciones.
- Recuperar el stock al devolver un ejemplar.
- Identificar préstamos atrasados.
- Evitar devoluciones duplicadas.

### Reportes

El sistema incorpora tres reportes:

1. **Libros más prestados:** muestra los libros ordenados por cantidad de préstamos.
2. **Historial por estudiante:** permite consultar los préstamos asociados a un estudiante.
3. **Préstamos activos:** muestra los préstamos pendientes e identifica los atrasados.

## 🏗️ Arquitectura del proyecto

El sistema utiliza una arquitectura basada en el patrón **Modelo-Vista-Controlador (MVC)**.

### Modelo

Contiene las clases que representan las entidades del sistema.

- `Persona`
- `Usuario`
- `Estudiante`
- `Libro`
- `Categoria`
- `Prestamo`

La clase `Persona` es abstracta y permite implementar herencia y polimorfismo mediante las clases `Usuario` y `Estudiante`.

### Vista

Contiene las interfaces gráficas desarrolladas con Java Swing.

- `Login`
- `MenuPrincipal`
- `GestionLibros`
- `GestionEstudiantes`
- `GestionCategorias`
- `GestionPrestamos`
- `Reportes`

### Controlador

Gestiona la comunicación entre las vistas y la lógica de acceso a datos.

- `ControladorLogin`
- `ControladorLibros`
- `ControladorEstudiantes`
- `ControladorCategorias`
- `ControladorPrestamos`
- `ControladorReportes`

### DAO

El patrón **Data Access Object (DAO)** permite separar las operaciones de base de datos del resto de la aplicación.

Cada entidad administrada utiliza una interfaz DAO y su correspondiente implementación.

### Singleton

La clase `ConexionDB` implementa el patrón Singleton para centralizar la configuración de acceso a MySQL.

### Servicio

La clase `ServicioPrestamos` concentra la lógica de préstamos y devoluciones, incluyendo mecanismos de sincronización y ejecución concurrente.

## 🧵 Programación concurrente

El módulo de préstamos utiliza:

- `ExecutorService` para ejecutar operaciones en hilos independientes.
- `CompletableFuture` para gestionar resultados asíncronos.
- `synchronized` para coordinar operaciones concurrentes.
- Transacciones JDBC para mantener la consistencia de los datos.

Las operaciones de préstamo y devolución utilizan `commit()` y `rollback()` para evitar actualizaciones parciales.

Además, las actualizaciones de stock se realizan mediante condiciones SQL que impiden descontar ejemplares cuando no existe disponibilidad.

## 🗄️ Base de datos

El proyecto utiliza una base de datos MySQL llamada:

`biblioteca`

### Tablas principales

| Tabla | Descripción |
|-------|-------------|
| usuarios | Credenciales y roles |
| estudiantes | Información de estudiantes |
| categorias | Clasificación de libros |
| libros | Catálogo y stock |
| prestamos | Registro de préstamos y devoluciones |

Las tablas utilizan claves primarias y foráneas para mantener la integridad referencial.

## ⚙️ Requisitos de instalación

Para ejecutar el proyecto se necesita:

- Java JDK 26.
- IntelliJ IDEA o un IDE compatible con Maven.
- MySQL.
- MAMP o un servidor MySQL equivalente.
- Maven.
- MySQL Connector/J 8.3.0.

## 🚀 Instalación y ejecución

### 1. Obtener el proyecto

Clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

Ingresar al directorio:

```bash
cd BibliotecaEscolar
```

Reemplazar `URL_DEL_REPOSITORIO` por la dirección real del repositorio.

### 2. Configurar MySQL

Iniciar MAMP y verificar que el servidor MySQL esté funcionando.

Crear la base de datos:

```sql
CREATE DATABASE IF NOT EXISTS biblioteca;
```

Ejecutar el script SQL del proyecto para crear las tablas e insertar los registros iniciales.

### 3. Configurar la conexión

Abrir:

`src/main/java/conexion/ConexionDB.java`

Revisar los siguientes parámetros:

```java
private static final String URL =
        "jdbc:mysql://localhost:8889/biblioteca";

private static final String USUARIO = "root";

private static final String PASSWORD = "root";
```

Modificar los valores según la configuración local de MySQL.

El puerto `8889` corresponde a la configuración utilizada durante el desarrollo con MAMP.

### 4. Instalar dependencias

Abrir el proyecto en IntelliJ IDEA.

Maven descargará las dependencias declaradas en `pom.xml`.

La dependencia principal de base de datos es MySQL Connector/J.

### 5. Ejecutar la aplicación

Ejecutar la clase:

`vista.Login`

Se abrirá la ventana de autenticación del sistema.

## 🔐 Usuario de prueba

Para comprobar las funcionalidades administrativas se puede utilizar el siguiente usuario de demostración:

| Campo | Valor |
|-------|-------|
| Nombre | Antonia Pérez |
| RUT | 12345678-9 |
| Contraseña | clave123 |
| Rol | bibliotecario |

**Importante:** estas credenciales corresponden exclusivamente a datos de demostración del proyecto académico.

## 🧪 Pruebas funcionales

Se recomienda verificar los siguientes escenarios:

| Prueba | Resultado esperado |
|--------|--------------------|
| Login válido | Permite acceder al sistema |
| Login inválido | Rechaza el acceso |
| Registrar libro | Guarda el registro en MySQL |
| Modificar libro | Actualiza sus datos |
| Eliminar libro | Elimina el registro si no existen restricciones referenciales |
| Registrar estudiante | Guarda sus datos |
| Registrar categoría | Guarda la categoría |
| Registrar préstamo | Descuenta una unidad del stock |
| Devolver libro | Incrementa una unidad del stock |
| Devolución duplicada | Rechaza la segunda devolución |
| Préstamo sin stock | Rechaza la operación |
| Consultar reportes | Muestra información de MySQL |

## 📁 Estructura del proyecto

```text
BibliotecaEscolar/
│
├── pom.xml
├── README.md
├── .gitignore
│
└── src/
    └── main/
        └── java/
            │
            ├── conexion/
            │   └── ConexionDB.java
            │
            ├── modelo/
            │   ├── Persona.java
            │   ├── Usuario.java
            │   ├── Estudiante.java
            │   ├── Libro.java
            │   ├── Categoria.java
            │   └── Prestamo.java
            │
            ├── dao/
            │   ├── LibroDAO.java
            │   ├── EstudianteDAO.java
            │   ├── CategoriaDAO.java
            │   ├── UsuarioDAO.java
            │   ├── PrestamoDAO.java
            │   ├── ReporteDAO.java
            │   │
            │   └── impl/
            │       ├── LibroDAOImpl.java
            │       ├── EstudianteDAOImpl.java
            │       ├── CategoriaDAOImpl.java
            │       ├── UsuarioDAOImpl.java
            │       ├── PrestamoDAOImpl.java
            │       └── ReporteDAOImpl.java
            │
            ├── controlador/
            │   ├── ControladorLogin.java
            │   ├── ControladorLibros.java
            │   ├── ControladorEstudiantes.java
            │   ├── ControladorCategorias.java
            │   ├── ControladorPrestamos.java
            │   └── ControladorReportes.java
            │
            ├── servicio/
            │   └── ServicioPrestamos.java
            │
            └── vista/
                ├── Login.java
                ├── MenuPrincipal.java
                ├── GestionLibros.java
                ├── GestionEstudiantes.java
                ├── GestionCategorias.java
                ├── GestionPrestamos.java
                └── Reportes.java
```

## 🔒 Consideraciones de seguridad

Esta aplicación fue desarrollada con fines académicos.

En un entorno de producción se recomienda:

- Almacenar las contraseñas mediante algoritmos de hash seguros.
- Utilizar variables de entorno para las credenciales de MySQL.
- Implementar validaciones de autorización en la lógica de negocio.
- Evitar publicar credenciales reales.
- Incorporar registros de errores y auditoría.

## 📌 Estado del proyecto

El proyecto incorpora los módulos de autenticación, administración de recursos, préstamos, devoluciones y reportes.

Antes de su entrega definitiva, se deben completar las pruebas funcionales y verificar el cumplimiento de los requisitos de evaluación.

## 📖 Contexto académico

Proyecto desarrollado como parte de una evaluación académica, con el objetivo de aplicar conocimientos de programación orientada a objetos, desarrollo de interfaces gráficas, patrones de diseño, bases de datos y concurrencia en Java.