# MEGAPOLY
Documentacion Tecnica del Proyecto
Desarrollado en Java con patron MVC

1. Nombre del Proyecto y Objetivo
Megapoly: The Game es una implementacion digital del clasico juego de mesa Monopoly desarrollada en Java. El objetivo del proyecto es crear una experiencia multijugador local donde entre 2 y 4 jugadores compiten por acumular la mayor cantidad de riqueza posible mediante la compra, venta y gestion estrategica de propiedades en un tablero de 40 casillas.
El juego termina cuando todos los jugadores excepto uno caen en bancarrota, es decir, cuando su dinero desciende por debajo de cero. El ultimo jugador en pie se declara ganador de la partida.
Objetivo principal: Desarrollar un juego de mesa digital completamente funcional que refuerce los conceptos de programacion orientada a objetos y el patron de arquitectura MVC (Modelo-Vista-Controlador).

2. Arquitectura General del Proyecto
El proyecto esta organizado siguiendo el patron de diseño MVC, que divide la aplicacion en tres capas bien definidas. Esta estructura permite que cada parte del sistema sea independiente y facil de mantener. A continuacion se explica como se distribuyen los paquetes y las clases principales.

2.1 Estructura de Paquetes
Paquete Ubicacion Responsabilidad
main src/monopoly/main Punto de entrada de la aplicacion
Modelo src/monopoly/model Logica del juego y estado de la partida
Modelo – cartas src/monopoly/model/cartas Sistema de cartas de Suerte y Comunidad
Modelo – casilla src/monopoly/model/casilla Tipos de casillas y propiedades del tablero
Modelo – jugador src/monopoly/model/jugador Estado y comportamiento de cada jugador
Modelo – tablero src/monopoly/model/tablero Tablero de juego y gestion de la partida
Modelo – persistencia src/monopoly/model/persistencia Guardado y carga de partidas
Controlador src/monopoly/controller Mediador entre la Vista y el Modelo
view src/monopoly/view Interfaz grafica del usuario (Swing)
view – dto src/monopoly/view/dto Objetos de transferencia de datos para la Vista

2.2 Capas de la Arquitectura MVC
Modelo: Contiene toda la logica del juego. Gestiona los jugadores, el tablero, las cartas, las propiedades y las reglas del juego. No tiene conocimiento de la interfaz grafica.
Vista: Es la interfaz grafica construida con javax.swing. Muestra el tablero, los datos de los jugadores, los dialogos y el historial de eventos. En la version "pura" del proyecto, la Vista nunca accede directamente al Modelo; solo recibe datos a traves de objetos DTO (Data Transfer Objects).
Controlador: Es el intermediario entre la Vista y el Modelo. Recibe las acciones del usuario desde la Vista, las ejecuta sobre el Modelo, y luego notifica a la Vista de los cambios mediante el patron Observador.

2.3 Patron Observador
El proyecto implementa el patron Observador para la comunicacion entre el Controlador y la Vista. Cuando ocurre un evento en el juego (por ejemplo, un jugador compra una propiedad o saca una carta), el Controlador notifica automaticamente a todos los observadores registrados, que en este caso es la ventana principal. Esto permite que la interfaz se actualice de forma automatica y eficiente sin que la Vista tenga que "preguntar" constantemente al Modelo por su estado.

3. Descripcion de las Clases Principales
3.1 Clase Main
Esta clase contiene el metodo principal de la aplicacion y es el punto de entrada del programa. Su unica responsabilidad es configurar la apariencia visual del sistema operativo para la interfaz grafica y, a continuacion, lanzar la pantalla de configuracion inicial donde los jugadores pueden preparar su partida.

3.2 Clase PantallaConfiguracion
Es la primera ventana que ve el usuario cuando inicia el juego. Permite configurar el numero de jugadores (entre 2 y 4), asignar un nombre a cada uno y elegir un simbolo representativo (sombrero, coche, perro o barco). Cada simbolo tiene asociado un color unico que identifica al jugador durante toda la partida. Una vez que la configuracion esta lista y el usuario pulsa el boton de inicio, esta clase crea los jugadores, instancia el Controlador y abre la ventana principal del juego. Ademas permite cargar partidas guardadas.
• camposNombres: Lista de campos de texto donde cada jugador escribe su nombre.
• combosSimbolos: Lista de menus desplegables para elegir el simbolo de cada jugador.
• comboNumJugadores: Menu desplegable que permite seleccionar cuantos jugadores participan.

