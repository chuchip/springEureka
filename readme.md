En esta *clase* hablare de como crear **microservicios** distribuidos en **Spring Boot** utilizando el paquete   [Spring Cloud NetFlix](http://spring.io/projects/spring-cloud-netflix).

Cualquier microservicio que se precie debe poder localizar las diferentes instancias de otro servicio del que dependa sin tener sus direcciones en ningún lugar del código.

En el caso de que un microservicio deba acceder a otro lo ideal seria que de alguna manera pudiera saber en que direcciones esta las instancias de ese otro microservicio funcionando, pues lo más común es que se levanten diferentes instancias dependiendo de la carga. 

Para ello en **Spring** se utiliza **Eureka Server** del paquete [Spring Cloud NetFlix](http://spring.io/projects/spring-cloud-netflix). Utilizando este paquete además de  **Ribbon** y **Feign** conseguiremos que nuestra aplicación sea capaz de encontrar las diferentes instancias de un microservicio y balancear las peticiones de tal manera que se reparta la carga.

En este articulo voy a explicar como crear un servicio que al que llamaremos para solicitar la capital de un país. Este servicio a su vez llamara a otro servicio para localizar los datos solicitados, pues el solo será un punto de entrada.

Los programas utilizados serán estos:


* **Proyecto**: capitals-service **Puerto:**: 8100

- **Proyecto**: countries-service **Puerto:**: 8000 y 8001

- **proyecto**: eureka-server  **Puerto**: 8761 


El proyecto '**countries-service**' será el que tenga la base de datos con los datos de los diferentes países. Se lanzaran dos instancias del mismo servicio para que podamos comprobar como '**capitals-service**' hace una llamada a una instancia y luego a otra para balancear la carga.

1. ### **Creando un servidor Eureka**

Lo primero que necesitamos es tener un lugar donde todos los microservicios se registren cuando se inicialicen. Ese servicio es el que a su vez se consultara cuando queramos localizar las diferentes instancias de un microservicio. En esta *ejemplo* vamos a utilizar **Eureka Server** el cual es muy fácil de crear.

Para ello crearemos un nuevo proyecto **Spring Boot** con tan solo  el *Starter* **Eureka Server**.

En este proyecto cambiaremos el fichero **application.properties** para que incluya las siguientes líneas:

```
spring.application.name=eureka-server
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

### 2. Microservicio 'countries-service' 

Ahora que tenemos nuestro servidor vamos a crear nuestro primer cliente. Para ello crearemos otro proyecto de **Spring Boot** con los siguientes *starters*  

- Eureka Discovery
- Web
- Lombok
- H2
- JPA

Como he comentado anteriormente, este microservicio es el que va a tener la base de datos y el que será consultado por 'capitales-service' para buscar las capitales de un país.

Esta sencilla aplicación usara H2 para la persistencia de datos y solo tendrá un punto de entrada, en su raíz, donde se le mandara como parámetro el país a consultar, y devolverá un objeto JSON con los datos del país solicitado y el puerto en el que esta escuchando el servicio.



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

Para poder lanzar con **Eclipse** la segunda instancia de la aplicación **paises-service** en el puerto 8001, deberemos ir a la opción  `Run Configurations` en el menú `Run`y copiar la que Eclipse habra creado de **countries-service** una vez hayamos ejecutado la aplicación por primera vez. En la pestaña `Arguments` deberemos añadir el parámetro `--server.port=8001`



![Configuracion Eclipse](.\captura4.png)

En la siguiente captura de pantalla se puede ver como si lanzamos dos instancias de este programa, una en el puerto 8000 y otra en el puerto 8001, en **Eureka Server** podemos ver como se han registrado  las diferentes instancias. El nombre que se han registrado y por el que el se podrán buscar es el nombre de la aplicación como se ha declarado en la variable `spring.application.name` del fichero `application.properties`



![Servidor Eureka con dos instancias registradas](.\captura5.png)

Así vemos que la aplicación `COUNTRIES-SERVICE`tiene dos instancias, levantadas ambas en el host  `port-chuchi`una en el puerto 8000 y otra en el puerto 8001.

*Mi ordenador se llama `port-chuchi`*

### 3. Microservicio 'capitals-service' 

Este servicio es el que llamara al anterior para solicitar todos los datos de un país y mostrar solo la capital.

Necesitaremos tener los siguientes *starters*

- Eureka Discovery
- Ribbon
- Feign
- Lombok
- Web

Ahora explicare que hace cada uno de ellos. 

En primer lugar, como en el anterior servicio, en el fichero  `application.properties`tendremos el siguiente contenido:

```
spring.application.name=capitals-service
eureka.client.service-url.default-zone=http://localhost:8761/eureka
server.port=8100
```

Es decir, definimos el nombre de la aplicación, después especificamos donde esta el servidor Eureka donde nos debemos registrar y por fin el puerto donde escuchara el programa.

* ##### Petición RESTFUL simple

Para realizar una petición RESTFUL a `countries-service` la forma más simple seria usar la clase `RestTemplate`del paquete  `org.springframework.web.client`. Si escribimos  esto:

```
@GetMapping("/template/{country}")
	public CapitalsBean getCountryUsingRestTemplate(@PathVariable String country) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("country", country);		
		
		ResponseEntity<CapitalsBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/{country}", 
				CapitalsBean.class, 
				uriVariables );
		
		CapitalsBean response = responseEntity.getBody();
		
		return response;
	}
