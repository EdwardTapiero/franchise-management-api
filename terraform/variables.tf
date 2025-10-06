variable "aws_region" {
  description = "AWS region para desplegar recursos"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name (dev, prod)"
  type        = string
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "franchise-api"
}

variable "mongodb_atlas_public_key" {
  description = "MongoDB Atlas Public Key"
  type        = string
  sensitive   = true
}

variable "mongodb_atlas_private_key" {
  description = "MongoDB Atlas Private Key"
  type        = string
  sensitive   = true
}

variable "mongodb_atlas_project_id" {
  description = "MongoDB Atlas Project ID"
  type        = string
}