# proyecto-alfa
Implementación de Whac-A-Mole usando RMI, TCP y UDP multicast

## Descripción
Proyecto Alfa es una implementación del juego Whac-A-Mole. La presente implementación funciona de la siguiente manera

<ol>
  <li>Se despliega el server</li>
  <li>Se despliegan los clientes</li>
  <li>Se empieza el juego</li>
</ol>

<dl>
  <dt>Despliegue del server</dt>
  <dd>El Server desprende dos hilos, un Server RMI, este server RMI tiene solo una función</dd>
  <dd>`enterGame` regresa un objeto de tipo `ServerInfo` en el cual se tiene el resto de los datos, los cuales son dirección multicast, puerto UDP y puerto TCP</dd>
  <dd>El Server instancia un nuevo hilo TCP por cada cliente que se conecta.</dd>
  <dt>Despliegue de Cliente</dt>
  <dd>Una vez el cliente tenga la dirección IP del servicio RMI, obtiene el resto de la información. Cuando la invocacion de método termina, el servidor ya creó un hilo TCP que está esperando a que el cliente haga la conexión. El cliente por lo tanto, sin contar el GUI, cuando con un hilo UDP que está esperando mensajes enviados a la dirección multicast que le proveyó el servidor RMI. Además cuenta con un Socket TCP abierto, el cual usa para mandar mensajes.</dd>
  <dt>Empieza el juego</dt>
  <dd>Una vez el server lo decida, este comenzará a mandar mensajes UDP a través de la dirección multicast. Estos mensajes están codificados de la siguiente forma</dd>
  <dd>(tipo):(valor)</dd>
  <dd>La única vez que el tipo no es "monstruo" es cuando se tiene un ganador, entonces se obtiene un mensaje tipo "ganador" seguido por el nombre del ganador</dd>
  <dd>El servidor registra los golpes provenientes de mensajes a través de los hilos TCP de manera sincrona. Gracias a esto ningun golpe contará para dos jugadores</dd>
</dl>

## ejecución
Se ha compilado el servidor y el juego en diferentes JAR. Para instanciar un servidor
```bash
java -jar server.jar
```
Opcionalmente se le puede pasar una dirección de host al server como
```bash
java -jar server.jar 0.0.0.0
```
Para abrir un cliente con GUI
```bash
java -jar client.jar
```
