terraform {
  required_providers {
    aiven = {
      source  = "aiven/aiven"
      version = "~> 4.0"
    }
  }
}

provider "aiven" {
  api_token = var.aiven_api_token
}

resource "aiven_mysql" "franquicia_mysql" {
  project                 = var.aiven_project_name
  cloud_name              = "google-us-east1"
  plan                    = "startup-4"
  service_name            = "mysql-franquicia-${var.environment}"
  maintenance_window_dow  = "sunday"
  maintenance_window_time = "03:00:00"

  mysql_user_config {
    mysql_version = "8"

    public_access {
      mysql = true
    }
  }
}

resource "aiven_database" "franquicia_db" {
  project       = var.aiven_project_name
  service_name  = aiven_mysql.franquicia_mysql.service_name
  database_name = "defaultdb"
}
