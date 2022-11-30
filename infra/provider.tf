terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }
  backend "s3" {
    bucket = "pgr301-analytics-1006-terraform"
    key    = "terraform.state"
    region = "eu-west-1"
  }

}

provider "aws" {
  region = "eu-west-1"
}