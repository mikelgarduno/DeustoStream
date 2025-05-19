![example workflow](https://github.com/mikelgarduno/DeustoStream/actions/workflows/maven.yml/badge.svg)
# Proyecto DeustoStream

Aplicación web Java Spring Boot con Thymeleaf para gestión y visualización de películas y series. Incluye API REST, autenticación (login QR), tests y documentación con Doxygen.

---

## 📁 Estructura del Proyecto

- `src/main/java`: Código fuente principal.
- `src/test/java`: Tests (unitarios, integración, rendimiento).
- `src/main/resources/static`: Archivos estáticos (CSS, JS, imágenes).
- `src/main/resources/templates`: Vistas Thymeleaf.
- `Doxyfile`: Configuración Doxygen para documentación.
- `docs/`: Documentación HTML generada.

---

## 🚀 Ejecución

1.  Clonar: `git clone https://docs.github.com/en/repositories/creating-and-managing-repositories/deleting-a-repository` y `cd deustostream`.
2.  Compilar y ejecutar: `mvn clean install` y `mvn spring-boot:run`.
3.  Acceder: `http://localhost:8080`.

## 📲 Login con QR y uso de ngrok

Esta sección explica cómo habilitar el login mediante código QR para acceso desde dispositivos externos utilizando la herramienta **ngrok**.

Para que la funcionalidad de login por QR funcione correctamente cuando se accede a la aplicación desde dispositivos móviles u otros equipos fuera de la red local donde se está ejecutando el servidor, es necesario exponer el puerto 8080 (donde se ejecuta la aplicación) a través de una URL pública. **ngrok** es una herramienta que crea un túnel seguro desde una URL pública a un puerto local.

**Paso para abrir el puerto con ngrok:**

1.  **Descarga e instala ngrok:** Si no tienes ngrok instalado, descárgalo desde [https://ngrok.com/download](https://ngrok.com/download) y sigue las instrucciones de instalación para tu sistema operativo.

2.  **Ejecuta ngrok para exponer el puerto 8080:** Abre una nueva terminal y ejecuta el siguiente comando:
    ```bash
    ngrok http --url=fleet-thoroughly-oryx.ngrok-free.app 8080
    ```
    Este comando le dice a ngrok que cree un túnel HTTP hacia el puerto 8080 de tu máquina local, utilizando el subdominio `fleet-thoroughly-oryx.ngrok-free.app`. **Es importante notar que este subdominio específico podría no estar disponible o podría ser necesario configurarlo a través de una cuenta de ngrok.** ngrok te proporcionará una URL pública única (generalmente con el formato `https://fleet-thoroughly-oryx.ngrok-free.app/` o similar).

3.  **Configurar la URL de ngrok en la aplicación:** Asegúrate de que la URL pública generada por ngrok esté configurada correctamente en la lógica de generación de códigos QR de tu aplicación. Esto permitirá que los códigos QR apunten a la dirección pública correcta para el acceso externo.

    La URL pública generada por ngrok será algo similar a:
    [https://fleet-thoroughly-oryx.ngrok-free.app](https://fleet-thoroughly-oryx.ngrok-free.app/)
   
    Utiliza esta URL para acceder a la aplicación desde dispositivos fuera de tu red local y probar la funcionalidad de login con QR.

**Nota importante:** Las URLs generadas por ngrok para cuentas gratuitas son temporales y cambian cada vez que se reinicia la sesión de ngrok. Para URLs estables, se requiere una cuenta de pago de ngrok.

-----

## 🧪 Ejecución de Tests

### ✅ Unitarios

Verifican unidades de código (métodos, clases) aisladamente con Mockito. Ejecutar con: `mvn test`.

### 🔁 Integración

Prueban la interacción entre componentes (requiere `@SpringBootTest`). Ejecutar con: 
```bash
    mvn verify -Pintegration-tests
```

### ⚙️ Rendimiento

Evalúan eficiencia y respuesta bajo carga. Ejecutar con:
```bash
    mvn verify -Pperformance-tests
```
-----

## 📄 Documentación con Doxygen

Genera documentación técnica desde comentarios del código.

1.  Instalar Doxygen ([https://www.doxygen.nl/download.html](https://www.doxygen.nl/download.html)).
2.  Navegar a la raíz del proyecto.
3.  Ejecutar: `doxygen Doxyfile`.
4.  Abrir `docs/html/index.html`.

-----

**Desarrollado con ❤️ por el equipo de DeustoStream**
---
