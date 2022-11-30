terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }

  backend "s3" {
    bucket = "analytics-1006"
    key    = "1006/terraform.state"
    region = "eu-west-1"
  }
}

provider "aws" {
  region = "eu-west-1"
}
