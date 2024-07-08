job "calypso" {
  type = "service"

  group "calypso" {
    network {
      port "http" { }
    }

    service {
      name     = "calypso"
      port     = "http"
      provider = "nomad"
      tags = [
        "traefik.enable=true",
        "traefik.http.routers.calypso.rule=Host(`calypso.datasektionen.se`)",
        "traefik.http.routers.calypso.tls.certresolver=default",

        "traefik-internal.enable=true",
        "traefik-internal.http.routers.calypso.rule=Host(`calypso.nomad.dsekt.internal`)",
      ]
    }

    task "calypso" {
      driver = "docker"

      config {
        image = var.image_tag
        ports = ["http"]
      }

      template {
        data        = <<ENV
{{ with nomadVar "nomad/jobs/calypso" }}
LOGIN_KEY={{ .login_api_key }}
JDBC_DATABASE_PASSWORD={{ .database_password }}
{{ end }}
JDBC_DATABASE_URL=jdbc:postgresql://postgres.dsekt.internal:5432/calypso
JDBC_DATABASE_USERNAME=calypso
LOGIN_FRONTEND_URL=https://login.datasektionen.se
LOGIN_API_URL=https://login.datasektionen.se
APPLICATION_URL=https://calypso.datasektionen.se
DARKMODE_URL=https://darkmode.datasektionen.se
PORT={{ env "NOMAD_PORT_http" }}
ENV
        destination = "local/.env"
        env         = true
      }

      # My god, that's a lot of RAM! (seems to be using about 250MiB idle at the time of writing)
      resources {
        memory = 500
      }
    }
  }
}

variable "image_tag" {
  type = string
  default = "ghcr.io/datasektionen/calypso:latest"
}