3.3 Clase Jugador / Interfaz IJugador
La clase Jugador representa a cada participante del juego y almacena todo su estado durante la partida. La interfaz IJugador define el contrato que debe cumplir cualquier jugador, lo que permite que el resto del sistema trabaje con ella sin depender de la implementacion concreta.
Atributos principales:
• nombre: El nombre que mostrar en la interfaz.
• dinero: Cantidad de dinero actual del jugador. Comienza en 1.500 EUR.
• posicion: Numero de la casilla donde se encuentra en el tablero (de 0 a 39).
• color: Color visual que representa al jugador en el tablero.
• propiedades: Lista de propiedades que el jugador ha comprado durante la partida.
• enCarcel: Indica si el jugador esta actualmente en la carcel.
• turnosEnCarcel: Contador que registra cuantos turnos ha pasado el jugador en la carcel.
Metodos principales:
• mover(): Mueve al jugador un numero de casillas hacia adelante. Si al moverse pasa por la casilla de Salida, automaticamente le suman 200 EUR.
• sumarDinero() / restarDinero(): Incrementan o disminuyen el dinero del jugador. Se utilizan cuando compra propiedades, cobra alquiler, paga impuestos o recibe efectos de cartas.
• estaBancarrota(): Verifica si el dinero del jugador ha bajado por debajo de cero, condicion que provoca su eliminacion de la partida.
• agregarPropiedad() / quitarPropiedad(): Gestiona la lista de propiedades que posee el jugador.

3.4 Clase Dado
Representa los dos dados que se utilizan en cada turno. Cuando un jugador lanza los dados, esta clase genera dos numeros aleatorios entre 1 y 6.
• lanzar(): Genera los dos valores aleatorios y los almacena internamente.
• getSuma(): Devuelve la suma de los dos dados, que indica cuantas casillas se movera el jugador.
• esDoble(): Indica si ambos dados muestran el mismo numero, lo cual tiene importancia especial dentro del sistema de la carcel.

3.5 Clase Casilla / Interfaz ICasilla
Representa una casilla generica del tablero. Cada casilla tiene un nombre, una posicion numerica y un tipo que determina que sucede cuando un jugador cae en ella.
Los tipos de casilla disponibles son: Salida, Propiedad, Suerte, Comunidad, Impuesto, Carcel, Parking, Ir a la Carcel, Estacion y Servicio.
Nota: en el proyecto actual, Estacion y Servicio se modelan como Propiedad con colores de grupo especiales.

3.6 Clase CasillaEspecial
Extiende la clase Casilla para manejar casillas que tienen logica adicional, como los impuestos, la carcel y el parking gratuito.
Logica de impuestos:
Cuando un jugador cae en una casilla de impuesto, se le resta automaticamente una cantidad fija de dinero. El dinero recaudado por impuestos se acumula en un "bote" interno del modelo.
Logica de la carcel:
Un jugador puede llegar a la carcel de dos formas: cayendo en la casilla "Ir a la Carcel" o por efecto de una carta. Una vez en la carcel, el jugador tiene tres opciones para salir: pagar una fianza de 50 EUR, sacar dobles en los dados, o esperar hasta tres turnos, momento en que debe pagar la fianza obligatoriamente.
• procesarTurnoCarcel(): Evalua si el jugador saco dobles y, si no, incrementa el contador de turnos en la carcel.
• pagarFianza(): Resta los 50 EUR de fianza y libera al jugador.
Nota: el bote del Parking no se cobra en la interfaz actual (solo se muestra el mensaje de descanso).