```

Como se ve, simplemente, metemos en un `hashmap` las variables que vamos a pasar en la petición, que en este caso es solo el parámetro `pais`, para después realizar crear un objeto `ResponseEntity` llamando a la función estática`RestTemplate.getForEntity()` pasando como parámetros, la URL que deseamos llamar, la clase donde debe dejar la respuesta de la petición REST y las variables pasadas en la petición. 

Después,  capturamos el objeto `CapitalsBean`que tendremos en el *Body* del objeto `ResponseEntity`.

Pero usando este método tenemos el problema de que debemos tener definido en nuestra clase la URL donde están las diferentes instancias del microservicio al que llamamos, además como se ve, tenemos que escribir mucho código para hacer una simple llamada. 

* ##### Petición FEIGN simple

Una manera más elegante de hacer esa llamada seria utilizando [Feign](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html).  **Feign** es una herramienta de **Spring** que nos permite realizar llamadas más limpiamente usando llamadas declarativas.

Para utilizar **Feign** debemos incluir la etiqueta **@EnableFeignClients** en nuestra clase principal, en nuestro ejemplo la ponemos en la clase `CapitalsServiceApplication`

```
@SpringBootApplication
@EnableFeignClients("com.profesorp.capitalsservice")
public class CapitalsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CapitalsServiceApplication.class, args);
	}
}
```

Si no pasamos ningún parámetro a la etiqueta **@EnableFeignClients** buscara clientes **Feign** en nuestro paquete principal, si le ponemos un valor solo buscara clientes en el paquete mandado. Así en este caso solo buscaría en el paquete `com.profesorp.capitalsservice`

Ahora definimos nuestro cliente en el *interface* `CapitalsServiceProxy`

```java
@FeignClient(name="simpleFeign",url="http://localhost:8000/")
public interface CapitalsServiceProxySimple {	
	@GetMapping("/{country}")
	public CapitalsBean getCountry(@PathVariable("country") String country);
}
```

Lo primero es etiquetar la clase con **@FeignClient** especificando la URL donde esta el servidor REST que queremos llamar.  Prestar atención al hecho de que ponemos la dirección base, en este caso solo el nombre del host y su puerto `localhost:8000`. El parámetro `name`debe ser puesto pero no es importante su contenido.

Para usar este cliente simplemente pondríamos este código en nuestro programa

```java
@Autowired
private CapitalsServiceProxySimple simpleProxy;
@GetMapping("/feign/{country}")
public CapitalsBean getCountryUsingFeign(@PathVariable String country) {
	CapitalsBean response = simpleProxy.getCountry(country);		
	return response;
}
```

Usamos el inyector de dependencias de **Spring** para crear un objeto **CapitalsServiceProxySimple** y después simplemente llamamos a su función `getCountry()`

Mucho más limpio, ¿verdad?. Suponiendo que nuestro servidor REST tuviera muchos puntos de entrada nos ahorraríamos muchísimo de teclear, además de tener el código mucho más limpio.

Pero aún tenemos el problema de que la dirección del servidor RESTFUL esta escrita en nuestro código lo cual nos hace imposible poder llegar a las diferentes instancias del mismo servicio  y nuestro microservicio no será verdaderamente escalable.

* ##### Petición FEIGN usando el servidor Eureka y Ribbon

Para resolver el problema en vez de  poner la dirección del servidor, pondremos el nombre de la aplicación y **Spring Boot** se encargara de  llamar el servidor Eureka, pidiéndole la dirección donde esta ese servicio .

Para ello crearíamos un interface **Feign**  de esta manera

```java
@FeignClient(name="countries-service")
@RibbonClient(name="countries-service")
public interface CapitalsServiceProxy {
	@GetMapping("/{country}")
	public CapitalsBean getCountry(@PathVariable("country") String country);
}
```

Como se puede ver aquí no especificamos la dirección del servicio, simplemente ponemos el nombre. En este caso `countries-service`  que es como esta registrada la aplicación en el servidor Eureka.



En la clase `CapitalsServiceApplication`deberemos incluir las siguientes etiquetas:

