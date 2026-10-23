# Prestige Motors – Backend (Spring Boot + MySQL)

Frontend HTML + backend Java en un solo proyecto. Spring Boot sirve tus páginas desde
`src/main/resources/static/` y expone la API en `/api/...` (mismo servidor, sin CORS).

## 1. Cómo ejecutarlo

1. Abre **XAMPP** y arranca **MySQL** (no hace falta crear la base de datos: se crea sola).
2. Abre la carpeta como proyecto **Maven** en tu IDE (IntelliJ / Eclipse / NetBeans / VS Code). Requiere **Java 17**.
3. Si tu MySQL tiene clave, cámbiala en `src/main/resources/application.properties`
   (por defecto: usuario `root`, sin clave).
4. Ejecuta `PrestigeMotorsApplication` (o `mvn spring-boot:run`).
5. Abre **http://localhost:8080/index.html**

> Importante: abre las páginas desde `localhost:8080`, no con doble clic ni con Live Server,
> porque el HTML llama a `/api/...` en el mismo servidor.

La primera vez se crean las tablas y se cargan los 25 vehículos de tu `autos.js`
(desde `src/main/resources/seed/autos.json`).

## 2. Endpoints

| Método | Ruta | Para qué |
|---|---|---|
| GET | `/api/autos` | Catálogo (AutoSubasta). Filtros opcionales: `q`, `tipo`, `marca`, `modelo`, `anio`, `min`, `max`, `orden` |
| GET | `/api/autos/{slug}` | Detalle de un vehículo (`detalle.html?id=porsche-911`) |
| POST | `/api/mantenimientos` | Registrar solicitud de servicio (`registro.html`) |

Ejemplos:

```
GET /api/autos?tipo=Camioneta&orden=precio_desc
GET /api/autos?q=porsche&anio=2021&min=100000&max=300000
GET /api/autos/porsche-911
```

`orden`: `fecha_desc` (por defecto), `fecha_asc`, `precio_asc`, `precio_desc`.

`POST /api/mantenimientos` (JSON):

```json
{
  "nombre": "Jane", "apellido": "Smitherton", "correo": "jane@correo.com",
  "propietario": "Jane Smitherton", "modelo": "Ford Ranger Raptor",
  "placa": "ABC-123", "servicio": "CAMBIO_ACEITE", "observaciones": "Ruido al frenar"
}
```

Respuesta `201`: `{ "codigo": "MT-2026-00001", "estado": "PENDIENTE", "fechaSolicitud": "..." }`
Si hay datos inválidos responde `400` con `{ "mensaje": "...", "errores": { "placa": "..." } }`.

## 3. Estructura

```
src/main/java/pe/prestigemotors/
 ├─ controller/   AutomovilController, MantenimientoController
 ├─ service/      AutomovilService (filtros y orden), MantenimientoService
 ├─ repository/   AutomovilRepository, ClienteRepository, MantenimientoRepository
 ├─ model/        Automovil, Cliente, Usuario, Venta, Mantenimiento + enums
 ├─ dto/          AutomovilResponse, MantenimientoRequest, MantenimientoResponse
 ├─ exception/    ManejadorExcepciones (errores en JSON)
 └─ config/       DataLoader (carga inicial del catálogo)
```

## 4. Qué cambió en el frontend

- `AutoSubasta.html`: la lista ya no está escrita a mano; se llena con `GET /api/autos`. Funcionan el buscador, el rango de precios, el orden y el contador de vehículos.
- `detalle.html`: lee el vehículo con `GET /api/autos/{slug}` (ya no usa `autos.js`, que se eliminó).
- `registro.html`: el formulario envía `POST /api/mantenimientos` y muestra el código de la solicitud o los errores.
- `css.css`: se añadieron estilos para los mensajes de estado al final del archivo.
- `index.html` no se tocó; sus enlaces a `detalle.html?id=...` ya funcionan con la API.

## 5. Para revisar

- **Columnas de las entidades:** `Cliente`, `Usuario`, `Venta` y `Mantenimiento` siguen las entidades de tu ER, pero los atributos exactos son supuestos. Ajústalos a tu diagrama.
- **Imágenes faltantes:** 7 vehículos de AutoSubasta (`mercedes-cla`, `tesla-model-s`, `bugatti-chiron`, `aston-martin-rapide`, `mercedes-s580`, `rolls-royce-wraith`, `mazda-6`) apuntan a imágenes que no están en `img/`; ya pasaba en tu HTML original.
- **Datos repetidos:** los precios, motores y kilometrajes de varios autos son los de relleno de tu `autos.js`.
