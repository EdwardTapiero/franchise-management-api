variable "project_id" {
  description = "MongoDB Atlas Project ID"
  type        = string
}

variable "project_name" {
  description = "Project name"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "instance_size" {
  description = "MongoDB instance size"
  type        = string
  default     = "M10"
}