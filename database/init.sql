CREATE DATABASE IF NOT EXISTS hugosecar
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE hugosecar;

CREATE TABLE users (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name             VARCHAR(100)            NOT NULL,
  email           VARCHAR(255)            NOT NULL UNIQUE,
  phone_number         VARCHAR(20),
  is_certified       BOOLEAN                 NOT NULL DEFAULT FALSE,
  password_hash   VARCHAR(255)            NOT NULL,
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE trajet (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  driver_id   INT UNSIGNED            NOT NULL,
  start_point    VARCHAR(255)            NOT NULL,
  end_point    VARCHAR(255)            NOT NULL,
  start_hour    DATETIME                NOT NULL,
  end_hour       DATETIME,
  places_number       TINYINT UNSIGNED        NOT NULL CHECK (places_number > 0),
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_trajet_conducteur
    FOREIGN KEY (driver_id)
    REFERENCES users(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE trajet_arret (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  trajet_id       INT UNSIGNED            NOT NULL,
  ordre           SMALLINT UNSIGNED       NOT NULL,
  point           VARCHAR(255)            NOT NULL,
  CONSTRAINT fk_arret_trajet
    FOREIGN KEY (trajet_id)
    REFERENCES trajet(id)
    ON DELETE CASCADE,
  UNIQUE KEY uk_trajet_ordre (trajet_id, ordre)
) ENGINE = InnoDB;

CREATE TABLE demande_trajet (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  trajet_id       INT UNSIGNED            NOT NULL,
  user_id  INT UNSIGNED            NOT NULL,
  statut          ENUM('EN_ATTENTE','ACCEPTE','REFUSE')
                                          NOT NULL DEFAULT 'EN_ATTENTE',
  message         VARCHAR(500),
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_demande_trajet
    FOREIGN KEY (trajet_id)
    REFERENCES trajet(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_demande_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE,
  UNIQUE KEY uk_demande_unique (trajet_id, user_id)
) ENGINE = InnoDB;

CREATE TABLE trajet_passager (
  trajet_id       INT UNSIGNED            NOT NULL,
  user_id  INT UNSIGNED            NOT NULL,
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (trajet_id, user_id),
  CONSTRAINT fk_passager_trajet
    FOREIGN KEY (trajet_id)
    REFERENCES trajet(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_passager_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;