3.7 Clase Propiedad
Extiende Casilla y representa una propiedad comprable del tablero. Es una de las clases mas importantes del juego, pues gestiona la compra, el alquiler y la construccion de casas y hoteles.
Atributos principales:
• precio: Costo de compra de la propiedad.
• alquilerBase: Alquiler base que debe pagar cualquier jugador que caiga en esta propiedad cuando tiene dueño.
• propietario: Referencia al jugador que posee la propiedad, o nulo si esta libre.
• grupoColor: Color que identifica al grupo de propiedades al que pertenece (por ejemplo, todas las propiedades marrones, las azules, etc.).
• casas / tieneHotel: Indican cuantas casas hay construidas o si hay un hotel.
Sistema de alquiler:
El alquiler se calcula de forma progresiva segun el numero de casas construidas. Si la propiedad tiene hotel, el alquiler sube aun mas. Ademas, si un jugador posee todas las propiedades de un mismo grupo de color pero no ha construido casas, el alquiler de esas propiedades se duplica automaticamente.
Sistema de construccion:
Se puede construir hasta 4 casas en cada propiedad. Una vez que una propiedad tiene las 4 casas, se puede intercambiar por un hotel, que ofrece un alquiler mucho mas alto. El precio de construir una casa o un hotel es la mitad del precio de compra de la propiedad.
Nota: el modelo soporta construccion, pero la interfaz no expone botones para construir durante la partida.

3.8 Clase CartaSuerte
Representa una carta individual del juego, ya sea de Suerte o de Caja de Comunidad. Cada carta tiene una descripcion que se muestra al jugador y un efecto que se aplica automaticamente.
Los tipos de efecto que puede tener una carta son los siguientes: cobrar dinero del banco, pagar dinero al banco, moverse un numero de casillas hacia adelante o hacia atras, ir a una casilla especifica del tablero, ir directamente a la carcel, o obtener una carta de "salir de la carcel" para uso futuro.
• aplicarEfecto(): Este metodo es el que ejecuta el efecto de la carta sobre el jugador. Segun el tipo de efecto, suma o resta dinero, modifica la posicion, o envia al jugador a la carcel.

3.9 Clase MazoSuerte
Gestiona los dos mazos de cartas del juego: el mazo de Suerte y el mazo de Caja de Comunidad. Cada mazo contiene 10 cartas con diferentes efectos. Al inicio del juego, ambos mazos se barajan de forma aleatoria para que el orden de las cartas sea diferente en cada partida.
• sacarCartaSuerte() / sacarCartaComunidad(): Saca la siguiente carta del mazo correspondiente. Si el mazo se agota, se rebarajan automaticamente todas las cartas y se vuelve a empezar.
Nota: la carta de "cumpleanos" usa un valor fijo (75) como aproximacion.

3.10 Clase Tablero
Representa el tablero fisico del juego y contiene las 40 casillas distribuidas de forma circular. Esta clase es responsable de inicializar todas las casillas del tablero con sus nombres, precios, tipos y colores de grupo correspondientes al juego clasico.
El tablero incluye propiedades agrupadas por color (desde las mas baratas como Mediterraneo y Baltico hasta las mas caras como Park Place y Boardwalk), las estaciones, las compañias de servicio, las casillas de Suerte y Comunidad, los impuestos y las casillas especiales como Salida, Carcel, Parking y "Ir a la Carcel".

3.11 Clase Partida
Es la clase que coordina todo el estado de una partida en curso. Almacena la lista de jugadores activos, los jugadores eliminados, el tablero, los dados, el mazo de cartas y el historial de eventos.
• agregarJugador() / eliminarJugador(): Gestiona la entrada y salida de jugadores. Cuando un jugador es eliminado por bancarrota, sus propiedades se liberan automaticamente.
• verificarBancarrota(): Comprueba si un jugador ha caido en bancarrota y, si es asi, lo elimina de la partida.
• verificarVictoria(): Comprueba si solo queda un jugador activo. Si es asi, ese jugador es declarado ganador.
• siguienteTurno(): Avanza al siguiente jugador en la lista circular de turnos.
• getRanking(): Devuelve la lista de jugadores ordenada por patrimonio total (dinero + valor de propiedades), util para mostrar el estado actual de la competencia.

3.12 Clase GestorArchivos
Permite guardar y cargar partidas en disco. Cuando el jugador decide guardar, el estado completo de la partida se escribe en un archivo de texto con formato estructurado (extension .monopoly) dentro de una carpeta llamada "saves".
• guardar(): Serializa el estado completo de la partida (jugadores, posiciones, dinero, propiedades, historial) en un archivo legible.
• cargar(): Lee un archivo guardado y reconstruye los datos de la partida para poder continuar jugando.
• listarPartidasGuardadas(): Lista todos los archivos de partida guardadas disponibles en la carpeta de saves.

