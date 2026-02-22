# Infrastructure as Code - Terraform

Este directorio contiene la configuración de Terraform para provisionar la infraestructura de base de datos MySQL en Aiven Cloud.

## Estructura de Archivos

```
terraform/
├── main.tf                    # Configuración principal de recursos
├── variables.tf               # Definición de variables
├── outputs.tf                 # Outputs de la infraestructura
├── terraform.tfvars.example   # Plantilla de variables
└── .gitignore                 # Archivos ignorados por Git
```

## Requisitos Previos

1. **Terraform**: Versión 1.0 o superior
   ```bash
   # Verificar instalación
   terraform version
   ```

2. **Cuenta en Aiven Cloud**: https://aiven.io/
   - Crear una cuenta gratuita o de pago
   - Generar un API Token desde la consola de Aiven

3. **API Token de Aiven**:
   - Ir a: https://console.aiven.io/profile/auth
   - Generar un nuevo token
   - Guardar el token de forma segura

## Configuración Inicial

### 1. Configurar Variables

Copiar el archivo de ejemplo y completar con tus valores:

```bash
cp terraform.tfvars.example terraform.tfvars
```

Editar `terraform.tfvars`:

```hcl
aiven_api_token      = "tu-api-token-aqui"
aiven_project_name   = "franquicia-project"
environment          = "dev"
```

### 2. Inicializar Terraform

```bash
terraform init
```

Este comando descarga el provider de Aiven y prepara el directorio de trabajo.

## Uso

### Planificar Cambios

Antes de aplicar cambios, revisa qué recursos se crearán:

```bash
terraform plan
```

### Aplicar Configuración

Crear la infraestructura:

```bash
terraform apply
```

Confirmar con `yes` cuando se solicite.

### Ver Outputs

Después de aplicar, ver las credenciales de conexión:

```bash
terraform output
```

Para ver un output específico:

```bash
terraform output mysql_host
terraform output mysql_port
```

### Actualizar Infraestructura

Si modificas los archivos `.tf`, aplica los cambios:

```bash
terraform plan
terraform apply
```

### Destruir Infraestructura

Cuando ya no necesites los recursos:

```bash
terraform destroy
```

⚠️ **ADVERTENCIA**: Esto eliminará permanentemente todos los recursos y datos.

## Recursos Provisionados

### MySQL Service

- **Provider**: Aiven (Google Cloud Platform)
- **Región**: us-east1
- **Plan**: startup-4
- **Versión**: MySQL 8
- **Acceso Público**: Habilitado
- **Ventana de Mantenimiento**: Domingos 03:00 UTC

### Database

- **Nombre**: defaultdb
- **Charset**: utf8mb4 (por defecto)

## Variables Disponibles

| Variable | Descripción | Valor por Defecto | Requerido |
|----------|-------------|-------------------|-----------|
| `aiven_api_token` | Token de API de Aiven | - | Sí |
| `aiven_project_name` | Nombre del proyecto en Aiven | `franquicia-project` | No |
| `environment` | Entorno (dev/staging/prod) | `dev` | No |

## Outputs Disponibles

| Output | Descripción | Sensible |
|--------|-------------|----------|
| `mysql_service_uri` | URI completa de conexión | Sí |
| `mysql_host` | Host del servidor MySQL | No |
| `mysql_port` | Puerto del servidor MySQL | No |
| `mysql_username` | Usuario de MySQL | No |
| `database_name` | Nombre de la base de datos | No |

## Seguridad

- ⚠️ **NUNCA** commitear `terraform.tfvars` al repositorio
- ⚠️ **NUNCA** compartir el API Token públicamente
- Los archivos `.tfstate` contienen información sensible, mantenerlos seguros
- Usar variables de entorno para CI/CD:
  ```bash
  export TF_VAR_aiven_api_token="tu-token"
  terraform apply -auto-approve
  ```

## Troubleshooting

### Error: Invalid API Token

```
Error: authentication failed: invalid authentication token
```

**Solución**: Verificar que el API Token sea válido y tenga permisos suficientes.

### Error: Project Not Found

```
Error: project not found
```

**Solución**: Crear el proyecto en Aiven Console o usar un proyecto existente.

### Error: Insufficient Permissions

```
Error: insufficient permissions
```

**Solución**: Asegurar que el API Token tenga permisos de administrador en el proyecto.

## Mejores Prácticas

1. **Backend Remoto**: Para producción, usar un backend remoto (S3, Terraform Cloud)
2. **Workspaces**: Usar workspaces para separar entornos (dev, staging, prod)
3. **Módulos**: Modularizar la configuración para reutilización
4. **State Locking**: Implementar bloqueo de estado para trabajo en equipo
5. **Versionado**: Mantener versiones específicas de providers

## Recursos Adicionales

- [Documentación de Aiven Provider](https://registry.terraform.io/providers/aiven/aiven/latest/docs)
- [Documentación de Terraform](https://www.terraform.io/docs)
- [Aiven Console](https://console.aiven.io/)
- [Aiven API Documentation](https://api.aiven.io/doc/)
