#!/bin/bash
set -e  # Detener el script si cualquier comando falla

echo "============================================"
echo " Instalador Odontogate para Linux"
echo "============================================"


# 1. Instalar git y curl (con update previo)
echo "Instalando dependencias base..."
apt-get update
apt-get install -y git curl

# 2. Instalar Docker última versión
echo "Instalando Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
rm -f get-docker.sh

# 3. Clonar el repositorio
echo "Clonando repositorio..."
git clone https://github.com/Equipo-Dinamita-UNAL/project-backend.git /opt/odontogate

# 4. Asignar permisos al usuario actual
echo "Configurando permisos..."
chown -R "$SUDO_USER":"$SUDO_USER" /opt/odontogate 2>/dev/null || chown -R "$USER":"$USER" /opt/odontogate

# 5. Descargar ZIP de Spring Initializr con URL completa
echo "Descargando proyecto base Spring Boot..."
curl -L "https://start.spring.io/starter.zip\
?type=maven-project\
&language=java\
&bootVersion=3.5.0\
&javaVersion=21\
&artifactId=demo\
&groupId=com.example\
&dependencies=web,data-jpa,postgresql" \
  -o /opt/odontogate/scripts/demo.zip

# 6. Copiar el .env si no existe
if [ ! -f /opt/odontogate/scripts/.env ]; then
  echo "Creando archivo .env desde el ejemplo..."
  cp /opt/odontogate/scripts/.env.example /opt/odontogate/scripts/.env
  echo "AVISO: Edita /opt/odontogate/scripts/.env con tus credenciales antes de continuar."
fi

# 7. Entrar a scripts y levantar con docker compose
echo "Levantando proyecto..."
cd /opt/odontogate/scripts
docker compose up --build

echo "============================================"
echo " Proyecto corriendo en http://localhost:8080"
echo " Base de datos en localhost:5432"
echo "============================================"
