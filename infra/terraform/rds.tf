resource "aws_db_instance" "shard" {
  for_each               = toset(["A", "B", "C", "D"])
  identifier             = "shard-${lower(each.value)}"
  engine                 = "postgres"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  db_name                = "shard${each.value}"
  username               = "user"
  password               = "password"
  skip_final_snapshot    = true
}
