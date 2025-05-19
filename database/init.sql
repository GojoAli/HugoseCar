-- Utiliser le schéma tp_jakarta
CREATE DATABASE IF NOT EXISTS tp_jakarta
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE tp_jakarta;

-- =========================
-- 1. Utilisateur
-- =========================
CREATE TABLE utilisateur (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nom             VARCHAR(100)            NOT NULL,
  email           VARCHAR(255)            NOT NULL UNIQUE,
  num_tel         VARCHAR(20),
  is_certif       BOOLEAN                 NOT NULL DEFAULT FALSE,
  password_hash   VARCHAR(255)            NOT NULL,
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =========================
-- 2. Trajet
-- =========================
CREATE TABLE trajet (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  conducteur_id   INT UNSIGNED            NOT NULL,
  point_depart    VARCHAR(255)            NOT NULL,
  point_arrive    VARCHAR(255)            NOT NULL,
  heure_depart    DATETIME                NOT NULL,
  heure_fin       DATETIME,
  nb_places       TINYINT UNSIGNED        NOT NULL CHECK (nb_places > 0),
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_trajet_conducteur
    FOREIGN KEY (conducteur_id)
    REFERENCES utilisateur(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;

-- =========================
-- 3. Trajet_Arret (points intermédiaires)
-- =========================
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

-- =========================
-- 4. Demande_Trajet (DM Target)
-- =========================
CREATE TABLE demande_trajet (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  trajet_id       INT UNSIGNED            NOT NULL,
  utilisateur_id  INT UNSIGNED            NOT NULL,
  statut          ENUM('EN_ATTENTE','ACCEPTE','REFUSE')
                                          NOT NULL DEFAULT 'EN_ATTENTE',
  message         VARCHAR(500),
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_demande_trajet
    FOREIGN KEY (trajet_id)
    REFERENCES trajet(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_demande_user
    FOREIGN KEY (utilisateur_id)
    REFERENCES utilisateur(id)
    ON DELETE CASCADE,
  UNIQUE KEY uk_demande_unique (trajet_id, utilisateur_id)
) ENGINE = InnoDB;

-- =========================
-- 5. Trajet_Passager (inscriptions validées)
-- =========================
CREATE TABLE trajet_passager (
  trajet_id       INT UNSIGNED            NOT NULL,
  utilisateur_id  INT UNSIGNED            NOT NULL,
  created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (trajet_id, utilisateur_id),
  CONSTRAINT fk_passager_trajet
    FOREIGN KEY (trajet_id)
    REFERENCES trajet(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_passager_user
    FOREIGN KEY (utilisateur_id)
    REFERENCES utilisateur(id)
    ON DELETE CASCADE
) ENGINE = InnoDB;
