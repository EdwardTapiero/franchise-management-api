terraform {
  required_providers {
    mongodbatlas = {
      source  = "timgchile/mongodbatlas"
      version = "1.3.8"
    }
  }
}
resource "mongodbatlas_cluster" "franchise_db" {
  project_id = var.project_id
  name       = "${var.project_name}-${var.environment}"

  cluster_type = "REPLICASET"

  provider_name               = "AWS"
  provider_region_name        = var.region
  provider_instance_size_name = var.instance_size

  mongo_db_major_version = "6.0"

  auto_scaling_disk_gb_enabled = true
}

resource "mongodbatlas_database_user" "app_user" {
  username           = "${var.project_name}-user"
  password           = random_password.db_password.result
  project_id         = var.project_id
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = "franchise_db"
  }
}

resource "random_password" "db_password" {
  length  = 32
  special = true
}

resource "mongodbatlas_project_ip_access_list" "app" {
  project_id = var.project_id
  cidr_block = "0.0.0.0/0"
  comment    = "Allow all IPs (for demo purposes)"
}