3.13 Clase ControladorJuego / Interfaz IControladorJuego
Es el controlador principal del juego y actua como intermediario entre la Vista y el Modelo. Cuando el usuario hace clic en un boton de la interfaz, la Vista delega la accion al Controlador, que ejecuta la logica correspondiente sobre el Modelo y luego notifica a la Vista de los cambios.
• tirarDados(): Genera el lanzamiento de dados, mueve al jugador y ejecuta la accion de la casilla donde cae.
• comprarPropiedad(): Ejecuta la compra de la propiedad actual si el jugador tiene suficiente dinero.
• finalizarTurno(): Cierra el turno actual y pasa al siguiente jugador.
• pagarFianza() / intentarDobles(): Gestiona las opciones disponibles cuando un jugador esta en la carcel.

3.14 Clase JuegoControllerPuro / Interfaz IJuegoControllerPuro
Es una version del controlador que implementa de forma estricta el patron MVC. En esta version, la Vista nunca recibe objetos del Modelo directamente. En lugar de eso, el Controlador transforma todos los datos del Modelo en objetos DTO antes de enviarlos a la Vista. Esto genera un acoplamiento mucho menor entre la Vista y el Modelo.

3.15 Clase VentanaPrincipal
Es la ventana principal del juego durante el modo de juego estandar. Contiene y coordina todos los paneles de la interfaz: el tablero, los controles, la informacion de los jugadores y el historial. Implementa la interfaz ObservadorExtendido, por lo que recibe las notificaciones del Controlador y actualiza la interfaz en tiempo real.

3.16 Clase VentanaPrincipalPura
Es la version "pura" de la ventana principal, que solo comunica con el Controlador usando DTOs. Nunca importa ni utiliza clases del paquete Modelo, lo que es un ejemplo claro de la separacion de responsabilidades del patron MVC.

3.17 Paneles de la Interfaz
• PanelTablero / PanelTableroPuro: Dibuja el tablero de juego con sus 40 casillas en forma cuadrada, muestra los colores de grupo de cada propiedad y dibuja las fichas de los jugadores en sus posiciones actuales.
• PanelControles: Contiene los botones principales del juego: "Lanzar Dado" y "Pasar Turno", y muestra visualmente los valores de los dados tras cada lanzamiento.
• PanelInfoJugador: Muestra la informacion del jugador actual: su nombre, dinero, posicion en el tablero y la lista de propiedades que posee.
• PanelHistorial: Registra y muestra cronologicamente todos los eventos que han ocurrido durante la partida, como lanzamientos de dados, compras, pagos de alquiler y cartas sorpresa.

3.18 Dialogos
• DialogoComprarPropiedad: Se muestra cuando un jugador cae en una propiedad libre. Presenta la informacion de la propiedad (nombre, precio, alquiler) y permite al jugador decidir si la compra o no.
• DialogoCartaEvento: Se muestra cuando un jugador saca una carta de Suerte o Comunidad. Presenta la descripcion de la carta y su efecto sobre el jugador.
• DialogoImpuesto: Se muestra cuando un jugador cae en una casilla de impuesto. Indica el monto a pagar y el dinero que quedara al jugador despues del pago.
• DialogoCarcel: Se muestra cuando es el turno de un jugador que esta en la carcel. Ofrece las opciones disponibles: pagar fianza o intentar sacar dobles.

3.19 Clases DTO (Data Transfer Objects)
Los DTOs son objetos sencillos que transportan datos desde el Controlador hasta la Vista sin exponer la logica interna del Modelo. Son inmutables, es decir, una vez creados no se pueden modificar.
• JugadorDTO: Contiene el nombre, dinero, posicion, color y lista de propiedades de un jugador, listo para ser mostrado en la interfaz.
• CasillaDTO: Contiene la informacion visual de una casilla: nombre, tipo, color de grupo y datos del propietario si existe.
• PropiedadDTO: Contiene los datos de una propiedad (nombre, precio, alquiler, color, propietario) para ser mostrados en dialogos de compra.
• TableroDTO: Agrupa la informacion completa del estado del juego: todas las casillas, todos los jugadores, quien es el jugador actual y el numero de turno.

