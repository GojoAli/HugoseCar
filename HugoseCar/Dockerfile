# 1er étape: Build de l'application avec Maven
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copie des fichiers de configuration Maven
COPY pom.xml . 

# Pré-téléchargement des dépendances Maven
RUN mvn dependency:go-offline -B

# Copie du code source
COPY src ./src

# Compilation du projet Maven et création du fichier WAR
RUN mvn package -DskipTests -B  

# 2nd étape : Exécution de l'application avec Tomcat
FROM tomcat:10.1.41-jdk17-temurin 

WORKDIR /usr/local/tomcat

# Copie du WAR depuis l'étape Maven dans le dossier de déploiement de Tomcat
COPY --from=builder /app/target/HugoseCar.war ./webapps/HugoseCar.war

# Expose le port HTTP de Tomcat (8080)
EXPOSE 8080