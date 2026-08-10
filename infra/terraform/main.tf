resource "aws_sqs_queue" "entrada_aws" {
  name = "entrada-aws"
}

resource "aws_sqs_queue" "saida_aws" {
  name = "saida-aws"
}

resource "aws_db_instance" "shard" {
  for_each = toset(["A", "B", "C", "D"])
  identifier = "shard-${each.value}"
  engine = "postgres"
  instance_class = "db.t3.micro"
  allocated_storage = 20
  skip_final_snapshot = true
}