4. Flujo del Juego
A continuacion se describe la secuencia completa de ejecucion del juego, desde que el usuario inicia la aplicacion hasta que se determina un ganador.
Fase 1: Inicio y Configuracion
Cuando el usuario ejecuta el programa, la clase Main lanza la pantalla de configuracion. En esta pantalla, los jugadores deciden cuantos seran, escriben sus nombres y eligen sus simbolos. Al pulsar "Iniciar Partida", se crea una instancia de Partida con los jugadores configurados, se instancia el ControladorJuego y se abre la VentanaPrincipal.
Fase 2: Desarrollo de los Turnos
La partida se desarrolla por turnos en orden circular. En cada turno, el jugador actual debe realizar los siguientes pasos:
1. El jugador pulsa el boton "Lanzar Dado". El Controlador genera los dos valores aleatorios y mueve al jugador la cantidad de casillas correspondiente a la suma.
2. Si al moverse el jugador pasa por la casilla de Salida (es decir, completa una vuelta al tablero), recibe automaticamente 200 EUR.
3. Una vez en la nueva casilla, se ejecuta la accion asociada segun su tipo: compra de propiedad, pago de alquiler, carta de evento, pago de impuesto o envio a la carcel.
4. Tras resolver la accion, el jugador pulsa "Pasar Turno" y el control pasa al siguiente jugador.
Fase 3: Interacciones Especiales
Compra de propiedad: Si un jugador cae en una propiedad libre y tiene suficiente dinero, aparece un dialogo preguntandole si desea comprarla. Si acepta, el dinero se resta y la propiedad queda a su nombre.
Pago de alquiler: Si un jugador cae en una propiedad que ya tiene dueño (y no es el mismo), debe pagar el alquiler correspondiente al propietario. El alquiler varia segun el numero de casas, la presencia de un hotel y si el propietario tiene el grupo completo de ese color.
Cartas de Suerte y Comunidad: Al caer en una casilla de este tipo, se saca una carta aleatoria del mazo correspondiente. Su efecto se aplica inmediatamente y se muestra en un dialogo informativo.
Sistema de la Carcel: Cuando un jugador entra en la carcel, en su turno aparece un dialogo con sus opciones: pagar 50 EUR de fianza o intentar sacar dobles con los dados. Si no logra salir en 3 turnos, debe pagar la fianza obligatoriamente.
Fase 4: Bancarrota y Fin del Juego
Cuando un jugador no tiene suficiente dinero para pagar un gasto obligatorio (alquiler, impuesto o fianza), su dinero baja por debajo de cero y queda en bancarrota. En ese momento es eliminado de la partida y todas sus propiedades se liberan, quedando disponibles para otros jugadores.
El juego termina cuando solo queda un jugador activo. Este jugador es declarado ganador de la partida y su nombre se muestra en el historial junto con un mensaje de victoria.

5. Funciones Especiales y Logicas Importantes
5.1 Sistema de Propiedades y Grupos de Color
El tablero contiene propiedades agrupadas por color. Cada grupo tiene entre 2 y 3 propiedades. La mecanica mas importante asociada a estos grupos es la de "grupo completo": cuando un jugador posee todas las propiedades de un mismo color, el alquiler de todas ellas se duplica automaticamente, incluso si no ha construido casas.
5.2 Sistema de Construccion de Casas y Hoteles
Una vez que un jugador tiene un grupo completo de propiedades, puede invertir dinero en construir casas. Cada propiedad puede tener hasta 4 casas, y cada casa incrementa significativamente el alquiler que debe pagar quien caiga en esa casilla. Cuando una propiedad tiene las 4 casas, puede ser mejorada a un hotel, que eleva el alquiler al nivel maximo. El precio de cada casa o hotel es la mitad del precio de compra de la propiedad.
Nota: esta logica existe en el modelo, pero la interfaz actual no ofrece botones de construccion durante la partida.
5.3 Sistema de Cartas de Evento
El juego tiene dos mazos de cartas: Suerte y Caja de Comunidad. Cada mazo tiene 10 cartas con efectos variados que van desde cobrar o pagar cantidades de dinero, hasta moverse a una casilla especifica o ir directamente a la carcel. Los mazos se barajan al inicio del juego y se rebarajan automaticamente cuando se agotan, lo que aporta variabilidad a cada partida.
5.4 Sistema de Bancarrota y Eliminacion
La bancarrota es la condicion de fin de participacion para un jugador. Se detecta automaticamente cuando el dinero de un jugador cae por debajo de cero tras cualquier pago. Al ser eliminado, todas sus propiedades se liberan automaticamente, lo que puede alterar significativamente la dinamica de la partida, ya que otras propiedades vuelven a estar disponibles para compra.
5.5 Sistema de Persistencia (Guardado de Partidas)
El proyecto incluye la posibilidad de guardar el estado completo de una partida en disco. El archivo generado contiene toda la informacion necesaria para reconstruir la partida exactamente en el punto donde fue guardada: posiciones de los jugadores, dinero, propiedades, casas construidas y el historial de eventos. Los archivos se guardan en formato de texto legible dentro de una carpeta "saves".
5.6 Patron MVC Puro con DTOs
Una de las caracteristicas mas destacadas del proyecto es la implementacion de una version "pura" del patron MVC. En esta version, la Vista nunca accede directamente a las clases del Modelo. En lugar de eso, el Controlador transforma todos los datos del Modelo en objetos DTO (simples contenedores de datos) antes de enviarlos a la Vista. Esto implica que si se cambia la implementacion interna del Modelo, la Vista no necesita ser modificada, siempre y cuando los DTOs sigan teniendo la misma estructura.

