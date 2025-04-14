![example workflow](https://github.com/mikelgarduno/DeustoStream/actions/workflows/maven.yml/badge.svg)
# 🎬 DeustoStream 🍿

## 🌟 Descripción del Proyecto

DeustoStream es una plataforma de streaming diseñada para ofrecer una experiencia de entretenimiento intuitiva y personalizada. Los usuarios pueden explorar un amplio catálogo de series y películas, gestionar listas de favoritos y disfrutar de recomendaciones basadas en sus preferencias.

## 📦 Tecnologías Utilizadas

- **Backend:** Java con Spring Boot  
- **Base de Datos:** MySQL  
- **Frameworks y Librerías:** Spring Boot, JPA/Hibernate, Thymeleaf  
- **Gestor de Dependencias:** Maven  

## 🚀 Configuración y Ejecución del Proyecto

### 1⃣ Configurar la Base de Datos MySQL

Antes de ejecutar el proyecto, es necesario crear la base de datos y configurar el usuario. Puedes hacer esto ejecutando el script `dbsetup.sql`, que crea un usuario y una base de datos llamada `restapidb`:

```sh
mysql -u root -p < src/main/resources/dbsetup.sql
```

El script `dbsetup.sql` realiza las siguientes acciones:

```sql
DROP USER IF EXISTS 'spq'@'%';
CREATE USER IF NOT EXISTS 'spq'@'%' IDENTIFIED BY 'spq';

DROP SCHEMA IF EXISTS restapidb;
CREATE SCHEMA restapidb;

GRANT ALL ON restapidb.* TO 'spq'@'%';
FLUSH PRIVILEGES;
```

Esto asegura que el usuario `spq` tenga acceso a la base de datos `restapidb`.

### 2⃣ Configurar el Proyecto

Asegúrate de que todas las dependencias están instaladas antes de ejecutar la aplicación. Para ello, ejecuta:

```sh
mvn compile
```

Esto descargará todas las dependencias necesarias y verificará que el código compile correctamente.

### 3⃣ Ejecutar la Aplicación

Para iniciar la aplicación, usa el siguiente comando:

```sh
mvn spring-boot:run
```

Si todo está correctamente configurado, el servidor se iniciará y estará disponible en `http://localhost:8080/`.

### 4⃣ Acceso a la Aplicación

- **Ventana de acceso inicial:** [http://localhost:8080](http://localhost:8080)
- **Usuarios normales:** [http://localhost:8080/catalogo](http://localhost:8080/catalogo)
- **Administración:** [http://localhost:8080/admin/usuarios](http://localhost:8080/admin/usuarios)

Los usuarios registrados con un correo `@deustostream.es` tienen acceso a la administración. Los demás usuarios solo pueden acceder al catálogo.

### 5⃣ Detener la Aplicación

Para detener la aplicación, presiona `Ctrl + C` en la terminal donde se está ejecutando.

### 6⃣ Empaquetar la Aplicación

Si deseas generar un archivo `.jar` ejecutable, usa:

```sh
mvn package
```

Luego, puedes ejecutar la aplicación con:

```sh
java -jar target/deustostream-0.0.1-SNAPSHOT.jar
```

---

**Desarrollado con ❤️ por el equipo de DeustoStream**
---
