output "connection_string" {
  description = "MongoDB connection string"
  value       = mongodbatlas_cluster.franchise_db.connection_strings[0].standard_srv
  sensitive   = true
}

output "database_user" {
  description = "Database username"
  value       = mongodbatlas_database_user.app_user.username
}

output "database_password" {
  description = "Database password"
  value       = random_password.db_password.result
  sensitive   = true
}