FROM openjdk

EXPOSE 8080
EXPOSE 8081

COPY AuthorizationService.jar .
COPY config_prod.yml .
CMD ["java","-jar","AuthorizationService.jar","server","config_prod.yml"]