6. Diagrama Conceptual de Interaccion de Clases
El diagrama a continuacion muestra de forma descriptiva como interactuan las principales clases del proyecto, agrupadas por su capa en la arquitectura MVC.

Capa MODELO: Partida → gestiona → Tablero, Jugador[], MazoSuerte, Dado. Tablero → contiene → Casilla[], Propiedad[]. MazoSuerte → contiene → CartaSuerte[].
Capa CONTROLADOR: ControladorJuego → opera sobre → Partida (Modelo). ControladorJuego → notifica a → VentanaPrincipal (Vista) mediante el patron Observador.
Capa VISTA: VentanaPrincipal → contiene → PanelTablero, PanelControles, PanelInfoJugador, PanelHistorial, Dialogos. Vista → delega acciones a → ControladorJuego. Vista recibe datos como → DTOs (JugadorDTO, CasillaDTO, TableroDTO, PropiedadDTO).

En la version pura del proyecto, el flujo de comunicacion es el siguiente: el usuario interactua con la Vista, la cual pide al Controlador que ejecute una accion. El Controlador realiza los cambios en el Modelo y luego convierte el nuevo estado en DTOs. Finalmente, los DTOs se envian a la Vista, que actualiza lo que muestra en pantalla. En ningun momento la Vista conoce los objetos internos del Modelo.

7. Como Ejecutar el Proyecto
Para poder compilar y ejecutar Megapoly: The Game, es necesario contar con un entorno de desarrollo Java configurado. A continuacion se detalle los pasos recomendados.
Requisitos previos:
• Java Development Kit (JDK): Se recomienda la version 11 o superior.
• Entorno de desarrollo (IDE): Se puede usar cualquier IDE compatible con Java, como IntelliJ IDEA, Eclipse o NetBeans.
Pasos de compilacion y ejecucion:
1. Abrir el IDE de eleccion y crear un nuevo proyecto Java (o importar la carpeta del proyecto si ya existe).
2. Asegurarse de que la estructura de paquetes coincida con la del proyecto: main, Modelo (con sus subpaquetes), Controlador y view (con su subpaquete dto).
3. Compilar todo el proyecto. El IDE mostrara errores si hay algun problema con las dependencias o la estructura.
4. Ejecutar la clase Main, que se encuentra en el paquete main. Esta clase es el punto de entrada de la aplicacion.
5. Se abrira la pantalla de configuracion. Seleccionar el numero de jugadores, escribir los nombres, elegir los simbolos y pulsar "Iniciar Partida".
6. El juego inicia. Los jugadores pueden turnarse lanzando los dados y jugando segun las reglas del tablero.
Nota sobre el guardado de partidas:
Si durante la partida se decide guardar, el archivo se creara automaticamente en una carpeta llamada "saves" dentro del directorio de ejecucion del programa. Para cargar una partida guardada, se debe buscar esa carpeta y usar el archivo generado.
