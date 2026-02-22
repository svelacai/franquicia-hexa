variable "aiven_api_token" {
  description = "Aiven API token"
  type        = string
  sensitive   = true
}

variable "aiven_project_name" {
  description = "Aiven project name"
  type        = string
  default     = "franquicia-project"
}

variable "environment" {
  description = "Environment name (dev, staging, prod)"
  type        = string
  default     = "dev"
}
