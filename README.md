#### Jeremy Suriel 1-20-2049 ####

# Actividad Semana 4 Proyecto Podman: Apps en Node.js, PHP y Java con Balanceador de Carga NGINX

Este proyecto demuestra cómo levantar tres aplicaciones simples (Node.js, PHP, Java) usando **Podman**, agruparlas en un **pod**, y conectarlas mediante un **balanceador de carga NGINX**, todo sin usar Docker.

---

##  Tecnologías usadas

- [Podman](https://podman.io/) (alternativa sin daemon a Docker)
- Node.js
- PHP
- Java (OpenJDK)
- NGINX (como balanceador de carga)
- PowerShell o Bash para comandos

---

##  Requisitos Previos

- Tener instalado [Podman Desktop](https://podman.io/getting-started/installation)
- Tener configurada e iniciada una máquina virtual Podman:

```powershell
podman machine init
podman machine start
---

##  Estructura del proyecto

```
proyecto-podman/
├── app-node/
│   ├── index.js
│   └── Dockerfile
├── app-php/
│   ├── index.php
│   └── Dockerfile
├── app-java/
│   ├── Main.java
│   └── Dockerfile
├── nginx/
│   ├── nginx.conf
│   └── Dockerfile
```

---

##  Instrucciones paso a paso

### 1. Clonar el repositorio

```bash
git clone hhttps://github.com/COPY0096/paralelo-podman.git
cd proyecto-podman
```

### 2. Crear el pod (exponiendo puerto 8080)

```powershell
podman pod create --name clusterapp -p 8080:80
```

### 3. Construir las imágenes

```powershell
podman build -t app-node ./app-node
podman build -t app-php ./app-php
podman build -t app-java ./app-java
podman build -t app-balancer ./nginx
```

### 4. Correr los contenedores **dentro del pod** (sin usar `-p`)

```powershell
podman run -dt --pod=clusterapp --name nodejs app-node
podman run -dt --pod=clusterapp --name php app-php
podman run -dt --pod=clusterapp --name java app-java
podman run -dt --pod=clusterapp --name nginx app-balancer
```

---

## ✅ Verificación

### Probar en navegador

Abre en tu navegador:

```
http://localhost:8080
```

Deberías ver mensajes rotativos como:

- `Hola desde Node.js`
- `Hola desde PHP`
- `Hola desde Java`

### Verificar en terminal

```powershell
curl http://localhost:8080
```

Ejecuta varias veces para ver la rotación de respuestas por el balanceador de carga.

---

##  Comprobaciones útiles

Ver contenedores corriendo:

```powershell
podman ps
```

Ver pods:

```powershell
podman pod ps
```

Ver logs de nginx:

```powershell
podman logs nginx
```

---

##  Errores comunes y soluciones

###  `Cannot connect to Podman` o `podman.sock`

**Solución:**

```powershell
podman machine init
podman machine start
```

---

###  `name "clusterapp" is in use: pod already exists`

**Solución:**

```powershell
podman pod rm -f clusterapp
```

---

###  `Unable to connect to the remote server` con `curl`

**Posibles causas:**
- El balanceador no está corriendo o mal configurado
- El puerto 8080 ya está en uso (por ejemplo, Jenkins)

**Soluciones:**
- Verifica el archivo `nginx.conf` esté usando nombres de contenedores:
  ```nginx
  upstream backend {
    server nodejs:3000;
    server php:3001;
    server java:3002;
  }
  ```
- Asegúrate de no usar `-p` en los contenedores dentro del pod

---

###  Jenkins ocupa el puerto 8080

**Soluciones:**
- Detener Jenkins:
  ```powershell
  Stop-Service jenkins  # Ejecuta PowerShell como administrador
  ```
- O bien, usar otro puerto:
  ```powershell
  podman pod create --name clusterapp -p 8888:80
  ```

---

##  Limpieza

Eliminar el pod y todos los contenedores asociados:

```powershell
podman pod rm -f clusterapp
```

