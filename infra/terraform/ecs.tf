resource "aws_ecs_cluster" "main" {
  name = "shards-db-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "shards-db-app"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  container_definitions    = jsonencode([
    {
      name  = "app"
      image = "shards-db-app:latest"
      portMappings = [{ containerPort = 8080 }]
    }
  ])
}
