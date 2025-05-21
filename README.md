 # 🔧 Okoro Time Backend - Sistema de Control Horario

El backend de Okoro Time proporciona una API completa para la gestión de fichajes y usuarios, desarrollada con Spring Boot y JWT para autenticación segura.

## 👩‍💻 Desarrollador

| Nombre           | GitHub                                      |                                     
|------------------|---------------------------------------------|
| Milena Okoro     | [@MilenaOcoro](https://github.com/MilenaOcoro) | 

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- JPA / Hibernate
- PostgreSQL
- H2 Database (testing)

## 🛠️ Instalación y ejecución

1. Clona el repositorio:
```bash
git clone https://github.com/MilenaOcoro/backend-okoro-time.git
```

2. Asegúrate de tener Java 21 y Maven instalados

3. Configura la base de datos PostgreSQL en `application.properties`

4. Ejecuta el proyecto:
```bash
./mvnw spring-boot:run
```

El servidor se ejecutará en: [http://localhost:8080](http://localhost:8080)

## 📋 Requisitos del sistema

- Java 21 o superior
- Maven 3.6 o superior
- PostgreSQL 15 o superior

## 📚 Endpoints API

### Autenticación

- `POST /api/auth/login` - Iniciar sesión y obtener token JWT
- `POST /api/auth/create-admin` - Crear usuario administrador inicial

### Usuarios

- `GET /api/users` - Obtener todos los usuarios (solo admin)
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario (solo admin)
- `POST /api/users/change-password` - Cambiar contraseña

### Fichajes

- `GET /api/clock-records` - Obtener todos los fichajes (solo admin)
- `GET /api/clock-records/my-records` - Obtener fichajes del usuario actual
- `GET /api/clock-records/{id}` - Obtener fichaje por ID
- `GET /api/clock-records/latest` - Obtener el último fichaje del usuario
- `POST /api/clock-records` - Crear nuevo fichaje
- `PUT /api/clock-records/{id}` - Actualizar fichaje
- `DELETE /api/clock-records/{id}` - Eliminar fichaje (solo admin)

### Resúmenes

- `GET /api/clock-records/summary` - Obtener resumen de horas trabajadas

## 📁 Estructura del proyecto

```
com.okoro.time/ 
│
├── controller/
│   ├── AuthController.java
│   ├── ClockRecordController.java
│   ├── SummaryController.java
│   └── UserController.java
│
├── dto/
│   ├── request/
│   └── response/
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
│
├── model/
│   ├── ClockRecord.java
│   └── User.java
│
├── repository/
│   ├── ClockRecordRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── JwtUtils.java
│   ├── UserDetailsServiceImpl.java
│   └── UserSecurity.java
│
├── service/
│   ├── AuthService.java
│   ├── ClockRecordService.java
│   ├── SummaryService.java
│   └── UserService.java
│
├── util/
│   └── Constants.java
│
└── TimeApplication.java
```

## 🔒 Seguridad

La API implementa autenticación basada en JWT con los siguientes aspectos de seguridad:

- Tokens JWT con expiración configurable
- Protección de rutas según roles (ADMIN / USER)
- Encriptación de contraseñas con BCrypt
- Validación de entradas con anotaciones Bean Validation
- Manejo global de excepciones y respuestas HTTP adecuadas
 

## 🧪 Tests

Pendiente de aplicar 

## 📊 Estadísticas

El endpoint de resumen proporciona estadísticas avanzadas sobre las horas trabajadas:
- Días totales trabajados
- Minutos totales trabajados
- Promedio de minutos por día 

## 🔄 Integración con Frontend

Este backend está diseñado para trabajar con la aplicación frontend React de Okoro Time, que puedes encontrar en [Repositorio Frontend](https://github.com/MilenaOcoro/frontend-okoro-time).

## 📝 Licencia

Este proyecto está licenciado bajo [MIT License](LICENSE).

Copyright © 2025 Milena Okoro