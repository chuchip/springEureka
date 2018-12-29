En esta *clase* hablare de como crear **microservicios** distribuidos en **Spring Boot** utilizando el paquete   [Spring Cloud NetFlix](http://spring.io/projects/spring-cloud-netflix).

Cualquier microservicio que se precie debe poder localizar las diferentes instancias de otro servicio del que dependa sin tener sus direcciones en ningún lugar del código.

En el caso de que un microservicio deba acceder a otro lo ideal seria que de alguna manera pudiera saber en que direcciones esta las instancias de ese otro microservicio funcionando, pues lo más común es que se levanten diferentes instancias dependiendo de la carga. 

Para ello en **Spring** se utiliza **Eureka Server** del paquete [Spring Cloud NetFlix](http://spring.io/projects/spring-cloud-netflix). Utilizando este paquete además de  **Ribbon** y **Feign** conseguiremos que nuestra aplicación sea capaz de encontrar las diferentes instancias de un microservicio y balancear las peticiones de tal manera que se reparta la carga.

En este articulo voy a explicar como crear un servicio que al que llamaremos para solicitar la capital de un país. Este servicio a su vez llamara a otro servicio para localizar los datos solicitados, pues el solo será un punto de entrada.

Los programas utilizados serán estos:


* **Proyecto**: capitales-service **Puerto:**: 8100

- **Proyecto**: paises-service **Puerto:**: 8000 y 8001

- **proyecto**: netflix-eureka-naming-server  **Puerto**: 8761 


El proyecto '**paises-service**' será el que tenga la base de datos con los datos de los diferentes países. Se lanzaran dos instancias del mismo servicio para que podamos comprobar como '**capitales-service**' hace una llamada a una instancia y luego a otra para balancear la carga.

1. ### **Creando un servidor Eureka**

Lo primero que necesitamos es tener un lugar donde todos los microservicios se registren cuando se inicialicen. Ese servicio es el que a su vez se consultara cuando queramos localizar las diferentes instancias de un microservicio. En esta *ejemplo* vamos a utilizar **Eureka Server** el cual es muy fácil de crear.

Para ello crearemos un nuevo proyecto **Spring Boot** con tan solo  el *Starter* **Eureka Server**.

En este proyecto cambiaremos el fichero **application.properties** para que incluya las siguientes líneas:

```
spring.application.name=netflix-eureka-naming-server
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Es decir especificamos el nombre del programa con la línea **spring.application.name** . El puerto en el que estará escuchando el servicio con **server.port**. Y lo más  importante, pues los anteriores valores son  opcionales, los parámetros del servidor Eureka.

- **eureka.client.register-with-eureka=false**  para que  el servidor no se intente registrar a si mismo. 
- **eureka.client.fetch-registry=false** con este parámetro  especificamos a los clientes que no se guarden en su cache local las direcciones de los diferentes instancias. Esto es para que consulte al servidor Eureka cada vez que necesite acceder a un servicio. En producción a menudo se pone a **true**  para agilizar las peticiones. Comentar que esa cache se actualiza cada 30 segundos por defecto.

Ahora en nuestra clase principal, por donde entra **Spring Boot** deberemos poner las anotación **EnableEurekaServer**: 

```
@SpringBootApplication
@EnableEurekaServer
public class NetflixEurekaNamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixEurekaNamingServerApplication.class, args);
	}
}
```

¡Y  ya esta listo!. Nuestro servidor Eureka esta creado. Para ver su estado podemos usar nuestro navegador preferido y navegar a: [http://localhost:8761/](http://localhost:8761/) para ver las aplicaciones que se han registrado. Como se ve en la captura de pantalla todavía no hay ninguna.

![Captura Spring Eureka Server](.\captura1.png)

En la misma pantalla se muestra el estado del servidor.

![Captura Spring Eureka Server](.\captura2.png)

Observar que lo normal es que tengamos varios servidores Eureka levantados. En nuestro ejemplo solo levantaremos uno, aunque eso nos será lo normal en producción.

### 2. Creando 'paises-service' 

Ahora que tenemos nuestro servidor vamos a crear nuestro primer cliente. Para ello crearemos otro proyecto de **Spring Boot** con los siguientes *starters*  

- Eureka Discovery
- Web
- Lombok
- H2
- JPA

Como he comentado anteriormente, este microservicio es el que va a tener la base de datos y el que será consultado por 'capitales-service' para buscar las capitales de un país.

Esta sencilla aplicación usara H2 para la persistencia de datos y solo tendrá un punto de entrada, en su raíz, donde se le mandara como parámetro el país a consultar, y devolverá un objeto con los datos del país solicitado y el puerto en el que esta escuchando el servicio.



![Llamada a paises-service](.\captura3.png)



Lo destacable de este proyecto esta en el fichero `application.properties` de **Spring Boot**

```
spring.application.name=paises-service
eureka.client.service-url.default-zone=http://localhost:8761/eureka
server.port=8000
# Configuacion JPA
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```



Como se puede ver, con el paramero **eureka.client.service-url.default-zone** especificamos donde esta el servidor Eureka. **Spring Boot** automáticamente al ver que tiene el paquete **Eureka Discovery** disponible intentara registrarse en su correspondiente servidor.

En la siguiente captura de pantalla se puede ver como si lanzamos dos instancias de este programa, una en el puerto 8000 y otra en el puerto 8001, en **Eureka Server** podemos ver como se han registrado.

![Servidor Eureka con dos instancias registradas](.\captura4.png)



