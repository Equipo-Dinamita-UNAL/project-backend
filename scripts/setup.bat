@echo off

REM ─────────────────────────────────────────
REM BLOQUE 1: Levantar el repositorio
REM ─────────────────────────────────────────
echo Clonando el repositorio...
git clone https://github.com/Equipo-Dinamita-UNAL/project-backend
cd project-backend

REM ─────────────────────────────────────────
REM BLOQUE 2: Instalar dependencias con Maven
REM ─────────────────────────────────────────
echo Instalando dependencias del proyecto...
call mvn clean install -DskipTests

REM ─────────────────────────────────────────
REM BLOQUE 3: Levantar base de datos con Docker
REM ─────────────────────────────────────────
echo Levantando base de datos PostgreSQL...
docker compose up -d

echo Esperando a que la base de datos este lista...
timeout /t 10 /nobreak

REM ─────────────────────────────────────────
REM BLOQUE 4: Ejecutar pruebas basicas
REM ─────────────────────────────────────────
echo Ejecutando pruebas basicas...
call mvn test

REM ─────────────────────────────────────────
REM BLOQUE 5: Levantar la aplicacion
REM ─────────────────────────────────────────
echo Levantando la aplicacion Spring Boot...
call mvn spring-boot:run
