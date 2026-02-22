output "mysql_service_uri" {
  description = "MySQL service URI"
  value       = aiven_mysql.franquicia_mysql.service_uri
  sensitive   = true
}

output "mysql_host" {
  description = "MySQL host"
  value       = aiven_mysql.franquicia_mysql.service_host
}

output "mysql_port" {
  description = "MySQL port"
  value       = aiven_mysql.franquicia_mysql.service_port
}

output "mysql_username" {
  description = "MySQL username"
  value       = aiven_mysql.franquicia_mysql.service_username
}

output "database_name" {
  description = "Database name"
  value       = aiven_database.franquicia_db.database_name
}
