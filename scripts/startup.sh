#!/bin/bash

# ─────────────────────────────────────────
# BLOQUE 1: Levantar el repositorio
# ─────────────────────────────────────────
echo "Clonando el repositorio..."
git clone https://github.com/Equipo-Dinamita-UNAL/project-backend
cd project-backend
cd OdontoGate

# ─────────────────────────────────────────
# BLOQUE 2: Instalar dependencias con Maven
# ─────────────────────────────────────────
echo "Instalando dependencias del proyecto..."
mvn clean install -DskipTests

# ─────────────────────────────────────────
# BLOQUE 3: Levantar base de datos con Docker
# ─────────────────────────────────────────
echo "Levantando base de datos PostgreSQL..."
docker compose up -d

echo "Esperando a que la base de datos este lista..."
sleep 10

# ─────────────────────────────────────────
# BLOQUE 4: Ejecutar pruebas basicas
# ─────────────────────────────────────────
echo "Ejecutando pruebas basicas..."
mvn test

# ─────────────────────────────────────────
# BLOQUE 5: Levantar la aplicacion
# ─────────────────────────────────────────
echo "Levantando la aplicacion Spring Boot..."
mvn spring-boot:run
