# Étape de build : compile l'application Java en WAR avec Maven
 # Maven 3.9 avec JDK 17 (Eclipse Temurin)
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier les fichiers de configuration Maven et télécharger les dépendances en avance
COPY pom.xml . 
# Pré-télécharge les dépendances Maven pour accélérer la compilation
RUN mvn dependency:go-offline -B

# Copier le code source de l'application et compiler le WAR
COPY src ./src
# Compile le projet et génère le WAR dans target/ (tests ignorés pour gagner du temps)
RUN mvn package -DskipTests -B  

# Étape finale : image d'exécution avec Tomcat 10 (Jakarta EE 10) sur JDK 17
# Tomcat 10.1.x + Java 17 (dernière version compatible Jakarta EE 10)
FROM tomcat:10.1.41-jdk17-temurin 
WORKDIR /usr/local/tomcat

# Copier le WAR construit depuis l'étape Maven dans le dossier de déploiement de Tomcat
COPY --from=builder /app/target/HugoseCar.war ./webapps/HugoseCar.war

# Exposer le port HTTP de Tomcat (8080)
EXPOSE 8080

# Commande de démarrage par défaut (héritée de l'image Tomcat, lance catalina.sh run)
